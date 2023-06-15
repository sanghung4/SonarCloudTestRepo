package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.model.VarianceItemQuantity;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface VarianceCountItemQuantityCustomRepository {
    Optional<VarianceItemQuantity> findMostRecentQuantity(CountItem countItem);
    Integer delete(UUID countId);
    Integer delete(ERPSystemName erpSystemName, Date endDate);
}
