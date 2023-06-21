package com.reece.platform.products.controller;

import com.reece.platform.products.exceptions.InvoiceDateRangeException;
import com.reece.platform.products.exceptions.OrderNotFoundException;
import com.reece.platform.products.model.DTO.GetInvoiceResponseDTO;
import com.reece.platform.products.model.DTO.MincronSingleInvoiceDTO;
import com.reece.platform.products.model.PermissionType;
import com.reece.platform.products.service.InvoiceService;
import com.reece.platform.products.utilities.DecodedToken;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Fetch invoices for a given account
     *
     * @param accountId account to fetch invoices for
     * @param erpName erp system to fetch invoices for
     * @param authorization auth token to check for permissions
     * @return list of invoices
     */
    @GetMapping(path = "/{accountId}/invoices")
    public @ResponseBody ResponseEntity<GetInvoiceResponseDTO> getInvoices(
        @PathVariable String accountId,
        @RequestParam String erpName,
        @RequestParam(required = false) String shipTo,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
        @RequestParam(required = false) String invoiceStatus,
        @RequestHeader(name = "authorization", required = false) String authorization
    ) throws HttpClientErrorException, InvoiceDateRangeException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!token.containsPermission(PermissionType.view_invoice)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        GetInvoiceResponseDTO invoices = invoiceService.getInvoices(
            accountId,
            erpName,
            shipTo,
            startDate,
            endDate,
            invoiceStatus
        );
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping(path = "/{accountId}/invoices-pdf")
    public @ResponseBody ResponseEntity<String> getInvoicesPdf(
        @PathVariable String accountId,
        @RequestParam List<String> invoiceNumbers,
        @RequestHeader(name = "authorization") String authorization
    ) {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!token.containsPermission(PermissionType.view_invoice)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(invoiceService.getInvoicesPdfUrl(accountId, invoiceNumbers), HttpStatus.OK);
    }

    /**
     * Get single invoice for a waterworks user.
     * @param accountId bill-to accountId
     * @param invoiceNumber invoice number which is the same as order number in Mincron
     * @param authorization fails the auth check if user doesn't have view_invoices permission
     * @return JSON object with single invoice data
     * @throws UnsupportedEncodingException
     */
    @GetMapping(path = "{accountId}/invoices/{invoiceNumber}")
    public ResponseEntity<MincronSingleInvoiceDTO> getInvoice(
        @PathVariable String accountId,
        @PathVariable String invoiceNumber,
        @RequestHeader(name = "authorization") String authorization
    ) throws OrderNotFoundException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!token.containsPermission(PermissionType.view_invoice)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(invoiceService.getMincronInvoice(accountId, invoiceNumber), HttpStatus.OK);
    }
}
