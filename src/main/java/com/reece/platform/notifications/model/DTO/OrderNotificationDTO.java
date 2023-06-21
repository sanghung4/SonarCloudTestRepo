package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderNotificationDTO {
    private String email;
    private String name;
    private String orderNumber;
    private String poNumber;
    private String shippingMethod;
    private String orderDate;
    private String brand;
    private String domain;
    private BaseAddressDTO address;
    private List<ProductDTO> productDTOs;
    private Float subTotal;
    private Float total;
    private Float tax;
    private BranchAddressDTO branchDTO;
    private String emailSubjectLine;
}
