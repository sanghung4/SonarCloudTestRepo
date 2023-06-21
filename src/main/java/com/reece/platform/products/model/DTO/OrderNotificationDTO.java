package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class OrderNotificationDTO {

    private String email;
    private String emailSubjectLine;
    private String name;
    private String orderNumber;
    private String orderDate;
    private String poNumber;
    private String shippingMethod;
    private String brand;
    private String domain;
    private EclipseAddressResponseDTO address;
    private List<OrderSubmitEmailProductDTO> productDTOs;
    private Float subTotal;
    private Float total;
    private Float tax;
    private OrderSubmitEmailBranchDTO branchDTO;

    public OrderNotificationDTO(GetOrderResponseDTO getOrderResponseDTO) {
        this.email = getOrderResponseDTO.getEmail();
        this.name = getOrderResponseDTO.getOrderedBy();
        this.orderNumber = getOrderResponseDTO.getOrderNumber();
        this.poNumber = getOrderResponseDTO.getCustomerPO();
        this.shippingMethod = getOrderResponseDTO.getDeliveryMethod();
        this.productDTOs =
            getOrderResponseDTO
                .getLineItems()
                .stream()
                .map(OrderSubmitEmailProductDTO::new)
                .collect(Collectors.toList());
        this.subTotal = getOrderResponseDTO.getSubTotal();
        this.tax = getOrderResponseDTO.getTax();
        this.total = getOrderResponseDTO.getOrderTotal();
        this.branchDTO = new OrderSubmitEmailBranchDTO(getOrderResponseDTO.getBranchInfo());
        this.orderDate = getOrderResponseDTO.getOrderDate();
        this.emailSubjectLine =
            "Order #" + getOrderResponseDTO.getOrderNumber() + " update: " + getOrderResponseDTO.getWebStatus();
    }
}
