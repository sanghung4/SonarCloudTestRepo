package com.reece.platform.eclipse.exceptions;

public class CreditCardNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Credit Card with id %s for account %s not found.\"}";

    public CreditCardNotFoundException(String creditCardId, String accountId) { super(String.format(DEFAULT_MESSAGE, creditCardId, accountId)); }
}
