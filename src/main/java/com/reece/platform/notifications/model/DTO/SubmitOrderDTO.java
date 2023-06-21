package com.reece.platform.notifications.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class SubmitOrderDTO {
    private List<String> emails;
    private String name;
    private String orderNumber;
    private String pendingApprovalOrderNumber;
    private String poNumber;
    private String shippingMethod;
    private String orderDate;
    private String brand;
    private String domain;
    private BaseAddressDTO address;
    private List<ProductDTO> productDTOs;
    private String subTotal;
    private String total;
    private String tax;
    private BranchAddressDTO branchDTO;
    private String rejectionReason;
    private String webStatus;

    private String contractName;
    private String erpSystemName;
    private String contractNumber;
}
