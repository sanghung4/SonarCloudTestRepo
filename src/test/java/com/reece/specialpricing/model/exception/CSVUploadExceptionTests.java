package com.reece.specialpricing.model.exception;

import org.junit.Test;

public class CSVUploadExceptionTests {
    @Test
    public void shouldSetMessageAndNotTypeOnInitialization(){
        TypedException exception = new CSVUploadException("something cool happened");
        assert exception.getErrorField() == null;
        assert exception.getExceptionType().equals("CSVUpload");
        assert exception.getMessage().equals("something cool happened");
    }
}
