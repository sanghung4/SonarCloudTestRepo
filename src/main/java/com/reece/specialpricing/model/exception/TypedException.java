package com.reece.specialpricing.model.exception;

public class TypedException extends Exception{
    private String errorField;
    public TypedException(String message){
        super(message);
    }

    public TypedException(String message, String errorField){
        super(message);
        this.errorField = errorField;
    }

    public String getExceptionType(){
        return "Typed";
    }

    public String getErrorField(){
        return errorField;
    }
}
