package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.EntityResponse.CreditCardList;
import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import com.reece.platform.eclipse.model.XML.EntityUpdateSubmit.EntityUpdateSubmitResponseWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EntityUpdateSubmitResponseDTO {

    private StatusResult statusResult;
    private CreditCardList creditCardList;

    public EntityUpdateSubmitResponseDTO(EntityUpdateSubmitResponseWrapper entityUpdateSubmitResponseWrapper, EntityResponse updatedEntityResponse) {
        this.statusResult = entityUpdateSubmitResponseWrapper.getEntityUpdateSubmitResponse().getStatusResult();
        this.creditCardList = updatedEntityResponse.getEntityInquiryResponse().getEntity().getCreditCardList();
    }
}
