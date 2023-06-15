package com.reece.platform.inventory.erpsystem;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import com.reece.platform.inventory.exception.InvalidCountForBranchException;
import com.reece.platform.inventory.external.mincron.MincronPostCountDTO;
import com.reece.platform.inventory.external.mincron.MincronService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MincronERPSystem implements ERPSystem {

    private final MincronService mincronService;

    @Override
    public CountDTO validateCount(String branchId, String countId) {
        val mincronCount = mincronService.getCount(branchId, countId);
        if (!branchId.equals(mincronCount.getBranchNumber())) {
            throw new InvalidCountForBranchException();
        }
        return CountDTO.fromMincronCountDto(mincronCount);
    }

    @Override
    public List<LocationDTO> loadAllLocationItems(String branchId, String countId) {
        return mincronService
            .getAllLocations(branchId, countId)
            .stream()
            .map(LocationDTO::fromMincronLocationDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<LocationProductDTO> loadLocationItems(String branchId, String countId, String locationId) {
        val mincronLocation = mincronService.getLocation(branchId, countId, locationId);
        return mincronLocation
            .getItems()
            .stream()
            .map(LocationProductDTO::fromMincronItemDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void updateCount(String branchId, String countId, String locationId, List<PostCountDTO> itemsToUpdate) {
        List<MincronPostCountDTO> listOfMincronProducts = itemsToUpdate
            .stream()
            .map(MincronPostCountDTO::new)
            .collect(Collectors.toList());

        mincronService.updateCount(branchId, countId, listOfMincronProducts);
    }

    @Override
    public void addToCount(String branchId, String countId, String locationId, String prodNum, int quantity) {
        mincronService.addToCount(branchId, countId, locationId, prodNum, quantity);
    }
}
