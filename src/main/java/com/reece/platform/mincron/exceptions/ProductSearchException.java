package com.reece.platform.mincron.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ProductSearchException extends MincronException {

    public ProductSearchException(String errorCode) {
        this(ErrorCode.fromCode(errorCode));
    }

    ProductSearchException(ErrorCode errorCode) {
        super(errorCode.description, HttpStatus.BAD_REQUEST, errorCode.code);
    }

    @RequiredArgsConstructor
    enum ErrorCode {
        NO_ITEMS_FOUND("1", "No items found matching search criteria"),
        UNKNOWN("unknown", "Unknown Error");

        private final String code;
        private final String description;

        static ErrorCode fromCode(String code) {
            return Arrays.stream(ErrorCode.values())
                    .filter(errorCode -> errorCode.code.equals(code))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }
}
