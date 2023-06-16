package com.reece.platform.eclipse.exceptions;

import com.reece.platform.eclipse.dto.inventory.KourierErrorDTO;
import lombok.Getter;

@Getter
public class KourierInventoryException extends RuntimeException {

    private final String code;

    public KourierInventoryException(KourierErrorDTO error) {
        super(error.getErrorMessage());
        this.code = error.getErrorCode();
    }
}
