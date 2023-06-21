package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.PreferredTimeEnum;
import com.reece.platform.products.model.eclipse.common.EclipseAddressRequestDTO;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class CreateSalesOrderRequestDTO {

    private String billToEntityId;

    private String shipToEntityId;

    private String eclipseLoginId;

    private String eclipsePassword;

    private String phoneNumber;

    private String orderedBy;

    private String instructions;

    private EclipseAddressRequestDTO address;

    private CreditCardDTO creditCard;

    private List<LineItemDTO> lineItems;

    private Date preferredDate;

    private PreferredTimeEnum preferredTime;

    private Boolean isDelivery;

    private Boolean shouldShipFullOrder;

    private String poNumber;

    private String branchId;
}
