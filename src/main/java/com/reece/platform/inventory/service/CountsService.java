package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.dto.LocationSummaryDTO;
import com.reece.platform.inventory.dto.LocationsDTO;
import com.reece.platform.inventory.dto.NextLocationDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.exception.CountNotFoundException;
import com.reece.platform.inventory.exception.LocationNotFoundException;
import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountItemQuantity;
import com.reece.platform.inventory.model.CountLocationItemStatus;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.model.QCountItem;
import com.reece.platform.inventory.model.VarianceCountItemStatus;
import com.reece.platform.inventory.repository.CountItemQuantityRepository;
import com.reece.platform.inventory.repository.CountLocationItemRepository;
import com.reece.platform.inventory.repository.CountRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CountsService {

    private final CountRepository countRepository;
    private final CountLocationItemRepository countLocationItemRepository;
    private final LocationCountRepository locationCountRepository;
    private final CountItemQuantityRepository countItemQuantityRepository;

    private final ERPSystemFactory erpSystemFactory;

    public Optional<CountDTO> getCount(String branchId, String countId) {
        val erpSystem = ERPSystemName.fromBranchNumber(branchId);

        if (erpSystem == ERPSystemName.ECLIPSE) {
            if (countId != null) {
                countId = CountsService.stripLeadingZero(countId);
            }
        }

        return countRepository.findCount(branchId, countId).map(CountDTO::fromEntity);
    }

    public static String stripLeadingZero(String countId) {
        countId = countId.trim();
        if (countId.charAt(0) == '0') {
            countId = countId.substring(1);
        }
        return countId;
    }

    public LocationsDTO getLocations(String branchId, String countId) {
        var count = countRepository.findCount(branchId, countId).orElseThrow(CountNotFoundException::new);

        val locations = locationCountRepository.findLocationSummaries(branchId, count.getId());
        val totalLocations = locations.size();
        val totalCounted = locations.stream().filter(LocationSummaryDTO::isCommitted).count();
        return new LocationsDTO(totalLocations, totalCounted, locations);
    }

    public LocationDTO getLocation(String branchId, String countId, String locationId) {
        return locationCountRepository
            .findLocation(branchId, countId, locationId)
            .orElseThrow(LocationNotFoundException::new);
    }

    public NextLocationDTO getNextLocation(String branchId, String countId, String locationId) {
        return locationCountRepository.getNextLocationId(branchId, countId, locationId).map(NextLocationDTO::new).get();
    }

    public void stageCount(String branchId, String countId, String locationId, String tagNum, Integer quantity) {
        val countItem = countLocationItemRepository.findCountItem(branchId, countId, locationId, tagNum).orElseThrow();

        if (quantity == null) {
            rollbackStageCount(countItem);
        } else {
            countItem.setMostRecentQuantity(quantity);
            countItem.setStatus(CountLocationItemStatus.STAGED);

            countLocationItemRepository.save(countItem);
        }
    }

    private void rollbackStageCount(CountItem countItem) {
        if (!CountLocationItemStatus.STAGED.equals(countItem.getStatus())) {
            return;
        }

        countItemQuantityRepository
            .findMostRecentQuantity(countItem)
            .ifPresentOrElse(
                countItemQuantity -> {
                    countItem.setMostRecentQuantity(countItemQuantity.getQuantity());
                    countItem.setStatus(CountLocationItemStatus.COMMITTED);
                },
                () -> {
                    countItem.setMostRecentQuantity(null);
                    countItem.setStatus(CountLocationItemStatus.UNCOUNTED);
                }
            );

        countLocationItemRepository.save(countItem);
    }

    public void commitCount(String branchId, String countId, String locationId) {
        val erpSystem = erpSystemFactory.getErpSystem(branchId);

        val locationCount = locationCountRepository.findLocationCount(branchId, countId, locationId).orElseThrow();
        val stagedItems = countLocationItemRepository.findStagedCountItems(branchId, countId, locationId);

        if (stagedItems.size() <= 0) return;

        val itemQuantities = new HashSet<CountItemQuantity>();
        val itemsToUpdate = new ArrayList<PostCountDTO>();

        for (val item : stagedItems) {
            PostCountDTO postCountDTO = new PostCountDTO(
                branchId,
                countId,
                locationId,
                item.getProductNum(),
                item.getMostRecentQuantity(),
                item.getTagNum()
            );
            itemsToUpdate.add(postCountDTO);

            // Record Count in DB
            val countItemQty = new CountItemQuantity();
            countItemQty.setCountItem(item);
            countItemQty.setQuantity(item.getMostRecentQuantity());
            itemQuantities.add(countItemQty);
            item.setStatus(CountLocationItemStatus.COMMITTED);
        }

        erpSystem.updateCount(branchId, countId, locationId, itemsToUpdate);

        countLocationItemRepository.saveAll(stagedItems);
        countItemQuantityRepository.saveAll(itemQuantities);
        locationCount.setCommitted(true);
        locationCountRepository.save(locationCount);
    }

    public LocationProductDTO addToCount(
        String branchId,
        String countId,
        String locationId,
        String prodNum,
        int quantity
    ) {
        val locationCount = locationCountRepository.findLocationCount(branchId, countId, locationId).orElseThrow();

        val nextSequence = (int) countLocationItemRepository.count(
            QCountItem.countItem.locationCount.eq(locationCount)
        );

        val erpSystem = erpSystemFactory.getErpSystem(branchId);

        erpSystem.addToCount(branchId, countId, locationId, prodNum, quantity);
        val locationProduct = erpSystem
            .loadLocationItems(branchId, countId, locationId)
            .stream()
            .filter(i -> i.getProdNum().equals(prodNum))
            .findFirst()
            .orElseThrow();

        val countItem = locationProduct.toEntity();
        countItem.setSequence(nextSequence);
        countItem.setLocationCount(locationCount);
        countItem.setMostRecentQuantity(quantity);
        countItem.setVarianceStatus(VarianceCountItemStatus.NONVARIANCE);
        countItem.setStatus(CountLocationItemStatus.COMMITTED);

        val countItemQty = new CountItemQuantity();
        countItemQty.setCountItem(countItem);
        countItemQty.setQuantity(countItem.getMostRecentQuantity());
        countLocationItemRepository.save(countItem);
        countItemQuantityRepository.save(countItemQty);

        return LocationProductDTO.fromEntity(countItem);
    }
}
