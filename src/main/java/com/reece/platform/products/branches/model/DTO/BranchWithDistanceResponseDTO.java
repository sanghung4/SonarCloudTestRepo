package com.reece.platform.products.branches.model.DTO;

import com.reece.platform.products.branches.model.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BranchWithDistanceResponseDTO extends BranchResponseDTO {

    public BranchWithDistanceResponseDTO() {}

    public BranchWithDistanceResponseDTO(Branch branch) {
        this.setBranchId(branch.getBranchId());
        this.setName(branch.getName());
        this.setEntityId(branch.getEntityId());
        this.setAddress1(branch.getAddress1());
        this.setAddress2(branch.getAddress2());
        this.setCity(branch.getCity());
        this.setState(branch.getState());
        this.setZip(branch.getZip());
        this.setPhone(branch.getPhone());
        this.distanceToBranch = branch.getDistance();
        this.setErpSystemName(branch.getErpSystemName());
        this.setWebsite(branch.getWebsite());
        this.setWorkdayId(branch.getWorkdayId());
        this.setWorkdayLocationName(branch.getWorkdayLocationName());
        this.setActingBranchManager(branch.getActingBranchManager());
        this.setActingBranchManagerPhone(branch.getActingBranchManagerPhone());
        this.setActingBranchManagerEmail(branch.getActingBranchManagerEmail());
        this.setBusinessHours(branch.getBusinessHours());
        this.setLatitude(branch.getLatitude());
        this.setLongitude(branch.getLongitude());
        this.setIsPlumbing(branch.getIsPlumbing());
        this.setIsWaterworks(branch.getIsWaterworks());
        this.setIsHvac(branch.getIsHvac());
        this.setIsBandK(branch.getIsBandK());
    }

    private Float distanceToBranch;
}
