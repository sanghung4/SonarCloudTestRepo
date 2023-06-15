package com.reece.platform.inventory.util;

import com.reece.platform.inventory.model.TechDoc;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class Util {

    public static final String INVALID_SEARCH_INPUT_TYPE = "Allowed values for searchInputType are 0 and 1.";

    public static boolean isValidUrl(String url) {
        return url.contains("http");
    }

    public static boolean isValidUrl(TechDoc doc) {
        return isValidUrl(doc.getUrl());
    }

    public static final String ECLIPSE_CONNECT_400_EXCEPTION = "PHYSICAL.COUNT::PUT.COUNT: No product";
    public static final String PERIOD = ".";
    public static final String UNDEFINED = "Undefined";

    public static boolean isValidSearchInputType(String inputType) {
        if (Objects.equals(inputType, "0") || Objects.equals(inputType, "1") || UNDEFINED.equalsIgnoreCase(inputType)) {
            return true;
        }
        return false;
    }
}
