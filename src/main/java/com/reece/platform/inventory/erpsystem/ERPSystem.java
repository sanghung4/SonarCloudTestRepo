package com.reece.platform.inventory.erpsystem;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import java.util.List;

public interface ERPSystem {
    CountDTO validateCount(String branchId, String countId);

    List<LocationDTO> loadAllLocationItems(String branchId, String countId);

    List<LocationProductDTO> loadLocationItems(String branchId, String countId, String locationId);

    void updateCount(String branchId, String countId, String locationId, List<PostCountDTO> itemsToUpdate);

    void addToCount(String branchId, String countId, String locationId, String prodNum, int quantity);
}
