package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.AccountInquiryResponse.AccountInquiryResponseWrapper;
import com.reece.platform.eclipse.model.XML.AccountInquiryResponse.AccountInquirySummary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountInquiryResponseDTO {
    public AccountInquiryResponseDTO(AccountInquiryResponseWrapper accountInquiryResponseWrapper) {
        AccountInquirySummary accountInquirySummary = accountInquiryResponseWrapper.getAccountInquiryResponse().getAccountInquirySummary();
        this.bucketThirty = accountInquirySummary.getThirty() != null ? accountInquirySummary.getThirty() : 0;
        this.bucketSixty = accountInquirySummary.getSixty() != null ? accountInquirySummary.getSixty() : 0;
        this.bucketNinety = accountInquirySummary.getNinety() != null ? accountInquirySummary.getNinety() : 0;
        this.bucketOneTwenty = accountInquirySummary.getOneTwenty() != null ? accountInquirySummary.getOneTwenty() : 0;
        this.bucketFuture = accountInquirySummary.getFuture() != null ? accountInquirySummary.getFuture() : 0;
        this.currentAmt = accountInquirySummary.getCurrent() != null ? accountInquirySummary.getCurrent() : 0;
        this.totalAmt = accountInquirySummary.getArTotal() != null ? accountInquirySummary.getArTotal() : 0;
        this.totalPastDue = this.totalAmt - this.currentAmt;

        if (accountInquiryResponseWrapper.getAccountInquiryResponse().getAccountInquiryItemList() != null) {
            this.invoices = accountInquiryResponseWrapper.getAccountInquiryResponse().getAccountInquiryItemList().getAccountInquiryItemList().stream().map(AccountInquiryItemDTO::new).collect(Collectors.toList());
        } else {
            this.invoices = new ArrayList<>();
        }
    }

    public AccountInquiryResponseDTO(InvoiceSummaryDTO invoiceSummaryDTO) {
        this.bucketThirty = invoiceSummaryDTO.getBucketThirty();
        this.bucketSixty = invoiceSummaryDTO.getBucketSixty();
        this.bucketNinety = invoiceSummaryDTO.getBucketNinety();
        this.bucketOneTwenty = invoiceSummaryDTO.getBucketOneTwenty();
        this.bucketFuture = invoiceSummaryDTO.getBucketFuture();
        this.currentAmt = invoiceSummaryDTO.getCurrentAmt();
        this.totalAmt = invoiceSummaryDTO.getTotalAmountDue();
        this.totalPastDue = invoiceSummaryDTO.getPastDueAmt();

        List<InvoiceDTO> invoices = invoiceSummaryDTO.getInvoiceNumber();
        if(invoices != null) {
            this.invoices = invoices
                    .stream()
                    .filter(invoice -> invoice.getInvoiceNumber() != null && !invoice.getInvoiceNumber().isEmpty())
                    .map(AccountInquiryItemDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.invoices = new ArrayList<>();
        }

    }

    // As cents
    private Double bucketThirty;
    private Double bucketSixty;
    private Double bucketNinety;
    private Double bucketOneTwenty;
    private Double bucketFuture;
    private Double currentAmt;
    private Double totalAmt;
    private Double totalPastDue;
    private List<AccountInquiryItemDTO> invoices;
}
