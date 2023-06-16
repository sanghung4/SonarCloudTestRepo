package com.reece.platform.mincron.exception;

import com.reece.platform.mincron.dto.kerridge.MincronErrorDTO;
import lombok.Getter;

@Getter
public class MincronException extends RuntimeException {
    private final String code;

    public MincronException(MincronErrorDTO error) {
        super(error.getError().getMessage());
        this.code = error.getError().getCode();
    }
}
