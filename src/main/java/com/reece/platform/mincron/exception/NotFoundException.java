package com.reece.platform.mincron.exception;

import com.reece.platform.mincron.dto.kerridge.MincronErrorDTO;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String code;

    public NotFoundException(MincronErrorDTO error) {
        super(error.getError().getMessage());
        this.code = error.getError().getCode();
    }
}
