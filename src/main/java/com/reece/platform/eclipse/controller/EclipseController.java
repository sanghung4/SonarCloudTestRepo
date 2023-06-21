package com.reece.platform.eclipse.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.DTO.ProductSearchResponseDTO;
import com.reece.platform.eclipse.model.XML.EntitySearchResponse.EntitySearchResult;
import com.reece.platform.eclipse.model.XML.ProductResponse.ProductResponse;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller()
@RequestMapping
public class EclipseController {

    @Autowired
    private EclipseService eclipseService;

    @Autowired
    private KourierService kourierService;
    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    public EclipseController(EclipseService eclipseService, FileTransferService fileTransferService) {
        this.eclipseService = eclipseService;
        this.fileTransferService = fileTransferService;
    }

    @PostMapping("products")
    public @ResponseBody
    ResponseEntity<ProductResponse> getProducts(@RequestParam List<String> productIds, @RequestBody ErpUserInformationDTO erpUserInformationDTO, @RequestParam(defaultValue = "false") Boolean isEmployee) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        ProductResponse eclipseResponse = eclipseService.getProducts(productIds, erpUserInformationDTO, isEmployee);

        return ResponseEntity.status(HttpStatus.OK)
            .headers(responseHeaders)
            .body(eclipseResponse);
    }

    @GetMapping("accounts/{accountId}/quotes")
    public @ResponseBody ResponseEntity<List<GetOrderResponseDTO>> getQuotes(@PathVariable String accountId,
                                                                        @RequestParam String orderPostStartDate,
                                                                        @RequestParam String orderPostEndDate,
                                                                        @RequestParam(required = false) Integer page
    ) throws JAXBException, XMLStreamException, EclipseException, IOException, EclipseTokenException {

        var response = eclipseService.getQuotesByAccountId(accountId, orderPostStartDate, orderPostEndDate, page).getSalesOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("accounts/{accountId}/quotes/{quoteId}")
    public @ResponseBody ResponseEntity<GetOrderResponseDTO> getQuote(@PathVariable String accountId,
                                                                      @PathVariable String quoteId
                                                                      ) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        GetOrderResponseDTO response = eclipseService.getQuoteById(accountId, quoteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("accounts/{accountId}/orders/{orderId}")
    public @ResponseBody ResponseEntity<GetOrderResponseDTO> getOrder(@PathVariable String accountId,
                                                                      @PathVariable String orderId,
                                                                      @RequestParam(required = false) String invoiceNumber) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        GetOrderResponseDTO response = eclipseService.getOrderById(accountId, orderId, invoiceNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("accounts/{accountId}/orders")
    public @ResponseBody ResponseEntity<List<GetOrderResponseDTO>> getOrders(@PathVariable String accountId,
                                                                             @RequestParam(required = false) String orderPostStartDate,
                                                                             @RequestParam(required = false) String orderPostEndDate) throws ExecutionException, InterruptedException, EclipseException, TimeoutException, XMLStreamException, JAXBException, EclipseTokenException {
        List<GetOrderResponseDTO> response = eclipseService.getOrdersByAccountId(accountId, orderPostStartDate, orderPostEndDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("accounts/{accountId}/salesorders")
    public @ResponseBody ResponseEntity<MassSalesOrderResponseDTO> getSalesOrders(@PathVariable String accountId,
                                                                                  @RequestParam(required = false) String orderPostStartDate,
                                                                                  @RequestParam(required = false) String orderPostEndDate,
                                                                                  @RequestParam(required = false) int page) throws JAXBException, XMLStreamException, EclipseException, IOException, EclipseTokenException {
        MassSalesOrderResponseDTO response = eclipseService.getSalesOrders(accountId, orderPostStartDate, orderPostEndDate, page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("product/{productId}")
    public @ResponseBody
    ResponseEntity<ProductResponse> getProduct(@PathVariable String productId, @RequestBody ErpUserInformationDTO erpUserInformationDTO, @RequestParam(defaultValue = "false") Boolean isEmployee) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        ProductResponse eclipseResponse = eclipseService.getProductById(productId, erpUserInformationDTO, isEmployee);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(eclipseResponse);
    }

    @GetMapping("product/{productId}/imageUrl")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getProductImageUrl(@PathVariable String productId) throws EclipseTokenException, ProductImageUrlNotFoundException {
        return eclipseService.getProductImageUrl(productId);
    }

    @GetMapping("accounts/{accountId}")
    public @ResponseBody
    ResponseEntity<GetAccountResponseDTO> getAccount(
            @PathVariable String accountId,
            @RequestParam(required = false, defaultValue = "false") Boolean retrieveBillTo,
            @RequestParam(required = false, defaultValue = "false") Boolean retrieveShipToList
    ) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        GetAccountResponseDTO account = eclipseService.getAccountById(accountId, retrieveBillTo, retrieveShipToList);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(account);
    }

    @GetMapping("accounts/{accountId}/user/{userId}")
    public @ResponseBody
    ResponseEntity<GetContactResponseDTO> getContact(@PathVariable String accountId, @PathVariable String userId) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        GetContactResponseDTO response = eclipseService.getContactById(accountId, userId);
        return new ResponseEntity<GetContactResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("entitySearch/{accountId}")
    public @ResponseBody
    ResponseEntity<EntitySearchResult> searchEntity(@PathVariable String accountId) throws EclipseTokenException, EclipseException {
        EntitySearchResponseDTO results = eclipseService.searchEntity(accountId);
        if(!results.getCustomers().getResults().isEmpty()){
            return new ResponseEntity<EntitySearchResult>(results.getCustomers().getResults().get(0), HttpStatus.OK);
        }
        else{
            throw new EclipseException(String.format("No account with id %s found.", accountId));
        }
    }

    @PostMapping("accounts/{accountId}/user")
    public @ResponseBody
    ResponseEntity<CreateContactResponseDTO> createContact(@PathVariable String accountId, @RequestBody CreateContactRequestDTO createContactRequestDTO) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {
        CreateContactResponseDTO response = eclipseService.createContact(accountId, createContactRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("accounts/{accountId}/user/{userId}")
    public @ResponseBody
    ResponseEntity<UpdateContactResponseDTO> updateContact(@PathVariable String accountId, @PathVariable String userId, @RequestBody UpdateContactRequestDTO updateContactRequestDTO) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {
        UpdateContactResponseDTO response = eclipseService.updateContact(userId, accountId, updateContactRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("accounts/{accountId}/user/{userId}")
    public @ResponseBody
    ResponseEntity<String> deleteContact(@PathVariable String userId) throws EclipseTokenException, InvalidIdException {
        String response = eclipseService.deleteContact(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("accounts/job")
    public @ResponseBody
    ResponseEntity<String> createJob(@RequestBody JobFormDTO jobFormDTO) throws IOException, JSchException, SftpException {
        String response = fileTransferService.uploadJobForm(jobFormDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("accounts/{accountId}/tax-document")
    public @ResponseBody
    ResponseEntity<DocumentImagingFileDTO> uploadTaxDocument(@PathVariable String accountId, @RequestBody String encodedFileData) throws EclipseTokenException, InvalidIdException {
        DocumentImagingFileDTO response = eclipseService.uploadTaxDocument(accountId, encodedFileData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This endpoint was created for local testing purposes
    @GetMapping("contacts/{userId}")
    public @ResponseBody
    ResponseEntity<String> getContact(@PathVariable String userId) throws EclipseTokenException, InvalidIdException {
        String response = eclipseService.getContact(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("orders")
    public @ResponseBody ResponseEntity<GetOrderResponseDTO> submitOrder(@RequestBody SalesOrderDTO salesOrderDTO) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        GetOrderResponseDTO eclipseResponse = eclipseService.submitOrder(salesOrderDTO);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PostMapping("quotes/approve")
    public ResponseEntity<GetOrderResponseDTO> approveQuote(@RequestBody ApproveQuoteDTO approveQuoteDTO) throws EclipseException, XMLStreamException, JAXBException, EclipseTokenException, InvalidIdException {
        // TODO: tw - validate that the quoteId is here
        var response = eclipseService.approveQuote(approveQuoteDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("quotes/reject")
    public ResponseEntity rejectQuote(@RequestBody RejectQuoteDTO rejectQuoteDTO) throws EclipseTokenException, InvalidIdException {
        eclipseService.rejectQuote(rejectQuoteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("orders/preview")
    public @ResponseBody ResponseEntity<GetOrderResponseDTO> submitOrderPreview(@RequestBody SalesOrderDTO salesOrderDTO) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        GetOrderResponseDTO eclipseResponse = eclipseService.submitOrderPreview(salesOrderDTO);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @GetMapping("credit-card/{accountId}")
    public @ResponseBody ResponseEntity<CreditCardListResponseDTO> getCreditCardList(@PathVariable String accountId) throws EclipseException, XMLStreamException, JAXBException, EclipseTokenException {
        CreditCardListResponseDTO cardListResponse = eclipseService.getCreditCardList(accountId);
        return new ResponseEntity<>(cardListResponse, HttpStatus.OK);
    }

    @DeleteMapping("credit-card/{accountId}/{creditCardId}")
    public ResponseEntity deleteCreditCard(@PathVariable String accountId, @PathVariable String creditCardId) throws EclipseException, XMLStreamException, JAXBException, CardInUseException, EclipseTokenException {
        eclipseService.deleteCreditCard(accountId, creditCardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("credit-card/{accountId}")
    public @ResponseBody ResponseEntity<EntityUpdateSubmitResponseDTO> updateCreditCardList(@PathVariable String accountId, @RequestBody EntityUpdateSubmitRequestDTO updateSubmitRequestDTO) throws EclipseException, XMLStreamException, JAXBException, EclipseTokenException {
        EntityUpdateSubmitResponseDTO eclipseResponse = eclipseService.updateEntityInquiryForCreditCard(accountId, updateSubmitRequestDTO);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PostMapping("credit-card/{accountId}/setup-url")
    public @ResponseBody ResponseEntity<ElementAccountSetupResponseDTO> getCreditCardSetupUrl(@PathVariable String accountId, @RequestBody ElementAccountSetupDataDTO elementAccountSetupDataDTO) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        ElementAccountSetupResponseDTO eclipseResponse = eclipseService.getCreditCardSetupUrl(accountId, elementAccountSetupDataDTO);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PostMapping("credit-card/{accountId}/info/{elementSetupId}")
    public @ResponseBody ResponseEntity<ElementSetupQueryResponseDTO> getCreditCardElementInfo(@PathVariable String accountId, @PathVariable String elementSetupId) throws JAXBException, XMLStreamException, EclipseException, EclipseTokenException {
        ElementSetupQueryResponseDTO eclipseResponse = eclipseService.getCreditCardElementInfo(accountId, elementSetupId);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @GetMapping("account/{accountId}/previouslyPurchasedProducts")
    public @ResponseBody ResponseEntity<PreviouslyPurchasedProductsDTO> getPreviouslyPurchasedProducts(
            @PathVariable String accountId,
            @RequestParam int currentPage,
            @RequestParam int pageSize
    ) throws EclipseException, JAXBException, XMLStreamException, EclipseTokenException {
        var eclipseResponse = eclipseService.getPreviouslyPurchasedProducts(accountId, currentPage, pageSize);

        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PostMapping("products/search")
    public @ResponseBody ResponseEntity<ProductSearchResponseDTO> getProductsBySearch(
            @RequestBody ProductSearchRequestDTO request
    ) throws EclipseTokenException {
        var eclipseResponse = eclipseService.getProductSearch(request);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @GetMapping("products/search")
    public @ResponseBody ResponseEntity<com.reece.platform.eclipse.model.DTO.kourier.ProductSearchResponseDTO> getProductsBySearch(
            @RequestParam String keywords,
            @RequestParam(value = "displayName",required=false) String displayName
    ) throws EclipseTokenException, UnsupportedEncodingException, InvalidProductSearchException {
        if (keywords==null || keywords.isEmpty()){
            throw new InvalidProductSearchException();
        }
        var eclipseResponse = kourierService.getProductSearch(keywords,displayName);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

}
