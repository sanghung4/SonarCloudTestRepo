package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.ERPSystemName;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteCountsDTO {

    private ERPSystemName erpSystemName;
    private String endDate;

    @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid")
    private String branchId;

    @NotBlank(message = "Invalid parameter: 'countId' is blank, which is not valid")
    private String countId;

    public DeleteCountsDTO(ERPSystemName erpSystemName, String endDate) {
        this.erpSystemName = erpSystemName;
        this.endDate = endDate;
    }
}
