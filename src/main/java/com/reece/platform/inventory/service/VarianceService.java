package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.NextLocationDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.exception.NextLocationNotFoundException;
import com.reece.platform.inventory.exception.VarianceNotFoundException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.external.mincron.MincronService;
import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.model.VarianceCountItemStatus;
import com.reece.platform.inventory.model.VarianceItemQuantity;
import com.reece.platform.inventory.model.variance.VarianceDetails;
import com.reece.platform.inventory.repository.CountLocationItemRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.VarianceCountItemQuantityRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VarianceService {

    private final EclipseService eclipseService;
    private final MincronService mincronService;

    private final CountLocationItemRepository countLocationItemRepository;
    private final LocationCountRepository locationCountRepository;
    private final VarianceCountItemQuantityRepository varianceCountItemQuantityRepository;

    private final ERPSystemFactory erpSystemFactory;

    public VarianceSummaryDTO getVarianceSummary(String countId, String branchId) {
        if (
            Objects.equals(ERPSystemName.fromBranchNumber(branchId), ERPSystemName.ECLIPSE)
        ) return eclipseService.getVarianceSummary(countId); else return mincronService.getMincronVarianceSummary(
            countId,
            branchId
        );
    }

    public void loadVarianceDetails(String branchId, String countId) {
        VarianceDetailsDTO varianceDetails = eclipseService.getVarianceDetails(countId);
        for (VarianceDetails productVariance : varianceDetails.getCounts()) {
            CountItem item = countLocationItemRepository
                .findCountItem(branchId, countId, productVariance.getLocation(), productVariance.getErpProductID())
                .orElse(null);
            if (item == null) {
                log.warn(
                    String.format(
                        "Failed to load variance for branch/count/location/productId: %s,%s,%s,%s",
                        branchId,
                        countId,
                        productVariance.getLocation(),
                        productVariance.getErpProductID()
                    )
                );
                continue;
            }
            item.setVarianceStatus(VarianceCountItemStatus.UNCOUNTED);
            item.setVarianceCost(productVariance.getCountedCost() - productVariance.getOnHandCost());
            countLocationItemRepository.save(item);
        }
    }

    public VarianceLocationsDTO getVarianceLocations(String branchId, String countId) {
        val locations = locationCountRepository.findVarianceLocationSummaries(branchId, countId);
        return new VarianceLocationsDTO(locations, locations.size());
    }

    public VarianceLocationDTO getVarianceLocation(String branchId, String countId, String locationId)
        throws VarianceNotFoundException {
        try {
            val varianceLocation = locationCountRepository.findVarianceLocation(branchId, countId, locationId);
            return varianceLocation.get();
        } catch (NoSuchElementException e) {
            throw new VarianceNotFoundException(branchId, countId, locationId);
        }
    }

    public NextLocationDTO getVarianceNextLocation(String branchId, String countId, String locationId) {
        return locationCountRepository
            .getVarianceNextLocationId(branchId, countId, locationId)
            .map(NextLocationDTO::new)
            .orElseThrow(NextLocationNotFoundException::new);
    }

    public void stageVarianceCount(
        String branchId,
        String countId,
        String locationId,
        String productId,
        Integer quantity
    ) {
        val countItem = countLocationItemRepository
            .findVarianceCountItem(branchId, countId, locationId, productId)
            .orElseThrow();
        if (quantity == null) {
            rollbackVarianceStageCount(countItem);
        } else {
            countItem.setMostRecentQuantity(quantity);
            countItem.setVarianceStatus(VarianceCountItemStatus.STAGED);

            countLocationItemRepository.save(countItem);
        }
    }

    private void rollbackVarianceStageCount(CountItem countItem) {
        if (!VarianceCountItemStatus.STAGED.equals(countItem.getVarianceStatus())) {
            return;
        }

        countItem.setMostRecentQuantity(null);
        countItem.setVarianceStatus(VarianceCountItemStatus.UNCOUNTED);

        countLocationItemRepository.save(countItem);
    }

    public void commitVarianceCount(String branchId, String countId, String locationId) {
        val erpSystem = erpSystemFactory.getErpSystem(branchId);
        val stagedItems = countLocationItemRepository.findVarianceStagedCountItems(branchId, countId, locationId);

        if (stagedItems.size() <= 0) return;

        val itemsToUpdate = new ArrayList<PostCountDTO>();
        val itemQuantities = new HashSet<VarianceItemQuantity>();

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

            // Update in DB
            val varianceItemQuantity = new VarianceItemQuantity();
            varianceItemQuantity.setCountItem(item);
            varianceItemQuantity.setQuantity(item.getMostRecentQuantity());
            itemQuantities.add(varianceItemQuantity);
            item.setVarianceStatus(VarianceCountItemStatus.COMMITTED);
        }

        erpSystem.updateCount(branchId, countId, locationId, itemsToUpdate);

        countLocationItemRepository.saveAll(stagedItems);
        varianceCountItemQuantityRepository.saveAll(itemQuantities);
    }

    public VarianceDetailsDTO getVarianceDetails(String countId, String branchId) {
        if (
            Objects.equals(ERPSystemName.fromBranchNumber(branchId), ERPSystemName.ECLIPSE)
        ) return eclipseService.getVarianceDetails(countId); else return mincronService.getMincronVarianceDetails(
            countId,
            branchId
        );
    }
}
