package com.reece.platform.products.model.DTO;

import java.util.UUID;
import lombok.Data;

@Data
public class CreditCardDTO {

    private String creditCardType;
    private String creditCardNumber;
    private DateWrapperDTO expirationDate;
    private String cardHolder;
    private String streetAddress;
    private String postalCode;
    private UUID elementPaymentAccountId;
}
