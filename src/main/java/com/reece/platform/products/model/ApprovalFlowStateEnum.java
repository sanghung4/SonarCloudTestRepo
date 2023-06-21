package com.reece.platform.products.model;

public enum ApprovalFlowStateEnum {
    ACTIVE("Active"),
    AWAITING_APPROVAL("Awaiting approval"),
    SUBMITTED("Submitted"),
    REJECTED("Rejected");

    private final String displayName;

    ApprovalFlowStateEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
