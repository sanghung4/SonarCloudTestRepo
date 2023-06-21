package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class EntityUpdateSubmitResponseDTO {
    private StatusResult statusResult;
    private CreditCardList creditCardList;
}
