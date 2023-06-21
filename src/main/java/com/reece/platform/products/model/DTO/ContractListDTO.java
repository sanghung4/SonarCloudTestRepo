package com.reece.platform.products.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractListDTO {

    String contractNumber;
    String description;
    String contractDate;
    String promiseDate;
    String firstReleaseDate;
    String lastReleaseDate;
    String jobNumber;
    String jobName;
    String purchaseOrderNumber;
}
