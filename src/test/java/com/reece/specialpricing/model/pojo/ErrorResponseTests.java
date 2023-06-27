package com.reece.specialpricing.model.pojo;

import org.junit.Test;

public class ErrorResponseTests {
    @Test
    public void shouldSetCountWhenInitializedWithOneError(){
        var errorDetails = new ErrorDetails("someField", "some error", "some exception");
        var model = new ErrorResponse(errorDetails);
        assert model.getErrorCount() == 1;
        assert model.getErrors().size() == 1;
        assert model.getErrors().get(0).equals(errorDetails);
    }
}
