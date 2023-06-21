package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.common.Address;
import com.reece.platform.eclipse.model.enums.PreferredTimeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SalesOrderDTO {
    private String billToEntityId;

    private String shipToEntityId;

    private String eclipseLoginId;

    private String eclipsePassword;

    private String phoneNumber;

    private String instructions;

    private Address address;

    private CreditCardDTO creditCard;

    private List<LineItemDTO> lineItems;

    private Date preferredDate;

    private PreferredTimeEnum preferredTime;

    private Boolean isDelivery;

    private Boolean shouldShipFullOrder;

    private String orderedBy;

    private String poNumber;

    private String branchId;
}
