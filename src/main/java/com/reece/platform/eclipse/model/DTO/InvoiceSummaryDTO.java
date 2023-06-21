package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceSummaryDTO {
    private Integer billTo;
    private Double bucketFuture;
    private Double bucketThirty;
    private Double bucketSixty;
    private Double bucketNinety;
    private Double bucketOneTwenty;
    private Double currentAmt;
    private Double totalAmountDue;
    private Double pastDueAmt;
    private List<InvoiceDTO> invoiceNumber;
}
