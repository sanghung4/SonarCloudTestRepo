package com.reece.platform.products.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractDTO {

    private String contractNumber;
    private String description;
    private String contractDate;
    private String promiseDate;
    private String firstReleaseDate;
    private String lastReleaseDate;
    private String jobNumber;
    private String jobName;
    private String purchaseOrderNumber;
}
