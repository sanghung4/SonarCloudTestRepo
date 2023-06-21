package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.model.DTO.AccountInquiryResponseDTO;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
public class InvoiceController {

    @Autowired
    private KourierService kourierService;

    @Autowired
    public InvoiceController(KourierService kourierService) {
        this.kourierService = kourierService;
    }

    @GetMapping("accounts/{accountId}/invoices")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountInquiryResponseDTO getInvoices(
            @PathVariable String accountId,
            @RequestParam(required = false) String shipTo,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) String invoiceStatus
    ) {
        return kourierService.getInvoices(
                accountId,
                shipTo,
                startDate,
                endDate,
                invoiceStatus
        );
    }
}
