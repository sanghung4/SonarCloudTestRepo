package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.EntityResponse.CreditCardList;
import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreditCardListResponseDTO {

    public CreditCardListResponseDTO(EntityResponse entityResponse) {
        this.creditCardList = entityResponse.getEntityInquiryResponse().getEntity().getCreditCardList();
    }
    private CreditCardList creditCardList;
}
