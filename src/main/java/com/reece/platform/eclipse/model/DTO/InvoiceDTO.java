package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String invoiceNumber;
    private Integer shipTo;
    private String shipToName;
    private String status;
    private String poNumber;
    private String invoiceDate;
    private Double invoiceAmount;
    private Double openBalance;
    private String agingBucket;
}
