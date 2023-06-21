package com.reece.platform.accounts.model.enums;

public enum RoleEnum {
    STANDARD_ACCESS("Standard Access"),
    INVOICE_ONLY("Invoice Only"),
    ACCOUNT_ADMIN("Account Admin"),
    MORSCO_ADMIN("Morsco Admin"),
    MORSCO_ENGINEER("Morsco Engineer"),
    PURCHASE_NO_INVOICE("Purchase - No Invoices"),
    PURCHASE_WITH_APPROVAL("Purchase with Approval");

    public final String label;

    RoleEnum(String label) {
        this.label = label;
    }
}
