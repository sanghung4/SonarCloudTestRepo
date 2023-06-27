package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private String field;
    private String errorMessage;
    private String exceptionType;
}
