package com.reece.platform.products.helpers;

import com.reece.platform.products.model.ErpUserInformation;
import java.util.regex.Pattern;

public enum ERPSystem {
    ECLIPSE("Reece"),
    MINCRON("Fortiline Waterworks");

    private final String defaultBrand;

    ERPSystem(String defaultBrand) {
        this.defaultBrand = defaultBrand;
    }

    public String getDefaultBrand() {
        return this.defaultBrand;
    }

    private static final Pattern ECLIPSE_PRODUCT_NUMBER_PATTERN = Pattern.compile("^MSC-.*");

    public static ERPSystem fromProductNumber(String fromProductNumber) {
        if (ECLIPSE_PRODUCT_NUMBER_PATTERN.matcher(fromProductNumber).matches()) {
            return ECLIPSE;
        }

        return MINCRON;
    }

    public static boolean isEclipseUser(ErpUserInformation erpUserInformation) {
        if (erpUserInformation.getErpSystemName() != null) {
            return erpUserInformation.getErpSystemName().equals(ECLIPSE.name());
        } else return false;
    }
}
