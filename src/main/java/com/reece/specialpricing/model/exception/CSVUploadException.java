package com.reece.specialpricing.model.exception;

public class CSVUploadException extends TypedException{
    public CSVUploadException(String message){
        super(message);
    }

    @Override
    public String getExceptionType(){
        return "CSVUpload";
    }
}
