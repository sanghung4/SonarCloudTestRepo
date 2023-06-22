package com.reece.platform.mincron.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class GetLocationException extends MincronException {

    public GetLocationException(String errorCode) {
        this(ErrorCode.fromCode(errorCode));
    }

    GetLocationException(ErrorCode errorCode) {
        super(errorCode.description, HttpStatus.BAD_REQUEST, errorCode.code);
    }

    @RequiredArgsConstructor
    enum ErrorCode {
        INVALID_BRANCH_COUNT_ID("1", "Invalid branch/count id combination"),
        INVALID_LOCATION("2", "Invalid location"),
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
