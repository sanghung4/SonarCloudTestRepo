package com.reece.platform.accounts.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CreditCardList {
    private List<CreditCard> creditCard;
}
