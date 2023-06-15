package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.ERPSystemName;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountItemCustomRepository {
    Optional<CountItem> findCountItem(String branchId, String countId, String locationId, String tagNum);
    List<CountItem> findStagedCountItems(String branchId, String countId, String locationId);
    Optional<CountItem> findVarianceCountItem(String branchId, String countId, String locationId, String tagNum);
    List<CountItem> findVarianceStagedCountItems(String branchId, String countId, String locationId);
    Integer delete(UUID countId);
    Integer delete(ERPSystemName erpSystemName, Date endDate);
}
