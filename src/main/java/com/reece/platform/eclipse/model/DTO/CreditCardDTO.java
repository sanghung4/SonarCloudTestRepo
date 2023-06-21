package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.common.DateWrapper;

import lombok.Data;

import java.util.UUID;

@Data
public class CreditCardDTO {
    private String creditCardType;
    private String creditCardNumber;
    private DateWrapper expirationDate;
    private String cardHolder;
    private String streetAddress;
    private String postalCode;
    private UUID elementPaymentAccountId;
}
