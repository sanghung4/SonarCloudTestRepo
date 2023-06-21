package com.reece.platform.eclipse.model.DTO;


import com.reece.platform.eclipse.model.XML.EntityResponse.CreditCardList;
import com.reece.platform.eclipse.model.XML.common.Address;
import lombok.Data;

@Data
public class EntityDTO {
    private String entityID;
    private String entityName;
    private Address address;
    private CreditCardList creditCardList;
}
