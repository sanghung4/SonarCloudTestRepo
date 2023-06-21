package com.reece.platform.notifications.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BranchAddressDTO extends BaseAddressDTO {
    private String branchName;
    private String brand;
    private String branchPhone;
    private String branchHours;
}
