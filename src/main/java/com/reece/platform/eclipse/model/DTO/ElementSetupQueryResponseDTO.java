package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.ElementAccount.ElementSetupQueryResponseWrapper;
import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import com.reece.platform.eclipse.model.XML.ElementAccount.StatusResult;
import lombok.Data;

@Data
public class ElementSetupQueryResponseDTO {
    private CreditCard creditCard;
    private StatusResult statusResult;

    public ElementSetupQueryResponseDTO(ElementSetupQueryResponseWrapper setupQueryResponseWrapper) {
        this.creditCard = setupQueryResponseWrapper.getElementSetupQueryResponse().getCreditCard();
        this.statusResult = setupQueryResponseWrapper.getElementSetupQueryResponse().getStatusResult();
    }
}
