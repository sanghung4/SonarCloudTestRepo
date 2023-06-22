package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.ErrorDTO;
import com.reece.platform.mincron.model.InvoiceDTO;
import com.reece.platform.mincron.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

    @Autowired
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public @ResponseBody List<InvoiceDTO> getInvoices(@RequestParam String accountId) throws ParseException, MincronException {
        return invoiceService.getInvoices(accountId);
    }

}
