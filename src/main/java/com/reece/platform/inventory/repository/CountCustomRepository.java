package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.dto.CountStatusDTO;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.ERPSystemName;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountCustomRepository {
    Optional<Count> findCount(String branchId, String countId);
    List<CountStatusDTO> findCountsByTimeRange(Date startDate, Date endDate);
    List<Count> findCountsByIds(List<String> countIds);
    Integer delete(ERPSystemName erpSystemName, Date endDate);
    List<Count> findMincronCountsBetweenDates(Date startDate, Date endDate);
    Integer getNumberOfProductsForCount(UUID uuid);
    List<UUID> findCountsUUID(ERPSystemName erpSystemName, String countId, String branchId);
    List<Count> findCountsByErpSystemAndEndDate(ERPSystemName erpSystemName, Date endDate);
}
