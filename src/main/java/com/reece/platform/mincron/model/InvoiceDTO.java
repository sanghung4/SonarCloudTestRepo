package com.reece.platform.mincron.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceDTO {

    public InvoiceDTO(OrderDTO closedInvoice) {
        this.customerPo = closedInvoice.getPurchaseOrderNumber();
        this.invoiceDate = closedInvoice.getInvoiceDate();
        this.jobName = closedInvoice.getJobName();
        this.jobNumber = closedInvoice.getJobNumber();
        this.invoiceNumber = closedInvoice.getInvoiceNumber();
        this.openBalance = "0.0d";
        this.originalAmt = closedInvoice.getOrderTotal();
        this.contractNumber = closedInvoice.getContractNumber();
        this.status = "Closed";
        this.age = "";
    }

    private String invoiceNumber;
    private String invoiceDate;
    private String customerPo;
    private String age;
    private String jobNumber;
    private String jobName;
    private String openBalance;
    private String originalAmt;
    private String status;
    private String contractNumber;
}
