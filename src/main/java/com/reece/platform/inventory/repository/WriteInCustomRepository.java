package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.model.WriteIn;
import java.util.Date;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WriteInCustomRepository {
    Page<WriteIn> findAllWriteIns(String branchId, String countId, Pageable pageRequest);
    Integer deleteWriteIns(UUID countId);
    Integer deleteWriteIns(ERPSystemName erpSystemName, Date endDate);
}
