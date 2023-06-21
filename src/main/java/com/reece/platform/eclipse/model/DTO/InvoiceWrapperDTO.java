package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceWrapperDTO {
    private List<InvoiceSummaryDTO> invoices;
}
