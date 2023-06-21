package com.reece.platform.products.exceptions;

public class ContractNotFoundException extends RuntimeException {

    public ContractNotFoundException(String accountId, String contractNumber) {
        super("Contract '" + contractNumber + "' not found for account '" + accountId + "'.");
    }
}
