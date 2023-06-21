package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.LineItems;
import com.reece.platform.products.model.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesOrderSubmitNotificationDTO {

    public SalesOrderSubmitNotificationDTO(
        GetOrderResponseDTO getOrderResponseDTO,
        boolean isDelivery,
        BranchDTO branchDTO,
        String userFirstName,
        List<String> emailAddresses
    ) {
        this.emails = emailAddresses;
        this.name = userFirstName;
        this.orderNumber = getOrderResponseDTO.getOrderNumber();
        this.poNumber = getOrderResponseDTO.getCustomerPO();
        this.shippingMethod = getOrderResponseDTO.getDeliveryMethod();
        this.address =
            isDelivery
                ? getOrderResponseDTO.getShipAddress()
                : new EclipseAddressResponseDTO(getOrderResponseDTO.getBranchInfo());
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
        this.webStatus = getOrderResponseDTO.getWebStatus();
        this.brand = branchDTO.getBrand();
        this.domain = branchDTO.getDomain();
    }

    public SalesOrderSubmitNotificationDTO(
        SubmitOrderRequestDTO submitOrderRequest,
        boolean isDelivery,
        BranchDTO branchDTO,
        String userFirstName,
        List<String> emailAddresses
    ) {
        this.emails = emailAddresses;
        this.name = userFirstName;
        this.orderNumber = submitOrderRequest.getOrderNumber();
        this.poNumber = submitOrderRequest.getPoNumber();
        this.shippingMethod = submitOrderRequest.getDeliveryMethod();
        this.address =
            isDelivery
                ? new EclipseAddressResponseDTO(submitOrderRequest.getShipToAddress())
                : new EclipseAddressResponseDTO(submitOrderRequest.getBranchInfo());
        this.productDTOs =
            submitOrderRequest
                .getLineItems()
                .stream()
                .map(OrderSubmitEmailProductDTO::new)
                .collect(Collectors.toList());
        this.subTotal = Double.parseDouble(submitOrderRequest.getSubTotal());
        this.tax = Double.parseDouble(submitOrderRequest.getTaxAmount());
        this.total = Double.parseDouble(submitOrderRequest.getOrderTotal());
        this.branchDTO = new OrderSubmitEmailBranchDTO(submitOrderRequest.getBranchInfo());
        this.orderDate = submitOrderRequest.getOrderDate();
        this.webStatus = submitOrderRequest.getWebStatus();
        this.brand = branchDTO.getBrand();
        this.domain = branchDTO.getDomain();
        this.contractName = submitOrderRequest.getContractName();
        this.erpSystemName = submitOrderRequest.getErpSystemName();
        this.contractNumber = submitOrderRequest.getContractNumber();
    }

    private List<String> emails;
    private String name;
    private String orderNumber;
    private String pendingApprovalOrderNumber;
    private String orderDate;
    private String poNumber;
    private String shippingMethod;
    private String brand;
    private String domain;
    private EclipseAddressResponseDTO address;
    private List<OrderSubmitEmailProductDTO> productDTOs;
    private double subTotal;
    private double total;
    private double tax;
    private OrderSubmitEmailBranchDTO branchDTO;
    private String rejectionReason;
    private String webStatus;
    private String contractName;
    private String erpSystemName;
    private String contractNumber;
}
