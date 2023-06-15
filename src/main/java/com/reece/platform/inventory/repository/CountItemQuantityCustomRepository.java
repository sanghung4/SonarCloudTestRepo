package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.CountItemQuantity;
import com.reece.platform.inventory.model.ERPSystemName;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface CountItemQuantityCustomRepository {
    Optional<CountItemQuantity> findMostRecentQuantity(CountItem countItem);
    Integer delete(UUID countId);
    Integer delete(ERPSystemName erpSystemName, Date endDate);
}
