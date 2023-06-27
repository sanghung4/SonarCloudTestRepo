package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int errorCount;
    private List<ErrorDetails> errors;

    public ErrorResponse(ErrorDetails error){
        this.errorCount = 1;
        this.errors = List.of(error);
    }
}
