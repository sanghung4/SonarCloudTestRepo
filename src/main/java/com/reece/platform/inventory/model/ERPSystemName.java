package com.reece.platform.inventory.model;

import java.util.regex.Pattern;

public enum ERPSystemName {
    ECLIPSE,
    MINCRON;

    private static final Pattern ECLIPSE_BRANCH_NUMBER_PATTERN = Pattern.compile("^(\\d{4}$)");

    public static ERPSystemName fromBranchNumber(String branchNumber) {
        if (ECLIPSE_BRANCH_NUMBER_PATTERN.matcher(branchNumber).matches()) {
            return ECLIPSE;
        }

        return MINCRON;
    }
}
