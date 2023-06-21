package com.reece.platform.products.model.DTO;

import lombok.Data;

@Data
public class CreditCardResponseDTO {

    private CreditCardDTO creditCard;
    private StatusResultDTO statusResult;
}
