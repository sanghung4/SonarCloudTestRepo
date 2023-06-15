package com.reece.platform.inventory.util;

import org.springframework.util.StringUtils;

public class StringUtil {

    public static String formatCountId(String id) {
        if (id.contains("PHYS:")) {
            return id.replace("PHYS:", "").replaceFirst("^0+(?!$)", "");
        }
        return id;
    }

    public static boolean hasNullorEmptyValue(String val) {
        if (StringUtils.isEmpty(val) || StringUtils.isEmpty(val.trim())) {
            return true;
        }
        return false;
    }

    public static boolean hasNullorEmptyValueOrUndefined(String val) {
        if (hasNullorEmptyValue(val) || val.equalsIgnoreCase("Undefined")) {
            return true;
        }
        return false;
    }
}
