package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class SubmitOrderRequestDTO {

    private String application;
    private String accountId;
    private String userId;
    private String shoppingCartId;
    private String shipBranchNumber;
    private String promiseDate;
    private String shipMethod;
    private String shipCode;
    private String shipDescription;
    private String phoneNumber;
    private String jobNumber;
    private String jobName;
    private String subTotal;
    private String taxAmount;
    private String shipHandleAmount;
    private String orderTotal;
    private String spInstructions;
    private String orderComments;
    private String poNumber;
    private AddressDTO shipToAddress;

    private BranchOrderInfoDTO branchInfo;
    private List<OrderLineItemResponseDTO> lineItems;
    private String contractName;
    private String erpSystemName;
    private String contractNumber;
    private String orderNumber;
    private String webStatus;
    private String orderDate;
    private String deliveryMethod;
    private UUID shipToId;
    private String authorization;
}
