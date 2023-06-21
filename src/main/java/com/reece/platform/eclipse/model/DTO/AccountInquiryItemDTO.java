package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.AccountInquiryResponse.AccountInquiryItem;
import lombok.Data;

@Data
public class AccountInquiryItemDTO {
    public AccountInquiryItemDTO(AccountInquiryItem accountInquiryItem) {
        this.invoiceNumber = accountInquiryItem.getAccountRegisterID();
        this.status = accountInquiryItem.getBalance() != 0 ? "Open" : "Closed";
        this.customerPo = accountInquiryItem.getCustomerPo();
        this.invoiceDate = accountInquiryItem.getTransaction().getDate();
        this.originalAmt = accountInquiryItem.getTransaction().getAmount();
        this.openBalance = accountInquiryItem.getBalance();
        this.age = accountInquiryItem.getAge();
    }

    public AccountInquiryItemDTO(InvoiceDTO invoiceDto) {
        this.invoiceNumber = invoiceDto.getInvoiceNumber();
        this.status = invoiceDto.getStatus();
        this.customerPo = invoiceDto.getPoNumber();
        this.invoiceDate = invoiceDto.getInvoiceDate();
        this.originalAmt = invoiceDto.getInvoiceAmount();
        this.openBalance = invoiceDto.getOpenBalance();
        this.age = invoiceDto.getAgingBucket();
        this.jobNumber = String.valueOf(invoiceDto.getShipTo());
        this.jobName = invoiceDto.getShipToName();
    }

    private String invoiceNumber;
    private String status;
    private String customerPo;
    private String invoiceDate;
    private Double originalAmt;
    private Double openBalance;
    private String age;
    private String jobNumber;
    private String jobName;
}
