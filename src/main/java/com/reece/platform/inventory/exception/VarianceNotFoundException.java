package com.reece.platform.inventory.exception;

public class VarianceNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "Variance not found for branchId: %s , coundId: %s and locationId: %s ";

    public VarianceNotFoundException(String branchId, String countId, String locationId) {
        super(String.format(DEFAULT_MESSAGE, branchId, countId, locationId));
    }
}
