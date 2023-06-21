package com.reece.platform.accounts.model.DTO;

import lombok.Data;

/**
 * Calling to eclipse end point for credit card setup url
 */
@Data
public class ElementSetupUrlDTO {
    private String cardHolder;
    private String streetAddress;
    private String postalCode;
    private String returnUrl;
}
