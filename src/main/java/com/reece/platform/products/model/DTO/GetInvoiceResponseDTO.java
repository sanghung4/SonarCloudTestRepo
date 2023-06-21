package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class GetInvoiceResponseDTO {

    private Double bucketThirty;
    private Double bucketSixty;
    private Double bucketNinety;
    private Double bucketOneTwenty;
    private Double bucketFuture;
    private Double currentAmt;
    private Double totalAmt;
    private Double totalPastDue;
    private List<InvoiceDTO> invoices;
}
