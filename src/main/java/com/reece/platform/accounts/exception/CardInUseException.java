package com.reece.platform.accounts.exception;

public class CardInUseException extends Exception {
    private static final String DEFAULT_MESSAGE = "{\"error\":\"Cannot delete card with id %s. Card is in use on an open order.\"}";

    public CardInUseException(String creditCardId) { super(String.format(DEFAULT_MESSAGE, creditCardId)); }
}
