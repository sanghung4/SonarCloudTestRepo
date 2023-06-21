package com.reece.platform.accounts.model.DTO;

import lombok.Data;

@Data
public class EntityDTO {
    private String entityID;
    private String entityName;
    private AddressDTO address;
    private CreditCardList creditCardList;
}
