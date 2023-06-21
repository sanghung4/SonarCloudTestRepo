package com.reece.platform.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErpUserInformation {

    private String userId;
    private String password;
    private String erpAccountId;
    private String name;
    private String erpSystemName;

    private String customerNumber;
}
