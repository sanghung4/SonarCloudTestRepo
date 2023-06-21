package com.reece.platform.products.model.DTO;

import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetOrderResponseDTO {

    public GetOrderResponseDTO(GetOrderResponseMincronDTO getOrderResponseMincronDTO) throws ParseException {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat waterworksDateFormat = new SimpleDateFormat("MMddyyyy");
        String orderDate = getOrderResponseMincronDTO.getOrderDate();
        String shipDate = getOrderResponseMincronDTO.getShipDate();

        /*
        Order date format from Mincron has a single digit for the month in months 1 - 9.  (i.e. 1122020 for the date 01/12/2020)
        In this case, the length of the date string would be 7 which no longer matches the above date pattern causing incorrect dates when formatting to MM/dd/yyyy.
        Appending a "0" character to match date format in this case to ensure properly formatted dates to display.
        */
        if (orderDate.length() == 7) {
            orderDate = "0" + orderDate;
        }
        boolean isShipDateValid = true;
        if (shipDate.length() == 7) {
            shipDate = "0" + shipDate;
        } else if (shipDate.length() < 7) {
            this.shipDate = "";
            isShipDateValid = false;
        }

        this.orderDate = displayDateFormat.format(waterworksDateFormat.parse(orderDate));
        this.orderNumber = getOrderResponseMincronDTO.getOrderNumber();
        this.webStatus = getOrderResponseMincronDTO.getStatus().toUpperCase(Locale.ROOT);
        this.customerPO = getOrderResponseMincronDTO.getPurchaseOrderNumber();
        this.shipToName = getOrderResponseMincronDTO.getJobName();
        if (isShipDateValid) {
            this.shipDate = displayDateFormat.format(waterworksDateFormat.parse(shipDate));
        }
        this.orderTotal = Float.parseFloat(getOrderResponseMincronDTO.getOrderTotal());
        this.contractNumber = getOrderResponseMincronDTO.getContractNumber();
        this.jobNumber = getOrderResponseMincronDTO.getJobNumber();
    }

    public GetOrderResponseDTO(OrderHeader orderHeader) {
        this.webStatus = orderHeader.getOrderStatus();
        this.customerPO = orderHeader.getPurchaseOrderNumber();
        this.orderedBy = orderHeader.getOrderBy();
        this.deliveryMethod = orderHeader.getShipmentMethod();
        this.tax = orderHeader.getTaxAmount();
        this.orderTotal = orderHeader.getTotalAmount();
        this.subTotal = orderHeader.getSubTotal();
        this.shipDate = orderHeader.getShipDate();
        this.shipToName = orderHeader.getJobName();
        this.orderDate = orderHeader.getOrderDate();
        this.terms = orderHeader.getTerms();
        this.contractNumber = orderHeader.getContractNumber();
        StringBuffer spInstructionList = new StringBuffer();
        for (var specialInstruction : orderHeader.getSpecialInstructions()) {
            spInstructionList.append(specialInstruction);
        }
        this.specialInstructions = spInstructionList.toString().trim();

        if (orderHeader.getShipToAddress() != null) {
            this.shipAddress = new EclipseAddressResponseDTO(orderHeader.getShipToAddress());
        }
    }

    private String orderNumber;
    private String invoiceNumber;
    private String orderStatus;
    private String webStatus;
    private String shipDate;
    private String customerPO;
    private String orderDate;
    private String amount;
    private String invoiceDueDate;
    private String billToName;
    private String shipToName;
    private String orderedBy;
    private String terms;
    private String contractNumber;
    private EclipseAddressResponseDTO shipAddress;
    private String email;
    private CreditCardDTO creditCard;
    private BranchOrderInfoDTO branchInfo;
    private String deliveryMethod;
    private String specialInstructions;
    private float subTotal;
    private float tax;
    private float orderTotal;
    private String bidExpireDate;
    private String requiredDate;
    private String shipToId;
    private String jobNumber;
    private List<OrderLineItemResponseDTO> lineItems;
}
