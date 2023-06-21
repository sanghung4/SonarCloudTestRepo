package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.AccountHistoryResponse.AccountHistoryItem;
import com.reece.platform.eclipse.model.XML.OpenOrderResponse.*;
import com.reece.platform.eclipse.model.XML.SalesOrder.CreditCard;
import com.reece.platform.eclipse.model.XML.SalesOrder.OrderHeader;
import com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit.SalesOrderSubmitPreviewResponseWrapper;
import com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit.SalesOrderSubmitResponseWrapper;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.LineItem;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrder;
import com.reece.platform.eclipse.model.XML.common.Address;
import com.reece.platform.eclipse.model.XML.common.OrderTotals;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class GetOrderResponseDTO {
    private String orderNumber;
    private String orderStatus;
    private String shipDate;
    private String customerPO;
    private String orderDate;
    private String amount;
    private String invoiceNumber;
    private String invoiceDueDate;
    private String billToName;
    private String shipToName;
    private String orderedBy;
    private String printStatus;
    private Address shipAddress;
    private String deliveryMethod;
    private String specialInstructions;
    private String email;
    private CreditCard creditCard;
    private BranchOrderInfoDTO branchInfo;
    private WebStatuses webStatus;
    private float subTotal;
    private float tax;
    private float orderTotal;
    private String bidExpireDate;
    private String requiredDate;
    private String shipToId;
    private String quoteStatus;
    private List<LineItemResponseDTO> lineItems;

    public GetOrderResponseDTO(SalesOrderSubmitResponseWrapper salesOrderSubmitResponseWrapper) {
        OrderHeader orderHeader = salesOrderSubmitResponseWrapper.getSalesOrderSubmitResponse().getSalesOrder().getOrderHeader();
        this.orderedBy = orderHeader.getOrderedBy().getDescription();
        this.orderNumber = orderHeader.getOrderId();
        this.orderStatus = orderHeader.getOrderStatus().getCode();

        if (orderHeader.getShipDate() != null) {
            this.shipDate = orderHeader.getShipDate().getDate();
        }
        this.customerPO = orderHeader.getCustomerPO();

        if (orderHeader.getOrderDate() != null) {
            this.orderDate = orderHeader.getOrderDate().getDate();
        }

        if (orderHeader.getCreditCard() != null) {
            this.creditCard = orderHeader.getCreditCard();
        }

        this.invoiceNumber = orderHeader.getInvoiceNumber();

        OrderTotals orderTotals = salesOrderSubmitResponseWrapper.getSalesOrderSubmitResponse().getSalesOrder().getOrderTotals();
        this.amount = String.valueOf(orderTotals.getTotal());
        this.subTotal = orderTotals.getSubtotal();
        this.tax = orderTotals.getTax();
        this.orderTotal = orderTotals.getTotal();
        this.email = orderHeader.getEmailAddress();
        this.branchInfo = new BranchOrderInfoDTO(orderHeader.getShippingBranch().getBranch());
        this.lineItems = salesOrderSubmitResponseWrapper.getSalesOrderSubmitResponse().getSalesOrder().getLineItemList().getLineItems().stream().map(LineItemResponseDTO::new).collect(Collectors.toList());
        this.deliveryMethod = WebShipViaMapping.map.get(salesOrderSubmitResponseWrapper.getSalesOrderSubmitResponse().getSalesOrder().getOrderHeader().getShippingInformation().getShipVia().getDescription());
        this.shipAddress = new Address(orderHeader.getShippingInformation().getAddress());
        this.specialInstructions = orderHeader.getShippingInformation().getInstructions();
        this.billToName = orderHeader.getBillTo() == null ? "" : orderHeader.getBillTo().getEntity().getEntityName();
        this.shipToName = orderHeader.getShipTo() == null ? "" : orderHeader.getShipTo().getEntity().getEntityName();
        this.shipToId = orderHeader.getShipTo() == null ? "" : orderHeader.getShipTo().getEntity().getEntityID();
        this.quoteStatus = orderHeader.getQuoteStatus() == null ? "" : orderHeader.getQuoteStatus();
        this.setWebStatus(orderHeader.getOrderStatus().getCode(), orderHeader.getShippingInformation().getShipVia().getShipViaID(), orderHeader.getPrintStatus());
    }

    public GetOrderResponseDTO(SalesOrderSubmitPreviewResponseWrapper salesOrderSubmitPreviewResponseWrapper) {
        OrderHeader orderHeader = salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getOrderHeader();
        this.orderedBy = orderHeader.getOrderedBy().getDescription();
        this.orderStatus = orderHeader.getOrderStatus().getCode();

        if (orderHeader.getShipDate() != null) {
            this.shipDate = orderHeader.getShipDate().getDate();
        }
        this.customerPO = orderHeader.getCustomerPO();

        this.billToName = orderHeader.getBillTo() == null ? "" : orderHeader.getBillTo().getEntity().getEntityName();
        this.shipToName = orderHeader.getShipTo() == null ? "" : orderHeader.getShipTo().getEntity().getEntityName();
        if (orderHeader.getShippingInformation() != null && orderHeader.getShippingInformation().getAddress() != null) {
            this.shipAddress = new Address(orderHeader.getShippingInformation().getAddress());
        }
        if (orderHeader.getShippingBranch() != null) {
            this.branchInfo = new BranchOrderInfoDTO(orderHeader.getShippingBranch().getBranch());
        }

        this.deliveryMethod = WebShipViaMapping.map.get(salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getOrderHeader().getShippingInformation().getShipVia().getDescription());
        this.specialInstructions = orderHeader.getShippingInformation().getInstructions();

        if (orderHeader.getOrderDate() != null) {
            this.orderDate = orderHeader.getOrderDate().getDate();
        }
        this.lineItems = salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getLineItemList().getLineItems().stream().map(LineItemResponseDTO::new).collect(Collectors.toList());
        this.tax = salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getOrderTotals().getTax();
        this.subTotal = salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getOrderTotals().getSubtotal();
        this.orderTotal = salesOrderSubmitPreviewResponseWrapper.getSalesOrderSubmitPreviewResponse().getSalesOrder().getOrderTotals().getTotal();
    }

    public GetOrderResponseDTO(AccountHistoryItem accountHistoryItem) {

        if (accountHistoryItem != null) {

            this.setOrderNumber(accountHistoryItem.getOrderID());
            this.setAmount(accountHistoryItem.getAmount());
            this.setCustomerPO(accountHistoryItem.getCustomerPO());
            this.setPrintStatus(accountHistoryItem.getPrintStatus());

            if (accountHistoryItem.getShippingInformation() != null && accountHistoryItem.getShippingInformation().getShipVia() != null) {
                this.setDeliveryMethod(WebShipViaMapping.map.get(accountHistoryItem.getShippingInformation().getShipVia().getDescription()));
            }

            if (accountHistoryItem.getOrderStatus() != null) {
                orderStatus = accountHistoryItem.getOrderStatus().getCode();
                this.setOrderStatus(orderStatus);
            }

            if (accountHistoryItem.getShipDate() != null) {
                shipDate = accountHistoryItem.getShipDate().getDate();
                this.setShipDate(shipDate);
            }

            if (accountHistoryItem.getOrderDate() != null) {
                orderDate = accountHistoryItem.getOrderDate().getDate();
                this.setOrderDate(orderDate);
            }

            if (accountHistoryItem.getInvoiceNumber() != null) {
                invoiceNumber = accountHistoryItem.getInvoiceNumber();
                this.setInvoiceNumber(invoiceNumber);
            }

            this.setWebStatus(this.orderStatus, accountHistoryItem.getShippingInformation().getShipVia().getDescription(), accountHistoryItem.getPrintStatus());
        }
    }

    public GetOrderResponseDTO(OpenOrderItem openOrderItem) {

        if (openOrderItem != null) {

            this.setOrderNumber(openOrderItem.getOrderID());
            this.setAmount(openOrderItem.getAmount());
            this.setCustomerPO(openOrderItem.getCustomerPO());
            this.setPrintStatus(openOrderItem.getPrintStatus());

            if (openOrderItem.getShippingInformation() != null && openOrderItem.getShippingInformation().getShipVia() != null) {
                this.setDeliveryMethod(WebShipViaMapping.map.get(openOrderItem.getShippingInformation().getShipVia().getDescription()));
            }

            if (openOrderItem.getOrderStatus() != null) {
                orderStatus = openOrderItem.getOrderStatus().getCode();
                this.setOrderStatus(orderStatus);
            }

            if (openOrderItem.getShipDate() != null) {
                shipDate = openOrderItem.getShipDate().getDate();
                this.setShipDate(shipDate);
            }

            if (openOrderItem.getOrderDate() != null) {
                orderDate = openOrderItem.getOrderDate().getDate();
                this.setOrderDate(orderDate);
            }

            if (openOrderItem.getInvoiceNumber() != null) {
                invoiceNumber = openOrderItem.getInvoiceNumber();
                this.setInvoiceNumber(invoiceNumber);
            }

            if (openOrderItem.getBidExpireDate() != null) {
                this.setBidExpireDate(openOrderItem.getBidExpireDate().getDate());
            }

            this.setWebStatus(this.orderStatus, openOrderItem.getShippingInformation().getShipVia().getDescription(), openOrderItem.getPrintStatus());
        }
    }

    public GetOrderResponseDTO(com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrderResponse salesOrderResponse) {

        if (salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder() != null) {

            OrderHeader orderHeader = salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder().getOrderHeader();

            this.setOrderNumber(orderHeader.getOrderId());
            this.setCustomerPO(orderHeader.getCustomerPO());
            this.setPrintStatus(orderHeader.getPrintStatus());

            if (orderHeader.getShippingInformation() != null) {
                this.setSpecialInstructions(orderHeader.getShippingInformation().getInstructions());

                if (orderHeader.getShippingInformation().getShipVia() != null) {
                    this.setDeliveryMethod(WebShipViaMapping.map.get(orderHeader.getShippingInformation().getShipVia().getDescription()));
                }
            }

            if (orderHeader.getOrderedBy() != null) {
                this.setOrderedBy(orderHeader.getOrderedBy().getDescription());
            }

            if (orderHeader.getOrderDate() != null) {
                this.setOrderDate(orderHeader.getOrderDate().getDate());
            }

            if (orderHeader.getCreditCard() != null) {
                this.setCreditCard(orderHeader.getCreditCard());
            }

            if(orderHeader.getBidExpireDate()!=null) {
                this.setBidExpireDate(orderHeader.getBidExpireDate().getDate());
            }

            if(orderHeader.getRequiredDate()!=null) {
                this.setRequiredDate(orderHeader.getRequiredDate().getDate());
            }

            if (orderHeader.getShippingInformation() != null) {
                this.setShipAddress(orderHeader.getShippingInformation().getAddress());
            }

            if (orderHeader.getShipDate() != null) {
                this.setShipDate(orderHeader.getShipDate().getDate());
            }

            if (orderHeader.getOrderStatus() != null) {
                this.setOrderStatus(orderHeader.getOrderStatus().getCode());
                this.setWebStatus(this.orderStatus, orderHeader.getShippingInformation().getShipVia().getDescription(), orderHeader.getPrintStatus());
            }

            if (orderHeader.getQuoteStatus() != null) {
                this.setQuoteStatus(orderHeader.getQuoteStatus());
            }

            if (orderHeader.getBillTo() != null && orderHeader.getBillTo().getEntity() != null) {
                this.setBillToName(orderHeader.getBillTo().getEntity().getEntityName());
            }

            if (orderHeader.getShipTo() != null && orderHeader.getShipTo().getEntity() != null) {
                this.setShipToName(orderHeader.getShipTo().getEntity().getEntityName());
                this.setShipToId(orderHeader.getShipTo().getEntity().getEntityID());
            }

            OrderTotals orderTotals = salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder().getOrderTotals();
            this.setSubTotal(orderTotals.getSubtotal());
            this.setOrderTotal(orderTotals.getSubtotal() + orderTotals.getTax()
                    + orderTotals.getFederalExciseTax() + orderTotals.getFreight() + orderTotals.getHandling());
            this.setTax(orderTotals.getTax() + orderTotals.getFederalExciseTax());
            List<LineItemResponseDTO> lineItems = new ArrayList<>();

            if (salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder().getLineItemList() != null
                    && salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder().getLineItemList().getLineItems() != null) {
                for (LineItem lineItem : salesOrderResponse.getSalesOrderInquiryResponse().getSalesOrder().getLineItemList().getLineItems()) {
                    LineItemResponseDTO lineItemResponseDTO = new LineItemResponseDTO(lineItem);
                    lineItems.add(lineItemResponseDTO);
                }
            }
            this.email = orderHeader.getEmailAddress();
            this.branchInfo = new BranchOrderInfoDTO(orderHeader.getShippingBranch().getBranch());
            this.setLineItems(lineItems);

            if (orderHeader.getInvoiceDueDate() != null) {
                this.invoiceDueDate = orderHeader.getInvoiceDueDate().getDate();
            }
        }
    }

    public GetOrderResponseDTO(SalesOrder salesOrder) {
        OrderHeader orderHeader = salesOrder.getOrderHeader();

        this.setOrderNumber(orderHeader.getOrderId());
        this.setCustomerPO(orderHeader.getCustomerPO());
        this.setPrintStatus(orderHeader.getPrintStatus());

        if (orderHeader.getShippingInformation() != null) {
            this.setSpecialInstructions(orderHeader.getShippingInformation().getInstructions());

            if (orderHeader.getShippingInformation().getShipVia() != null) {
                this.setDeliveryMethod(WebShipViaMapping.map.get(orderHeader.getShippingInformation().getShipVia().getDescription()));
            }
        }

        if (orderHeader.getOrderedBy() != null) {
            this.setOrderedBy(orderHeader.getOrderedBy().getDescription());
        }

        if (orderHeader.getOrderDate() != null) {
            this.setOrderDate(orderHeader.getOrderDate().getDate());
        }

        if (orderHeader.getCreditCard() != null) {
            this.setCreditCard(orderHeader.getCreditCard());
        }

        if (orderHeader.getShippingInformation() != null) {
            this.setShipAddress(orderHeader.getShippingInformation().getAddress());
        }

        if (orderHeader.getShipDate() != null) {
            this.setShipDate(orderHeader.getShipDate().getDate());
        }

        if (orderHeader.getOrderStatus() != null) {
            this.setOrderStatus(orderHeader.getOrderStatus().getCode());
            this.setWebStatus(this.orderStatus, orderHeader.getShippingInformation().getShipVia().getDescription(), orderHeader.getPrintStatus());
        }

        if (orderHeader.getBillTo() != null && orderHeader.getBillTo().getEntity() != null) {
            this.setBillToName(orderHeader.getBillTo().getEntity().getEntityName());
        }

        if (orderHeader.getShipTo() != null && orderHeader.getShipTo().getEntity() != null) {
            this.setShipToName(orderHeader.getShipTo().getEntity().getEntityName());
        }

        if(orderHeader.getBidExpireDate()!=null) {
            this.setBidExpireDate(orderHeader.getBidExpireDate().getDate());
        }

        if(orderHeader.getRequiredDate()!=null) {
            this.setRequiredDate(orderHeader.getRequiredDate().getDate());
        }

        if (orderHeader.getShipTo() != null && orderHeader.getShipTo().getEntity() != null) {
            this.setShipToId(orderHeader.getShipTo().getEntity().getEntityID());
        }

        OrderTotals orderTotals = salesOrder.getOrderTotals();
        this.setSubTotal(orderTotals.getSubtotal());
        this.setOrderTotal(orderTotals.getSubtotal() + orderTotals.getTax()
                + orderTotals.getFederalExciseTax() + orderTotals.getFreight() + orderTotals.getHandling());
        this.setTax(orderTotals.getTax() + orderTotals.getFederalExciseTax());
        List<LineItemResponseDTO> lineItems = new ArrayList<>();

        if (salesOrder.getLineItemList() != null
                && salesOrder.getLineItemList().getLineItems() != null) {
            for (LineItem lineItem : salesOrder.getLineItemList().getLineItems()) {
                LineItemResponseDTO lineItemResponseDTO = new LineItemResponseDTO(lineItem);
                lineItems.add(lineItemResponseDTO);
            }
        }

        this.setLineItems(lineItems);

        if (orderHeader.getInvoiceDueDate() != null) {
            this.invoiceDueDate = orderHeader.getInvoiceDueDate().getDate();
        }

        if (orderHeader.getInvoiceNumber() != null) {
            this.invoiceNumber = orderHeader.getInvoiceNumber();
        }

        this.quoteStatus = orderHeader.getQuoteStatus();
    }

    public void setWebStatus(String orderStatus, String shipViaId, String printStatus) {
        String status = String.format("%s-%s-%s", orderStatus, printStatus, WebShipViaMapping.map.get(shipViaId));

        switch (status) {
            case "I-Q-WILL_CALL":
                this.webStatus = WebStatuses.READY_FOR_PICKUP;
                return;
            case "I-P-WILL_CALL":
                this.webStatus = WebStatuses.PICKED_UP;
                return;
            case "I-Q-OUR_TRUCK":
                this.webStatus = WebStatuses.IN_PROCESS;
                return;
            case "I-M-OUR_TRUCK":
                this.webStatus = WebStatuses.MANIFESTED;
                return;
            case "I-P-OUR_TRUCK":
                this.webStatus = WebStatuses.DELIVERED;
                return;
            default:
                if (status.startsWith("I-N") || status.startsWith("I-B")) {
                    this.webStatus = WebStatuses.INVOICED;
                    return;
                } else if (status.startsWith("I") && status.contains("THIRD_PARTY")) {
                    this.webStatus = WebStatuses.SHIPPED;
                    return;
                } else if (status.startsWith("D")) {
                    this.webStatus = WebStatuses.DIRECT;
                    return;
                } else if (status.startsWith("P")) {
                    this.webStatus = WebStatuses.PICKUP_NOW;
                    return;
                } else {
                    this.webStatus = WebStatuses.SUBMITTED;
                    return;
                }
        }
    }
}
