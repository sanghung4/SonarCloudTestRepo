package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.PaymentMethodTypeEnum;
import com.reece.platform.products.model.entity.Cart;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartResponseDTO {

    private UUID id;
    private UUID ownerId;
    private UUID approverId;
    private UUID shipToId;
    private String poNumber;
    private String pricingBranchId;
    private String shippingBranchId;
    private String paymentMethodType;
    private CreditCardDTO creditCard;
    private UUID approvalState;
    private String rejectionReason;
    private int subtotal;
    private int shippingHandling;
    private int tax;
    private int total;
    private String deliveryMethod;
    private DeliveryDTO delivery;
    private WillCallDTO willCall;
    private List<LineItemResponseDTO> products;
    private List<String> removedProducts;
    private String erpSystemName;
    private String quoteId;

    /**
     * Creates a cart from a quote
     * @param orderResponseDTO quote body
     */
    public CartResponseDTO(GetOrderResponseDTO orderResponseDTO, UUID accountId, String erpSystemName, String branchId)
        throws ParseException {
        this.id = UUID.randomUUID(); // Temp for caching on UI
        this.erpSystemName = erpSystemName;
        this.shipToId = accountId;
        this.poNumber = orderResponseDTO.getCustomerPO();
        this.shippingBranchId = branchId;
        this.paymentMethodType = PaymentMethodTypeEnum.BILLTOACCOUNT.name();
        this.subtotal = Math.round(orderResponseDTO.getSubTotal() * 100);
        this.tax = Math.round(orderResponseDTO.getTax() * 100);
        this.shippingHandling = 0;
        this.total = Math.round(orderResponseDTO.getOrderTotal() * 100);
        this.products =
            orderResponseDTO.getLineItems().stream().map(LineItemResponseDTO::new).collect(Collectors.toList());
        this.delivery = new DeliveryDTO(orderResponseDTO, accountId);
        this.willCall = new WillCallDTO(branchId);
    }

    public CartResponseDTO(Cart cart) {
        this.id = cart.getId();
        this.ownerId = cart.getOwnerId();
        this.approverId = cart.getApproverId();
        this.shipToId = cart.getShipToId();
        this.poNumber = cart.getPoNumber();
        this.pricingBranchId = cart.getPricingBranchId();
        this.shippingBranchId = cart.getShippingBranchId();
        this.creditCard = cart.getCreditCard();
        this.approvalState = cart.getApprovalState();
        this.rejectionReason = cart.getRejectionReason();
        this.subtotal = cart.getSubtotal();
        this.shippingHandling = cart.getShippingHandling();
        this.tax = cart.getTax();
        this.total = cart.getTotal();

        if (cart.getErpSystemName() != null) this.erpSystemName = cart.getErpSystemName().name();

        if (cart.getPaymentMethodType() != null) this.paymentMethodType = cart.getPaymentMethodType().toString();

        if (cart.getDeliveryMethod() != null) this.deliveryMethod = cart.getDeliveryMethod().toString();

        if (cart.getDelivery() != null) this.delivery = new DeliveryDTO(cart.getDelivery());

        if (cart.getWillCall() != null) this.willCall = new WillCallDTO(cart.getWillCall());
    }
}
