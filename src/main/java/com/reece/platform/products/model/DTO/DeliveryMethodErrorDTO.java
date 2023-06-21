package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.entity.Cart;
import lombok.Data;

@Data
public class DeliveryMethodErrorDTO {

    private String billToEntityId;
    private String shipToEntityId;
    private String orderedBy;
    private Boolean isDelivery;
    private String branchId;
    private String userEmail;

    public DeliveryMethodErrorDTO(CreateSalesOrderRequestDTO createSalesOrderRequestDTO, Cart cart, String userEmail) {
        this.billToEntityId = createSalesOrderRequestDTO.getBillToEntityId();
        this.shipToEntityId = createSalesOrderRequestDTO.getShipToEntityId();
        this.orderedBy = createSalesOrderRequestDTO.getOrderedBy();
        this.isDelivery = createSalesOrderRequestDTO.getIsDelivery();
        this.branchId = cart.getShippingBranchId();
        this.userEmail = userEmail;
    }
}
