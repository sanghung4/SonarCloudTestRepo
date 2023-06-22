package com.reece.platform.mincron.model.contracts;

import com.reece.platform.mincron.model.AddressDTO;
import lombok.Data;

@Data
public class SubmitOrderWrapperDTO {

    private AddressDTO shipToAddress;
    private ContractItemListWrapperDTO itemList;
    private String orderType;
    private String gstHstUseCode;
    private String promiseDate;
    private String orderNumber;
    //check type
    private String[] orderComments;
    private String subTotalWithBackOrder;
    private String trackingURL;
    private String otherCharges;
    private String creditCardMessage;
    private String orderBy;
    private String subTotal;
    private String gstHstAmount;
    private String orderDescription;
    private String enteredBy;
    private String shipmentMethod;
    private String paidStatus;
    private String shipVia;
    private String allowCreditCardFlag;
    private String merchantId;
    //check type
    private String[] creditCardTypes;
    private String invoiceNumber;
    private String freightCode;
    private String trackingNumber;
    private String jobName;
    private String taxable;
    private String branchNumber;
    private String editable;
    private String contractNumber;
    private String branchName;
    private String shipDate;
    private String shipHandleAmount;
    private String paidByCC;
    private String customerName;
    private String totalAmount;
    private String phoneNumber;
    private String creditCardAuthAmount;
    private String[] specialInstructions;
    private String purchaseOrderNumber;
    private String shipmentMethodSysConfig;
    private String orderQuoteCreditDebitCode;
    private String taxAmount;
    private String orderDate;
    private String jobNumber;
    private String status;
    private String shoppingCartId;
}
