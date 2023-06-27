package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBlock {

    private boolean isSuccess;
    private Error error;

    public ErrorBlock(boolean isSuccess) {
        this.isSuccess=isSuccess;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Error {

        private String code;
        private String message;
        private String details;

    }
}




