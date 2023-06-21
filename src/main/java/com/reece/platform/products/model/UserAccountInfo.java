package com.reece.platform.products.model;

import com.reece.platform.products.helpers.ERPSystem;
import java.util.UUID;
import lombok.Data;

@Data
public class UserAccountInfo {

    private UUID userId;
    private UUID shipToAccountId;
    private String shippingBranchId;
    private ERPSystem erpSystemName;
}
