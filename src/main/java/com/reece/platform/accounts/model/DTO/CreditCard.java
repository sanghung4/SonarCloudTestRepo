package com.reece.platform.accounts.model.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CreditCard {
    private String creditCardType;
    private String creditCardNumber;
    private DateWrapper expirationDate;
    private String cardHolder;
    private String streetAddress;
    private String postalCode;
    private UUID elementPaymentAccountId;
}
