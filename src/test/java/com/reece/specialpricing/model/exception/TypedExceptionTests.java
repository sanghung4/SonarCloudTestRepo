package com.reece.specialpricing.model.exception;

import org.junit.Test;

public class TypedExceptionTests {
    @Test
    public void shouldSetMessageAndTypeOnInitialization(){
        TypedException exception = new TypedException("something even cooler happened", "someField");
        assert exception.getErrorField().equals("someField");
        assert exception.getExceptionType().equals("Typed");
        assert exception.getMessage().equals("something even cooler happened");
    }

    @Test
    public void shouldSetMessageAndNotTypeOnInitialization(){
        TypedException exception = new TypedException("something even cooler happened");
        assert exception.getErrorField() == null;
        assert exception.getExceptionType().equals("Typed");
        assert exception.getMessage().equals("something even cooler happened");
    }
}
