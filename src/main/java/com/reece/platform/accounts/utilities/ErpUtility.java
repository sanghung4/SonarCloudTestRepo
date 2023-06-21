package com.reece.platform.accounts.utilities;

import com.reece.platform.accounts.model.enums.ErpEnum;

public class ErpUtility {

    public static final String MINCRON_BRAND_NAME = "Fortiline Waterworks";

    public static ErpEnum getErpFromBrand(String brand) {
        if (MINCRON_BRAND_NAME.equals(brand)) {
            return ErpEnum.MINCRON;
        }
        return ErpEnum.ECLIPSE;
    }
}
