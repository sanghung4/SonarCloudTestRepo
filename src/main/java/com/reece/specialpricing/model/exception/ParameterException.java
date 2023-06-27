package com.reece.specialpricing.model.exception;

public class ParameterException extends TypedException {
    public ParameterException(String message, String errorField){
        super(message, errorField);
    }

    @Override
    public String getExceptionType(){
        return "InvalidParameter";
    }
}
