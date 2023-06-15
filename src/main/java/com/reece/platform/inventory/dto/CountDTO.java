package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.external.eclipse.EclipseBatchDTO;
import com.reece.platform.inventory.external.mincron.MincronCountDTO;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.CountStatus;
import com.reece.platform.inventory.model.ERPSystemName;
import java.util.Date;
import lombok.Value;

@Value
public class CountDTO {

    String branchId;
    String countId;
    String branchName;
    ERPSystemName erpSystem;
    CountStatus status;
    String errorMessage;
    Date createdAt;

    public static CountDTO fromEntity(Count count) {
        return new CountDTO(
            count.getBranch().getErpBranchNum(),
            count.getErpCountId(),
            count.getBranch().getName(),
            count.getBranch().getErpSystem(),
            count.getStatus(),
            count.getErrorMessage(),
            count.getCreatedAt()
        );
    }

    public static CountDTO fromMincronCountDto(MincronCountDTO mincronCountDTO) {
        return new CountDTO(
            mincronCountDTO.getBranchNumber(),
            mincronCountDTO.getCountId(),
            mincronCountDTO.getBranchName(),
            ERPSystemName.MINCRON,
            null,
            null,
            null
        );
    }

    public static CountDTO fromEclipseBatchDTO(EclipseBatchDTO eclipseBatchDTO) {
        return new CountDTO(
            eclipseBatchDTO.getBranchId(),
            eclipseBatchDTO.getCountId(),
            eclipseBatchDTO.getBranchName(),
            ERPSystemName.ECLIPSE,
            null,
            null,
            null
        );
    }
}
