package com.reece.platform.products.exceptions;

public class BranchNotFoundPricingAndAvailabilityException extends Exception {

    private static final String DEFAULT_MESSAGE =
        "Unable to find ship to branch with id %s for product availability and pricing.";

    public BranchNotFoundPricingAndAvailabilityException(String shipToBranchId) {
        super(String.format(DEFAULT_MESSAGE, shipToBranchId));
    }
}
