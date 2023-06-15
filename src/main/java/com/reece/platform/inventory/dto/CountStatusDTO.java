package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.dto.internal.MincronCountDTO;
import com.reece.platform.inventory.external.eclipse.EclipseCountStatusDTO;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.CountStatus;
import com.reece.platform.inventory.util.DateUtil;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountStatusDTO {

    private UUID id;
    private String branchId;
    private String countId;
    private String branchName;
    private CountStatus status;
    private String errorMessage;
    private Date createdAt;
    private Integer totalProducts;

    public CountStatusDTO(Count count, Integer totalProducts) {
        this.id = count.getId();
        this.branchId = count.getBranch().getErpBranchNum();
        this.countId = count.getErpCountId();
        this.branchName = count.getBranch().getName();
        this.status = count.getStatus();
        this.errorMessage = count.getErrorMessage();
        this.createdAt = count.getCreatedAt();
        this.totalProducts = totalProducts;
    }

    public CountStatusDTO(MincronCountDTO count) {
        this.branchId = count.getBranchNum();
        this.countId = count.getCountId();
        this.branchName = null;
        this.status = CountStatus.NOT_LOADED;
        this.errorMessage = null;
        this.createdAt = DateUtil.extractDateFromString(count.getCountDate());
        this.totalProducts = count.getNumItems();
    }

    public CountStatusDTO(EclipseCountStatusDTO count) {
        this.branchId = count.getBranchNum();
        this.countId = count.getCountId();
        this.branchName = count.getBranchNum();
        this.status = CountStatus.NOT_LOADED;
        this.errorMessage = null;
        this.createdAt = DateUtil.extractDateTimeFromString(count.getCountDate());
        this.totalProducts = count.getNumItems();
    }

    public static CountStatusDTO fromEntity(Count count) {
        return new CountStatusDTO(
            count.getId(),
            count.getBranch().getErpBranchNum(),
            count.getErpCountId(),
            count.getBranch().getName(),
            count.getStatus(),
            count.getErrorMessage(),
            count.getCreatedAt(),
            null
        );
    }

    public Boolean withinDateRange(Date startDate, Date endDate) {
        return createdAt.after(startDate) && createdAt.before(endDate);
    }
}
