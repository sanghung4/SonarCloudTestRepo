package com.reece.platform.eclipse.exceptions;

public class ToteLockedException extends Exception{
    private static final String DEFAULT_MESSAGE = "Tote %s is currently locked.";

    public ToteLockedException(String toteId) {
        super(String.format(DEFAULT_MESSAGE, toteId));
    }
}
