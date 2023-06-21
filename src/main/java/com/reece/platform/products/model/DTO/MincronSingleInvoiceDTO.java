package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MincronSingleInvoiceDTO {

    private String invoiceNumber;
    private String status;
    private String terms;
    private String customerPo;
    private String invoiceDate;
    private String jobNumber;
    private String jobName;
    private EclipseAddressResponseDTO address;
    private String shipDate;
    private String deliveryMethod;
    private String subtotal;
    private String tax;
    private String otherCharges;
    private String openBalance;
    private String paidToDate;
    private String originalAmt;
    private List<InvoiceProduct> invoiceItems;
    private boolean isDelivery;
    private String dueDate;

    public MincronSingleInvoiceDTO(OrderHeader orderHeader, Double openBalance) {
        this.deliveryMethod = orderHeader.getShipmentMethod();
        this.isDelivery = orderHeader.isDelivery();
        this.shipDate = orderHeader.getShipDate();
        this.dueDate = orderHeader.getDueDate();
        if (orderHeader.getShipToAddress() != null) {
            this.address = new EclipseAddressResponseDTO(orderHeader.getShipToAddress());
        }
        //        this.terms = orderHeader.getTerms(); // TODO add terms to OrderHeader response
        this.invoiceNumber = orderHeader.getOrderNumber().trim();
        this.customerPo = orderHeader.getPurchaseOrderNumber().trim();
        this.jobName = orderHeader.getJobName().trim();
        this.jobNumber = orderHeader.getJobNumber().trim();
        this.invoiceDate = orderHeader.getInvoiceDate();
        this.status = orderHeader.getOrderStatus();
        this.subtotal = String.format("%.2f", orderHeader.getSubTotal());
        this.tax = String.format("%.2f", orderHeader.getTaxAmount());
        this.otherCharges = String.format("%.2f", orderHeader.getOtherCharges());
        this.originalAmt = String.format("%.2f", orderHeader.getTotalAmount());
        this.openBalance = String.format("%.2f", openBalance);
        this.paidToDate = String.format("%.2f", orderHeader.getTotalAmount() - openBalance.floatValue());
        this.terms = orderHeader.getTerms();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class InvoiceProduct {

        @Data
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        public static class InvoiceProductQty {

            private int quantityOrdered = 0;
            private int quantityShipped = 0;

            public InvoiceProductQty(ProductLineItem productLineItem) {
                if (!productLineItem.getQuantityOrdered().isEmpty()) {
                    quantityOrdered = Integer.parseInt(productLineItem.getQuantityOrdered());
                }

                if (!productLineItem.getQuantityShipped().isEmpty()) {
                    quantityShipped = Integer.parseInt(productLineItem.getQuantityShipped());
                }
            }
        }

        private String id;
        private String brand;
        private String name;
        private String partNumber;
        private String mfr;
        private String thumb;
        private String price;
        private InvoiceProductQty qty;
        private String pricingUom;

        public InvoiceProduct(ProductDTO product, ProductLineItem productLineItem) {
            partNumber = productLineItem.getProductNumber();
            price = String.format("%.2f", Float.valueOf(productLineItem.getUnitPrice()));
            qty = new InvoiceProductQty(productLineItem);
            name = productLineItem.getDescription().trim();
            pricingUom = productLineItem.getPricingUom();

            if (product != null) {
                id = product.getId();
                brand = product.getManufacturerName();
                name = product.getName();
                mfr = product.getManufacturerNumber();
                thumb = product.getImageUrls().getThumb();
            } else {
                id = "null-product-" + UUID.randomUUID();
            }
        }
    }
}
