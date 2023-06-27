package com.reece.specialpricing.model.exception;

import org.junit.Test;

public class ParameterExceptionTests {
    @Test
    public void shouldSetMessageAndTypeOnInitialization(){
        TypedException exception = new ParameterException("something else cool happened", "someField");
        assert exception.getErrorField().equals("someField");
        assert exception.getExceptionType().equals("InvalidParameter");
        assert exception.getMessage().equals("something else cool happened");
    }
}
