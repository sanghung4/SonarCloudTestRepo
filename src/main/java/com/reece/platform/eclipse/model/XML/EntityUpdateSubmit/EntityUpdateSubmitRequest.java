package com.reece.platform.eclipse.model.XML.EntityUpdateSubmit;

import com.reece.platform.eclipse.model.DTO.CreditCardDTO;
import com.reece.platform.eclipse.model.DTO.EntityUpdateSubmitRequestDTO;
import com.reece.platform.eclipse.model.XML.EntityResponse.CreditCardList;
import com.reece.platform.eclipse.model.XML.EntityResponse.Entity;
import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "EntityUpdateSubmitRequestWrapper" })
public class EntityUpdateSubmitRequest {

    public EntityUpdateSubmitRequest(String accountId, Security security, EntityResponse entityResponse) {
        EntityUpdateSubmitRequestWrapper entityUpdateSubmitRequestWrapper = new EntityUpdateSubmitRequestWrapper();

        Entity entity = new Entity();
        entity.setEntityID(accountId);
        entity.setEntityName(entityResponse.getEntityInquiryResponse().getEntity().getEntityName());
        entity.setAddress(entityResponse.getEntityInquiryResponse().getEntity().getAddress());
        entity.setCreditCardList(entityResponse.getEntityInquiryResponse().getEntity().getCreditCardList());
        entityUpdateSubmitRequestWrapper.setSecurity(security);
        entityUpdateSubmitRequestWrapper.setEntity(entity);
        this.EntityUpdateSubmitRequestWrapper = entityUpdateSubmitRequestWrapper;
    }

    public EntityUpdateSubmitRequest(String accountId, Security security, EntityUpdateSubmitRequestDTO updateSubmitRequestDTO, EntityResponse entityResponse) {
        EntityUpdateSubmitRequestWrapper entityUpdateSubmitRequestWrapper = new EntityUpdateSubmitRequestWrapper();

        CreditCardDTO creditCard = updateSubmitRequestDTO.getCreditCard();
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setCreditCardType(creditCard.getCreditCardType());
        newCreditCard.setCreditCardNumber(creditCard.getCreditCardNumber());
        newCreditCard.setExpirationDate(creditCard.getExpirationDate());
        newCreditCard.setCardHolder(creditCard.getCardHolder());
        newCreditCard.setStreetAddress(creditCard.getStreetAddress());
        newCreditCard.setPostalCode(creditCard.getPostalCode());
        newCreditCard.setElementPaymentAccountId(creditCard.getElementPaymentAccountId());

        CreditCardList creditCardList = entityResponse.getEntityInquiryResponse().getEntity().getCreditCardList();
        List<CreditCard> creditCardListList;
        if (creditCardList != null && creditCardList.getCreditCard() != null) {
            creditCardListList = creditCardList.getCreditCard();
        } else {
            creditCardListList = new ArrayList<>();
        }
        creditCardListList.add(newCreditCard);

        CreditCardList newCreditCardList = new CreditCardList();
        newCreditCardList.setCreditCard(creditCardListList);

        Entity entity = new Entity();
        entity.setEntityID(accountId);
        entity.setEntityName(entityResponse.getEntityInquiryResponse().getEntity().getEntityName());
        entity.setAddress(entityResponse.getEntityInquiryResponse().getEntity().getAddress());
        entity.setCreditCardList(newCreditCardList);
        entityUpdateSubmitRequestWrapper.setSecurity(security);
        entityUpdateSubmitRequestWrapper.setEntity(entity);
        this.EntityUpdateSubmitRequestWrapper = entityUpdateSubmitRequestWrapper;
    }

    @XmlElement(name = "EntityUpdateSubmit")
    private EntityUpdateSubmitRequestWrapper EntityUpdateSubmitRequestWrapper;
}
