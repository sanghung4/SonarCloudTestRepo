package com.reece.platform.eclipse.service.EclipseService;

import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.helpers.Partition;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.DTO.ProductSearchResponseDTO;
import com.reece.platform.eclipse.model.XML.AccountInquiry.AccountInquiry;
import com.reece.platform.eclipse.model.XML.AccountInquiry.AccountInquiryRequest;
import com.reece.platform.eclipse.model.XML.AccountInquiryResponse.AccountInquiryResponseWrapper;
import com.reece.platform.eclipse.model.XML.ContactInquiry.ContactInquiryRequest;
import com.reece.platform.eclipse.model.XML.ContactInquiryResponse.ContactInquiryResponseWrapper;
import com.reece.platform.eclipse.model.XML.ContactNewSubmit.ContactNewSubmitRequest;
import com.reece.platform.eclipse.model.XML.ContactNewSubmitResponse.ContactNewSubmitResponseWrapper;
import com.reece.platform.eclipse.model.XML.ContactUpdateSubmit.ContactUpdateSubmitRequest;
import com.reece.platform.eclipse.model.XML.ContactUpdateSubmitResponse.ContactUpdateSubmitResponseWrapper;
import com.reece.platform.eclipse.model.XML.ElementAccount.*;
import com.reece.platform.eclipse.model.XML.EntityRequest.EntityRequest;
import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import com.reece.platform.eclipse.model.XML.EntityUpdateSubmit.EntityUpdateSubmitRequest;
import com.reece.platform.eclipse.model.XML.EntityUpdateSubmit.EntityUpdateSubmitResponseWrapper;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiry.MassSalesOrderInquiryRequest;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.MassSalesOrderResponse;
import com.reece.platform.eclipse.model.XML.ProductRequest.ProductRequest;
import com.reece.platform.eclipse.model.XML.ProductResponse.ProductResponse;
import com.reece.platform.eclipse.model.XML.ProductSearchResponse.ProductSearchResult;
import com.reece.platform.eclipse.model.XML.ReorderPadInquiry.ReorderPadInquiry;
import com.reece.platform.eclipse.model.XML.ReorderPadInquiry.ReorderPadInquiryRequest;
import com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse.ReorderPadInquiryResponseWrapper;
import com.reece.platform.eclipse.model.XML.SalesOrder.*;
import com.reece.platform.eclipse.model.XML.SalesOrder.SalesOrderSubmit.*;
import com.reece.platform.eclipse.model.XML.SalesOrderInquiry.SalesOrderInquiryRequest;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrderResponse;
import com.reece.platform.eclipse.model.XML.common.*;
import com.reece.platform.eclipse.model.XML.common.Branch;
import com.reece.platform.eclipse.model.XML.common.ShipVia;
import com.reece.platform.eclipse.model.enums.OrderStatusEnum;
import com.reece.platform.eclipse.model.generated.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tomcat.util.json.ParseException;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class EclipseService extends BaseEclipseService {
    private static final String WILL_CALL_SHIP_VIA_ID = "WILL CALL";
    private static final String DELIVERY_SHIP_VIA_ID = "OT OUR TRUCK";
    private static final String QUOTE_STATUS = "ORDER";
    private static final int MAX_ORDER_PARTITIONS = 10;

    @Autowired
    public EclipseSessionService eclipseSessionService;

    @Autowired
    public AsyncExecutionsService asyncExecutionsService;

    public EclipseService(@Qualifier("xml") RestTemplate xml, @Qualifier("json") RestTemplate json){
        this.restTemplateXML = xml;
        this.restTemplate = json;
    }

    /**
     * getOrderById makes a SalesOrderInquiry request and returns an object with order details for the given order and
     * generation ID.
     * @param accountId
     * @param orderId
     * @param invoiceNumber secondary identifier that is part of the order ID
     * @return order details for a single order
     * @throws EclipseException when call to Eclipse returns an error
     */
    public GetOrderResponseDTO getOrderById(String accountId, String orderId, String invoiceNumber)
        throws EclipseException, EclipseTokenException {
        var salesOrderResponse = getSalesOrderById(accountId, orderId, invoiceNumber);
        return new GetOrderResponseDTO(salesOrderResponse);
    }

    /**
     * Helper to fetch sales orders. Returns data from eclipse instead of in a DTO.
     * @param accountId
     * @param orderId
     * @param invoiceNumber
     * @return
     * @throws EclipseException
     */
    private SalesOrderResponse getSalesOrderById(String accountId, String orderId, String invoiceNumber)
        throws EclipseException, EclipseTokenException {
        SalesOrderInquiryRequest salesOrderInquiryRequest = new SalesOrderInquiryRequest(
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            orderId,
            invoiceNumber
        );
        SalesOrderResponse salesOrderResponse = sendRequest(salesOrderInquiryRequest, SalesOrderResponse.class)
            .orElseThrow();

        if (!salesOrderResponse.getSalesOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            ErrorMessage errorMessage = salesOrderResponse
                .getSalesOrderInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .getErrorMessages()
                .get(0);
            String errorDescription = errorMessage.getDescription();
            String errorCode = StringUtils.isEmpty(errorMessage.getCode()) ? "" : "(" + errorMessage.getCode() + ")";
            throw new EclipseException(
                errorDescription + " " + errorCode,
                salesOrderResponse.getSalesOrderInquiryResponse().getStatusResult().getDescription()
            );
        }

        return salesOrderResponse;
    }

    /**
     * getQuoteByQuoteId makes a SalesQuoteInquiry request and returns an object with quote details for the given quote and
     * generation ID.
     * @param accountId
     * @param quoteId
     * @return quote details for a single order
     * @throws EclipseException when call to Eclipse returns an error
     */
    public GetOrderResponseDTO getQuoteById(String accountId, String quoteId)
        throws EclipseException, EclipseTokenException {
        SalesOrderInquiryRequest salesOrderInquiryRequest = new SalesOrderInquiryRequest(
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            quoteId,
            null
        );
        SalesOrderResponse salesOrderResponse = sendRequest(salesOrderInquiryRequest, SalesOrderResponse.class)
            .orElseThrow();

        if (!salesOrderResponse.getSalesOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            ErrorMessage errorMessage = salesOrderResponse
                .getSalesOrderInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .getErrorMessages()
                .get(0);
            String errorDescription = errorMessage.getDescription();
            String errorCode = StringUtils.isEmpty(errorMessage.getCode()) ? "" : "(" + errorMessage.getCode() + ")";
            throw new EclipseException(
                errorDescription + " " + errorCode,
                salesOrderResponse.getSalesOrderInquiryResponse().getStatusResult().getDescription()
            );
        }

        return new GetOrderResponseDTO(salesOrderResponse);
    }

    /**
     * getQuotesByAccountId makes an AccountHistoryInquiry request for the specified date range, loops through the
     * AccountHistoryItems, maps specific XML elements to Java objects, and returns a list of quote objects.
     * @param accountId account to retrieve orders from
     * @return list of quotes that have a post date within the provided range of dates
     * @throws EclipseException when call to Eclipse returns an error
     */
    public MassSalesOrderResponseDTO getQuotesByAccountId(
        String accountId,
        String orderPostStartDate,
        String orderPostEndDate,
        Integer page
    ) throws EclipseException, IOException, EclipseTokenException {
        MassSalesOrderInquiryRequest massSalesOrderInquiryRequest = new MassSalesOrderInquiryRequest(
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            orderPostStartDate,
            orderPostEndDate,
            page
        );

        MassSalesOrderResponse massSalesOrderResponse = sendRequest(
            massSalesOrderInquiryRequest,
            MassSalesOrderResponse.class
        )
            .orElseThrow();

        if (!massSalesOrderResponse.getMassSalesOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            String errorMessages = massSalesOrderResponse
                .getMassSalesOrderInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new MassSalesOrderResponseDTO(massSalesOrderResponse, true);
    }

    /**
     * getOrdersByAccountId makes an AccountHistoryInquiry request for the specified date range, loops through the
     * AccountHistoryItems, maps specific XML elements to Java objects, and returns a list of order objects.
     * @param accountId account to retrieve orders from
     * @param postOrderStartDate beginning of date range
     * @param postOrderEndDate end of date range
     * @return list of orders that have a post date within the provided range of dates
     * @throws ExecutionException thrown from parallel execution of OpenOrderInquiry and AccountHistoryInquiry
     * @throws InterruptedException thrown from parallel execution of OpenOrderInquiry and AccountHistoryInquiry
     * @throws EclipseException when call to Eclipse returns an error
     */
    public List<GetOrderResponseDTO> getOrdersByAccountId(
        String accountId,
        String postOrderStartDate,
        String postOrderEndDate
    ) throws EclipseException {
        LocalDate startDate = LocalDate.parse(postOrderStartDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate endDate = LocalDate.parse(postOrderEndDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        int totalDays = (int)ChronoUnit.DAYS.between(startDate, endDate);
        int numberOfPartitions = totalDays/30 + 1;
        val chunks = getDateRangeChunks(startDate, endDate, numberOfPartitions > MAX_ORDER_PARTITIONS ? MAX_ORDER_PARTITIONS: numberOfPartitions);

        List<CompletableFuture<List<GetOrderResponseDTO>>> futures = new ArrayList<>();
        chunks.forEach(chunk -> {
            val chunkStartDate = String.format("%02d", chunk.get(0).getMonthValue()) + "/"
                    + String.format("%02d", chunk.get(0).getDayOfMonth()) + "/"
                    + chunk.get(0).getYear();
            val chunkEndDate = String.format("%02d", chunk.get(1).getMonthValue()) + "/"
                    + String.format("%02d", chunk.get(1).getDayOfMonth()) + "/"
                    + chunk.get(1).getYear();
            try {
                CompletableFuture<List<GetOrderResponseDTO>> future = asyncExecutionsService.getOrdersForDateRangeAsync(
                        accountId,
                        chunkStartDate,
                        chunkEndDate
                );
                futures.add(future);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        CompletableFuture<List<List<GetOrderResponseDTO>>> allCompletableFuture = allFutures.thenApply(future -> {
            return futures.stream().map(completableFuture -> completableFuture.join())
                    .collect(Collectors.toList());
        });

        val orders = new ArrayList<GetOrderResponseDTO>();
        try {
            List<List<GetOrderResponseDTO>> finalList = allCompletableFuture.get();
            finalList.forEach(orderList -> {
                orders.addAll(orderList);
            });
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error occurred: " + e.getMessage());
            throw new EclipseException(e.getMessage());
        }
        return orders;
    }

    public List<List<LocalDate>> getDateRangeChunks(LocalDate startDateInclusive, LocalDate endDateInclusive, int numberOfPartitions) {
        endDateInclusive = endDateInclusive.plusDays(1);
        List<LocalDate> dates = startDateInclusive.datesUntil(endDateInclusive).collect(Collectors.toList());
        Integer chunkSize = (int) Math.ceil((double) dates.size() / (double) numberOfPartitions);

        Partition<LocalDate> partition = Partition.ofSize(dates, chunkSize);
        return partition.stream().map(x -> {
            return Arrays.asList(x.get(0), x.get(x.size() - 1));
        }).collect(Collectors.toList());
    }

    public ProductResponse getProductById(
        String productId,
        ErpUserInformationDTO erpUserInformationDTO,
        Boolean isEmployee
    ) throws EclipseException, EclipseTokenException {
        ProductRequest productRequest = new ProductRequest(
            List.of(productId),
            eclipseSessionService.getRequestSecurity(erpUserInformationDTO.getErpAccountId(), null, null, true)
        );

        ProductResponse res = sendRequest(productRequest, ProductResponse.class).orElseThrow();

        if (!res.getMassProductInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            throw new EclipseException(
                "Error response from Eclipse",
                res.getMassProductInquiryResponse().getStatusResult().getDescription()
            );
        }
        return res;
    }

    public ProductResponse getProducts(
        List<String> productIds,
        ErpUserInformationDTO erpUserInformationDTO,
        Boolean isEmployee
    ) throws EclipseException, EclipseTokenException {
        ProductRequest productRequest = new ProductRequest(
            productIds,
            eclipseSessionService.getRequestSecurity(erpUserInformationDTO.getErpAccountId(), null, null, true)
        );

        ProductResponse res = sendRequest(productRequest, ProductResponse.class).orElseThrow();

        if (!res.getMassProductInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            ErrorMessage errorMessage = res
                .getMassProductInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .getErrorMessages()
                .stream()
                .findFirst()
                .get();
            throw new EclipseException("Error response from Eclipse: " + errorMessage.getDescription());
        }

        return res;
    }

    @Cacheable("account-by-id")
    public GetAccountResponseDTO getAccountById(String accountId, Boolean retrieveBillTo, Boolean retrieveShipToList)
        throws EclipseException, EclipseTokenException {
        EntityRequest entityRequest = new EntityRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true)
        );
        EntityResponse entityResponse = new EntityResponse();
        try {
            entityResponse = sendRequest(entityRequest, EntityResponse.class).get();
        } catch (Exception e) {
            throw new EclipseException();
        }

        if (!entityResponse.getEntityInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            throw new EclipseException(
                "Error response from Eclipse",
                entityResponse.getEntityInquiryResponse().getStatusResult().getDescription()
            );
        }

        // Find parent account if requested
        if (retrieveBillTo && entityResponse.getEntityInquiryResponse().getEntity().getBillTo() != null) {
            String billToId = entityResponse.getEntityInquiryResponse().getEntity().getBillTo().getEntityID();
            boolean hasBillTo = billToId != null && !billToId.isEmpty();

            if (hasBillTo) {
                return this.getAccountById(billToId, true, retrieveShipToList);
            }
        }

        List<GetAccountResponseDTO> shipToAccounts = new ArrayList<>();
        List<String> shipToAccountIds = new ArrayList<>();
        if (entityResponse.getEntityInquiryResponse().getEntity().getShipToList() != null) {
            shipToAccountIds = entityResponse.getEntityInquiryResponse().getEntity().getShipToList().getEntityID();

            // Retrieve Ship To List if requested
            if (retrieveShipToList) {
                if (shipToAccountIds != null) {
                    for (String shipAccountId : shipToAccountIds) {
                        shipToAccounts.add(this.getAccountById(shipAccountId, false, false));
                    }
                }
            }
        }

        GetAccountResponseDTO response = new GetAccountResponseDTO(entityResponse, shipToAccounts);
        response.setShipToAccountIds(shipToAccountIds);
        return response;
    }

    public GetContactResponseDTO getContactById(String accountId, String userId)
        throws EclipseException, EclipseTokenException {
        ContactInquiryRequest contactRequest = new ContactInquiryRequest(
            userId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, false)
        );
        ContactInquiryResponseWrapper contactRes = sendRequest(contactRequest, ContactInquiryResponseWrapper.class)
            .orElseThrow();

        GetContactResponseDTO responseDTO = new GetContactResponseDTO();

        if (!contactRes.getContactInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            throw new EclipseException(
                "Error response from Eclipse",
                contactRes.getContactInquiryResponse().getStatusResult().getDescription()
            );
        }

        // Get list of emails
        List<EmailAddress> emails = contactRes
            .getContactInquiryResponse()
            .getContact()
            .getEmailAddressList()
            .getEmailAddresses();
        List<String> emailList = new ArrayList<>();
        for (EmailAddress email : emails) {
            emailList.add(email.getContent());
        }

        // Get list of phone numbers
        List<Telephone> telephones = contactRes
            .getContactInquiryResponse()
            .getContact()
            .getTelephoneList()
            .getTelephone();
        List<String> telephoneList = new ArrayList<>();
        for (Telephone telephone : telephones) {
            telephoneList.add(telephone.getNumber());
        }

        responseDTO.setFirstName(contactRes.getContactInquiryResponse().getContact().getContactName().getFirstName());
        responseDTO.setLastName(contactRes.getContactInquiryResponse().getContact().getContactName().getLastName());
        responseDTO.setTelephone(telephoneList);
        responseDTO.setEmail(emailList);

        return responseDTO;
    }

    public CreateContactResponseDTO createContact(String accountId, CreateContactRequestDTO createContactRequestDTO)
        throws EclipseException, EclipseTokenException {
        ContactNewSubmitRequest contactNewSubmitRequest = new ContactNewSubmitRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            createContactRequestDTO
        );
        ContactNewSubmitResponseWrapper contactNewSubmitResponseWrapper = sendRequest(
            contactNewSubmitRequest,
            ContactNewSubmitResponseWrapper.class
        )
            .orElseThrow();

        if (
            !contactNewSubmitResponseWrapper.getContactNewSubmitResponse().getStatusResult().getSuccess().equals("Yes")
        ) {
            throw new EclipseException(
                "Error response from Eclipse",
                contactNewSubmitResponseWrapper.getContactNewSubmitResponse().getStatusResult().getDescription()
            );
        }

        CreateContactResponseDTO responseDTO = new CreateContactResponseDTO();
        responseDTO.setContactId(
            contactNewSubmitResponseWrapper.getContactNewSubmitResponse().getContact().getContactID().getContactId()
        );
        responseDTO.setErpUsername(
            contactNewSubmitResponseWrapper.getContactNewSubmitResponse().getContact().getLogin().getLoginID()
        );
        responseDTO.setErpPassword(
            contactNewSubmitResponseWrapper.getContactNewSubmitResponse().getContact().getLogin().getPassword()
        );

        return responseDTO;
    }

    public UpdateContactResponseDTO updateContact(
        String userId,
        String accountId,
        UpdateContactRequestDTO updateContactRequestDTO
    ) throws EclipseException, EclipseTokenException {
        ContactInquiryRequest contactRequest = new ContactInquiryRequest(
            userId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true)
        );
        ContactInquiryResponseWrapper contactRes = sendRequest(contactRequest, ContactInquiryResponseWrapper.class)
            .orElseThrow();

        ContactUpdateSubmitRequest request = new ContactUpdateSubmitRequest(
            userId,
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            updateContactRequestDTO,
            contactRes
        );
        ContactUpdateSubmitResponseWrapper res = sendRequest(request, ContactUpdateSubmitResponseWrapper.class)
            .orElseThrow();

        if (!res.getContactUpdateSubmitResponse().getStatusResult().getSuccess().equals("Yes")) {
            throw new EclipseException(
                "Error response from Eclipse",
                res.getContactUpdateSubmitResponse().getStatusResult().getDescription()
            );
        }

        UpdateContactResponseDTO responseDTO = new UpdateContactResponseDTO();

        // Get list of emails
        List<EmailAddress> emails = res
            .getContactUpdateSubmitResponse()
            .getContact()
            .getEmailAddressList()
            .getEmailAddresses();
        List<String> emailList = new ArrayList<>();
        for (EmailAddress email : emails) {
            emailList.add(email.getContent());
        }

        // Get list of phone numbers
        List<Telephone> telephones = res
            .getContactUpdateSubmitResponse()
            .getContact()
            .getTelephoneList()
            .getTelephone();
        List<String> telephoneList = new ArrayList<>();
        for (Telephone telephone : telephones) {
            telephoneList.add(telephone.getNumber());
        }

        responseDTO.setFirstName(res.getContactUpdateSubmitResponse().getContact().getContactName().getFirstName());
        responseDTO.setLastName(res.getContactUpdateSubmitResponse().getContact().getContactName().getLastName());
        responseDTO.setPhoneNumber(telephoneList);
        responseDTO.setEmail(emailList);
        responseDTO.setPhoneTypeDisplayName(telephones.stream().findFirst().get().getDescription());

        return responseDTO;
    }

    /**
     * Delete a contact from Eclipse.
     * @param userId
     * @return 200 status with empty response body if user deleted
     * @throws InvalidIdException when userId does not exist in Eclipse
     */
    public String deleteContact(String userId) throws InvalidIdException, EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("sessionToken", sessionToken.getSessionToken());
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        String deleteUserUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, request, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("Invalid Id")) {
                throw new InvalidIdException(userId);
            } else {
                throw e;
            }
        }
        return response.getBody();
    }

    /**
     * Get details for given contact in Eclipse.
     * @param userId
     * @return contact details
     * @throws EclipseTokenException
     * @throws InvalidIdException when userId does not exist in Eclipse
     */
    public String getContact(String userId) throws EclipseTokenException, InvalidIdException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("sessionToken", sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        String getContactUrl = String.format(eclipseApiEndpoint + "/Contacts/%s", userId);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(getContactUrl, HttpMethod.GET, request, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("Invalid Id")) {
                throw new InvalidIdException(userId);
            } else {
                throw e;
            }
        }
        return response.getBody();
    }

    /**
     * Search details for given account(entity) in Eclipse.
     * @param accountId
     * @return entity details
     * @throws EclipseTokenException
     */
    public EntitySearchResponseDTO searchEntity(String accountId) throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("sessionToken", sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        String searchEntityUrl = String.format(eclipseApiEndpoint + "/EntitySearch?id=" + accountId);
        ResponseEntity<EntitySearchResponseDTO> response;
        response = restTemplate.exchange(searchEntityUrl, HttpMethod.GET, request, EntitySearchResponseDTO.class);
        return response.getBody();
    }

    /**
     * Submit a sales order to eclipse with given DTO information
     *
     * @param salesOrderDTO information to create sales order with
     * @return SalesOrderSubmitResponseWrapper with order information
     */
    public GetOrderResponseDTO submitOrder(SalesOrderDTO salesOrderDTO) throws EclipseException, EclipseTokenException {
        SalesOrderSubmitRequest salesOrderSubmitRequest = new SalesOrderSubmitRequest(
            buildSalesOrderSubmit(salesOrderDTO)
        );
        return submitSalesOrder(salesOrderSubmitRequest);
    }

    /**
     * Helper method to mark a sales order and send to Eclipse.
     * @param salesOrderSubmitRequest sales order request
     * @return GetOrderResponseDTO sales order response
     * @throws EclipseException on error
     */
    private GetOrderResponseDTO submitSalesOrder(SalesOrderSubmitRequest salesOrderSubmitRequest)
        throws EclipseException {
        // Flag this order from Ecomm
        EclipseIDWrapper writtenBy = new EclipseIDWrapper();
        writtenBy.setEclipseId("WEBSITE");
        OrderHeader header = salesOrderSubmitRequest.getSalesOrderSubmit().getSalesOrder().getOrderHeader();
        header.setSalesSource("Web Order Entry");
        header.setWrittenBy(writtenBy);

        SalesOrderSubmitResponseWrapper salesOrderSubmitResponseWrapper = sendRequest(
            salesOrderSubmitRequest,
            SalesOrderSubmitResponseWrapper.class
        )
            .orElseThrow();

        if (
            !salesOrderSubmitResponseWrapper.getSalesOrderSubmitResponse().getStatusResult().getSuccess().equals("Yes")
        ) {
            String errorMessages = salesOrderSubmitResponseWrapper
                .getSalesOrderSubmitResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new GetOrderResponseDTO(salesOrderSubmitResponseWrapper);
    }

    public GetOrderResponseDTO approveQuote(ApproveQuoteDTO approveQuoteDTO)
        throws EclipseException, InvalidIdException, EclipseTokenException {
        // TODO: tw - remove this if we get spring validation working
        if (approveQuoteDTO.getQuoteId().isBlank()) throw new IllegalArgumentException("No quoteId provided");

        var quote = getSalesOrderById(approveQuoteDTO.getShipToEntityId(), approveQuoteDTO.getQuoteId(), "0001");

        var lineItems = quote
            .getSalesOrderInquiryResponse()
            .getSalesOrder()
            .getLineItemList()
            .getLineItems()
            .stream()
            .map(LineItemDTO::new)
            .collect(Collectors.toList());

        approveQuoteDTO.setLineItems(lineItems);

        var salesOrder = buildSalesOrderSubmit(approveQuoteDTO, true);

        // Comment line items
        var commentedItems = salesOrder
            .getSalesOrder()
            .getLineItemList()
            .getLineItemList()
            .stream()
            .peek(item -> {
                var comment = new LineItemComment(
                    String.format("Item priced from bid: %s", approveQuoteDTO.getQuoteId())
                );
                var comments = item.getLineItemComment();

                if (comments != null) {
                    comments.add(comment);
                } else {
                    item.setLineItemComment(List.of(comment));
                }
            })
            .collect(Collectors.toList());

        salesOrder.getSalesOrder().getLineItemList().setLineItemList(commentedItems);
        salesOrder
            .getSalesOrder()
            .getOrderHeader()
            .setInternalNotes(String.format("Order created from bid: %s", approveQuoteDTO.getQuoteId()));

        var order = submitSalesOrder(new SalesOrderSubmitRequest(salesOrder));

        updateSalesOrderInternalNotes(
            approveQuoteDTO.getQuoteId(),
            String.format("%s by Ecomm - See Order %s", QuoteStatusEnum.APPROVED, order.getOrderNumber())
        );

        return order;
    }

    public SalesOrderInternalNote updateSalesOrderInternalNotes(String orderId, String note)
        throws InvalidIdException, EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("sessionToken", sessionToken.getSessionToken());
        var internalNoteAppend = new SalesOrderInternalNoteAppend();
        internalNoteAppend.setInternalNotes(note);
        HttpEntity<SalesOrderInternalNoteAppend> request = new HttpEntity<>(internalNoteAppend, httpHeaders);
        String addInternalNoteUrl = String.format(
            eclipseApiEndpoint + "/SalesOrders/%s/InternalNotes?generationId=1&copyToAll=true",
            orderId
        );
        ResponseEntity<SalesOrderInternalNote> response;
        try {
            response =
                restTemplate.exchange(addInternalNoteUrl, HttpMethod.POST, request, SalesOrderInternalNote.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().contains("Invalid Id")) {
                throw new InvalidIdException(orderId);
            } else {
                throw e;
            }
        }
        return response.getBody();
    }

    public void rejectQuote(RejectQuoteDTO rejectQuoteDTO) throws InvalidIdException, EclipseTokenException {
        updateSalesOrderInternalNotes(
            rejectQuoteDTO.getQuoteId(),
            String.format("%s by Ecomm", QuoteStatusEnum.REJECTED)
        );
    }

    /**
     * Submit a sales order preview to eclipse with given DTO information
     *
     * @param salesOrderDTO information to create sales order preview with
     * @return SalesOrderSubmitResponseWrapper with order information
     */
    public GetOrderResponseDTO submitOrderPreview(SalesOrderDTO salesOrderDTO)
        throws EclipseException, EclipseTokenException {
        SalesOrderSubmitPreviewRequest salesOrderSubmitPreviewRequest = new SalesOrderSubmitPreviewRequest(
            buildSalesOrderSubmit(salesOrderDTO)
        );

        SalesOrderSubmitPreviewResponseWrapper salesOrderSubmitPreviewResponseWrapper = sendRequest(
            salesOrderSubmitPreviewRequest,
            SalesOrderSubmitPreviewResponseWrapper.class
        )
            .orElseThrow();

        if (
            !salesOrderSubmitPreviewResponseWrapper
                .getSalesOrderSubmitPreviewResponse()
                .getStatusResult()
                .getSuccess()
                .equals("Yes")
        ) {
            String errorMessages = salesOrderSubmitPreviewResponseWrapper
                .getSalesOrderSubmitPreviewResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new GetOrderResponseDTO(salesOrderSubmitPreviewResponseWrapper);
    }

    /**
     * Get credit card setup url for world pay Iframe
     *
     * @param elementAccountSetupDataDTO
     *
     * @return ElementAccountSetupResponseDTO
     */
    public ElementAccountSetupResponseDTO getCreditCardSetupUrl(
        String accountId,
        ElementAccountSetupDataDTO elementAccountSetupDataDTO
    ) throws EclipseException, EclipseTokenException {
        ElementAccountRequest elementAccountRequest = new ElementAccountRequest(
            buildElementAccountSetup(accountId, elementAccountSetupDataDTO)
        );
        ElementAccountResponseWrapper elementAccountResponseWrapper = sendRequest(
            elementAccountRequest,
            ElementAccountResponseWrapper.class
        )
            .orElseThrow();
        if (
            elementAccountResponseWrapper.getElementAccountSetupResponse().getStatusResult().getSuccess().equals("No")
        ) {
            String errorMessages = elementAccountResponseWrapper
                .getElementAccountSetupResponse()
                .getStatusResult()
                .getDescription();
            throw new EclipseException(errorMessages);
        }
        return new ElementAccountSetupResponseDTO(elementAccountResponseWrapper);
    }

    /**
     * Get Element setup account id
     *
     * @param accountId
     * @param elementSetupId
     *
     * @return ElementSetupQueryResponseDTO
     */
    public ElementSetupQueryResponseDTO getCreditCardElementInfo(String accountId, String elementSetupId)
        throws EclipseException, EclipseTokenException {
        ElementSetupQuery elementSetupQuery = new ElementSetupQuery();
        Security security = eclipseSessionService.getRequestSecurity(accountId, null, null, true);
        elementSetupQuery.setSecurity(security);
        elementSetupQuery.setElementSetupId(elementSetupId);
        ElementSetupQueryRequest elementSetupQueryRequest = new ElementSetupQueryRequest(elementSetupQuery);

        ElementSetupQueryResponseWrapper queryResponseWrapper = sendRequest(
            elementSetupQueryRequest,
            ElementSetupQueryResponseWrapper.class
        )
            .orElseThrow();
        if (queryResponseWrapper.getElementSetupQueryResponse().getStatusResult().getSuccess().equals("No")) {
            String errorMessages = queryResponseWrapper
                .getElementSetupQueryResponse()
                .getStatusResult()
                .getDescription();
            throw new EclipseException(errorMessages);
        }

        return new ElementSetupQueryResponseDTO(queryResponseWrapper);
    }

    /**
     * Add Credit card Information
     *
     * @param accountId
     * @param entityUpdateSubmitRequestDTO
     *
     * @return EntityUpdateSubmitResponseDTO
     */
    public EntityUpdateSubmitResponseDTO updateEntityInquiryForCreditCard(
        String accountId,
        EntityUpdateSubmitRequestDTO entityUpdateSubmitRequestDTO
    ) throws EclipseException, EclipseTokenException {
        EntityResponse entityResponse = getEntityWithCreditCards(accountId);

        EntityUpdateSubmitRequest entityUpdateSubmitRequest = new EntityUpdateSubmitRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            entityUpdateSubmitRequestDTO,
            entityResponse
        );
        EntityUpdateSubmitResponseWrapper entityUpdateSubmitResponseWrapper = sendRequest(
            entityUpdateSubmitRequest,
            EntityUpdateSubmitResponseWrapper.class
        )
            .orElseThrow();
        if (
            entityUpdateSubmitResponseWrapper
                .getEntityUpdateSubmitResponse()
                .getStatusResult()
                .getSuccess()
                .equals("No")
        ) {
            String errorMessages = entityUpdateSubmitResponseWrapper
                .getEntityUpdateSubmitResponse()
                .getStatusResult()
                .getDescription();
            throw new EclipseException(errorMessages);
        }

        EntityResponse updatedEntityResponse = getEntityWithCreditCards(accountId);

        return new EntityUpdateSubmitResponseDTO(entityUpdateSubmitResponseWrapper, updatedEntityResponse);
    }

    private EntityResponse getEntityWithCreditCards(String accountId) throws EclipseException, EclipseTokenException {
        EntityRequest entityRequest = new EntityRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true)
        );
        entityRequest.getEntityInquiry().setIncludeCreditCardData("Yes");
        EntityResponse updatedEntityResponse = sendRequest(entityRequest, EntityResponse.class).orElseThrow();

        if (updatedEntityResponse.getEntityInquiryResponse().getStatusResult().getSuccess().equals("No")) {
            String errorMessages = updatedEntityResponse.getEntityInquiryResponse().getStatusResult().getDescription();
            throw new EclipseException(errorMessages);
        }

        return updatedEntityResponse;
    }

    public void deleteCreditCard(String accountId, String creditCardId)
        throws EclipseException, CardInUseException, EclipseTokenException {
        EntityResponse entityResponse = getEntityWithCreditCards(accountId);

        EntityUpdateSubmitRequest entityUpdateSubmitRequest = new EntityUpdateSubmitRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            entityResponse
        );

        // remove card
        var ccList = entityUpdateSubmitRequest
            .getEntityUpdateSubmitRequestWrapper()
            .getEntity()
            .getCreditCardList()
            .getCreditCard()
            .stream()
            .filter(cc -> !cc.getElementPaymentAccountId().equals(UUID.fromString(creditCardId)))
            .collect(Collectors.toList());

        entityUpdateSubmitRequest
            .getEntityUpdateSubmitRequestWrapper()
            .getEntity()
            .getCreditCardList()
            .setCreditCard(ccList);

        EntityUpdateSubmitResponseWrapper entityUpdateSubmitResponseWrapper = sendRequest(
            entityUpdateSubmitRequest,
            EntityUpdateSubmitResponseWrapper.class
        )
            .orElseThrow();
        if (
            entityUpdateSubmitResponseWrapper
                .getEntityUpdateSubmitResponse()
                .getStatusResult()
                .getSuccess()
                .equals("No")
        ) {
            var hasCardInUseError = entityUpdateSubmitResponseWrapper
                .getEntityUpdateSubmitResponse()
                .getStatusResult()
                .getErrorMessageList()
                .getErrorMessages()
                .stream()
                .filter(err -> err.getCode().equals("827"))
                .collect(Collectors.toList());

            if (hasCardInUseError.size() >= 1) {
                throw new CardInUseException(creditCardId);
            } else {
                String errorMessages = entityUpdateSubmitResponseWrapper
                    .getEntityUpdateSubmitResponse()
                    .getStatusResult()
                    .getDescription();
                throw new EclipseException(errorMessages);
            }
        }
    }

    /**
     * Retrieve Credit Card Info
     *
     * @param accountId
     *
     * @return CreditCardListResponseDTO
     */
    public CreditCardListResponseDTO getCreditCardList(String accountId)
        throws EclipseException, EclipseTokenException {
        EntityRequest entityRequest = new EntityRequest(
            accountId,
            eclipseSessionService.getRequestSecurity(accountId, null, null, true)
        );
        entityRequest.getEntityInquiry().setIncludeCreditCardData("Yes");
        EntityResponse entityResponse = sendRequest(entityRequest, EntityResponse.class).orElseThrow();
        if (entityResponse.getEntityInquiryResponse().getStatusResult().getSuccess().equals("No")) {
            String errorMessages = entityResponse.getEntityInquiryResponse().getStatusResult().getDescription();
            throw new EclipseException(errorMessages);
        }

        return new CreditCardListResponseDTO(entityResponse);
    }

    private SalesOrderSubmit buildSalesOrderSubmit(SalesOrderDTO salesOrderDTO) throws EclipseTokenException {
        return buildSalesOrderSubmit(salesOrderDTO, false);
    }

    //fixable
    private SalesOrderSubmit buildSalesOrderSubmit(SalesOrderDTO salesOrderDTO, Boolean allowManualPricing)
        throws EclipseTokenException {
        BillTo billTo = new BillTo();
        billTo.setEntityID(salesOrderDTO.getBillToEntityId());

        ShipTo shipTo = new ShipTo();
        shipTo.setEntityId(salesOrderDTO.getShipToEntityId());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateWrapper requiredDate = new DateWrapper();
        if (salesOrderDTO.getPreferredDate() != null) {
            requiredDate.setDate(dateFormat.format(salesOrderDTO.getPreferredDate()));
        } else {
            requiredDate.setDate("");
        }

        ShipVia shipVia = new ShipVia();
        shipVia.setShipViaID(salesOrderDTO.getIsDelivery() ? DELIVERY_SHIP_VIA_ID : WILL_CALL_SHIP_VIA_ID);

        OrderStatus orderStatus = determineOrderStatus(
            salesOrderDTO.getIsDelivery(),
            salesOrderDTO.getShouldShipFullOrder(),
            salesOrderDTO.getLineItems()
        );

        Telephone telephone = new Telephone();
        telephone.setNumber(salesOrderDTO.getPhoneNumber());

        List<LineItem> lineItemList = salesOrderDTO
            .getLineItems()
            .stream()
            .map(LineItem::new)
            .peek(item -> {
                if (!allowManualPricing) item.setLineItemPrice(null);
            })
            .collect(Collectors.toList());

        String specialInstructions = formatSpecialInstructions(salesOrderDTO, requiredDate);

        ShippingInformation shippingInformation = new ShippingInformation(
            salesOrderDTO.getAddress(),
            shipVia,
            specialInstructions
        );

        ShippingBranch shippingBranch = new ShippingBranch();
        Branch branch = new Branch();
        branch.setBranchId(salesOrderDTO.getBranchId());
        shippingBranch.setBranch(branch);

        CreditCard creditCard = new CreditCard();
        if (salesOrderDTO.getCreditCard() != null) {
            creditCard.setCreditCardType(salesOrderDTO.getCreditCard().getCreditCardType());
            creditCard.setCreditCardNumber(salesOrderDTO.getCreditCard().getCreditCardNumber());
            creditCard.setExpirationDate(salesOrderDTO.getCreditCard().getExpirationDate());
            creditCard.setCardHolder(salesOrderDTO.getCreditCard().getCardHolder());
            creditCard.setStreetAddress(salesOrderDTO.getCreditCard().getStreetAddress());
            creditCard.setPostalCode(salesOrderDTO.getCreditCard().getPostalCode());
            creditCard.setElementPaymentAccountId(salesOrderDTO.getCreditCard().getElementPaymentAccountId());
        }

        OrderHeader orderHeader = new OrderHeader(
            billTo,
            shipTo,
            requiredDate,
            shippingInformation,
            telephone,
            orderStatus,
            QUOTE_STATUS,
            new Description(salesOrderDTO.getOrderedBy()),
            salesOrderDTO.getPoNumber(),
            salesOrderDTO.getIsDelivery() ? null : shippingBranch,
            creditCard
        );

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderHeader(orderHeader);
        salesOrder.setLineItemList(new LineItemsListWrapper(lineItemList));

        Security security = eclipseSessionService.getRequestSecurity(
            null,
            salesOrderDTO.getEclipseLoginId(),
            salesOrderDTO.getEclipsePassword(),
            false
        );

        return new SalesOrderSubmit(security, salesOrder);
    }

    private ElementAccountSetup buildElementAccountSetup(
        String accountId,
        ElementAccountSetupDataDTO elementAccountSetupDataDTO
    ) throws EclipseTokenException {
        ElementAccountSetupData elementAccountSetupData = new ElementAccountSetupData();
        elementAccountSetupData.setCardHolder(elementAccountSetupDataDTO.getCardHolder());
        elementAccountSetupData.setStreetAddress(elementAccountSetupDataDTO.getStreetAddress());
        elementAccountSetupData.setPostalCode(elementAccountSetupDataDTO.getPostalCode());
        elementAccountSetupData.setReturnUrl(elementAccountSetupDataDTO.getReturnUrl());
        Security security = eclipseSessionService.getRequestSecurity(accountId, null, null, true);

        return new ElementAccountSetup(security, elementAccountSetupData);
    }

    private static String formatSpecialInstructions(SalesOrderDTO salesOrderDTO, DateWrapper requiredDate) {
        String specialInstructions = "";
        String deliveryOption = salesOrderDTO.getIsDelivery() ? "Requested Delivery: " : "Requested Pickup: ";

        if (!requiredDate.getDate().isEmpty() && salesOrderDTO.getPreferredTime() != null) {
            specialInstructions += deliveryOption + requiredDate.getDate() + ", " + salesOrderDTO.getPreferredTime();
        } else if (!requiredDate.getDate().isEmpty()) {
            specialInstructions += deliveryOption + requiredDate.getDate();
        } else if (salesOrderDTO.getPreferredTime() != null) {
            specialInstructions += deliveryOption + salesOrderDTO.getPreferredTime();
        }

        if (salesOrderDTO.getInstructions() != null && specialInstructions.isEmpty()) {
            specialInstructions += salesOrderDTO.getInstructions();
        } else if (salesOrderDTO.getInstructions() != null) {
            specialInstructions += "; " + salesOrderDTO.getInstructions();
        }

        return specialInstructions;
    }

    /**
     * Determines if given line items have enough quantity in stock to fulfill all items
     *
     * @param lineItems line items with quantity ordered and quantity available
     * @return true if all items in stock; false otherwise
     */
    private static boolean allItemsInStock(List<LineItemDTO> lineItems) {
        for (LineItemDTO lineItemDTO : lineItems) {
            if (lineItemDTO.getQuantity() > lineItemDTO.getQtyAvailable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine the correct order status for the sales order.
     * Order status is dependent on if the order was for WILL CALL or DELIVERY, as well
     * as availability of the products ordered.  Delivery orders have an additional variable
     * to determine order status which is whether or not the order should be split into
     * multiple shipments.  See table in https://reeceusa.atlassian.net/browse/DPS-1093 for the logic
     * determining order status.
     *
     * @param isDelivery true if DELIVERY order; false if WILL CALL order
     * @param lineItems all line items for the order
     * @param shouldShipFullOrder DELIVERY only variable that determines whether there will be multiple shipments
     * @return correct order status based on given input
     */
    private static OrderStatus determineOrderStatus(
        Boolean isDelivery,
        Boolean shouldShipFullOrder,
        List<LineItemDTO> lineItems
    ) {
        if (!isDelivery) {
            if (allItemsInStock(lineItems)) {
                return new OrderStatus(OrderStatusEnum.SHIP_WHEN_SPECIFIED);
            }
            return new OrderStatus(OrderStatusEnum.CALL_WHEN_COMPLETE);
        }

        if (allItemsInStock(lineItems) || shouldShipFullOrder) {
            return new OrderStatus(OrderStatusEnum.CALL_WHEN_COMPLETE);
        }

        return new OrderStatus(OrderStatusEnum.CALL_WHEN_AVAILABLE);
    }

    /**
     * Fetch invoices by accountId.
     *
     * @param accountId accountId to fetch invoices for
     * @return list of invoices
     */
    public AccountInquiryResponseDTO getAccountInquiry(String accountId)
        throws EclipseException, EclipseTokenException {
        AccountInquiryRequest accountInquiryRequest = new AccountInquiryRequest();
        AccountInquiry accountInquiry = new AccountInquiry();
        accountInquiry.setEntityId(accountId);
        Security security = eclipseSessionService.getRequestSecurity(accountId, null, null, true);
        accountInquiry.setSecurity(security);
        accountInquiryRequest.setAccountInquiry(accountInquiry);

        AccountInquiryResponseWrapper accountInquiryResponseWrapper = sendRequest(
            accountInquiryRequest,
            AccountInquiryResponseWrapper.class
        )
            .orElseThrow();

        if (!accountInquiryResponseWrapper.getAccountInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            String errorMessages = accountInquiryResponseWrapper
                .getAccountInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new AccountInquiryResponseDTO(accountInquiryResponseWrapper);
    }

    public MassSalesOrderResponseDTO getSalesOrders(
        String accountId,
        String orderPostStartDate,
        String orderPostEndDate,
        int page
    ) throws EclipseException, IOException, EclipseTokenException {
        MassSalesOrderInquiryRequest massSalesOrderInquiryRequest = new MassSalesOrderInquiryRequest(
            eclipseSessionService.getRequestSecurity(accountId, null, null, true),
            orderPostStartDate,
            orderPostEndDate,
            page
        );

        MassSalesOrderResponse massSalesOrderResponse = sendRequest(
            massSalesOrderInquiryRequest,
            MassSalesOrderResponse.class
        )
            .orElseThrow();

        if (!massSalesOrderResponse.getMassSalesOrderInquiryResponse().getStatusResult().getSuccess().equals("Yes")) {
            String errorMessages = massSalesOrderResponse
                .getMassSalesOrderInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new MassSalesOrderResponseDTO(massSalesOrderResponse, false);
    }

    /**
     * Query Eclipse API for product search results
     * @param request
     * @return
     * @throws EclipseTokenException
     */
    public ProductSearchResponseDTO getProductSearch(ProductSearchRequestDTO request) throws EclipseTokenException {
        final int DEFAULT_PAGE_SIZE = 8;
        final int DEFAULT_CURRENT_PAGE = 1;

        int searchMode = SearchTypeEnum.ALL_PRIMARY.getValue();
        String keyword = request.getSearchTerm();
        Integer pageSize = request.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        Integer currentPage = request.getCurrentPage();
        if (currentPage == null || currentPage < 1) {
            currentPage = DEFAULT_CURRENT_PAGE;
        }
        int startIndex = pageSize * (currentPage - 1) + 1; // sets start index at beginning of page, eclipse products are index 1

        boolean includeTotalItems = true;

        int searchInputType = request.getSearchInputType();

        String updatedAfter; //Unused for now

        String searchUri = String.format("%s%s", eclipseApiEndpoint, "/Products");

        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        // Omitting ID query since this is covered in keyword
        UriComponentsBuilder partialUri = UriComponentsBuilder
            .fromHttpUrl(searchUri)
            .queryParam("searchMode", "{searchMode}")
            .queryParam("keyword", "{keyword}") // This field is required even if ID is supplied, in which case the value does not matter
            .queryParam("startIndex", "{startIndex}")
            .queryParam("pageSize", "{pageSize}")
            .queryParam("includeTotalItems", "{includeTotalItems}");
        //                .queryParam("updatedAfter", "updatedAfter")

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("searchMode", searchMode);
        queryParams.put("keyword", keyword);
        queryParams.put("startIndex", startIndex);
        queryParams.put("pageSize", pageSize);
        queryParams.put("includeTotalItems", includeTotalItems);

        if (searchInputType == SearchInputTypeEnum.ID.getValue()) {
            partialUri = partialUri.queryParam("id", "{id}");
            queryParams.put("id", keyword);
        }

        String urlTemplate = partialUri.encode().toUriString();

        ResponseEntity<ProductSearchResponseDTO> response;
        ProductSearchResponseDTO results;

        response =
            restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                eclipseRequest,
                ProductSearchResponseDTO.class,
                queryParams
            );
        results = response.getBody();
        for (ProductSearchResult result : results.getResults()) {
            var imageUrl = getProductImageUrlOrNull(Integer.toString(result.getId()));
            ProductWebReference pwr = new ProductWebReference();
            pwr.setWebReferenceId("THUMB");
            pwr.setWebReferenceDescription("THUMBNAIL");
            pwr.setWebReferenceParameters(imageUrl);
            result.setWebReferences(Collections.singletonList(pwr));
        }
        return response.getBody();
    }

    private String getProductImageUrlOrNull(String productId) {
        try {
            return getProductImageUrl(productId);
        } catch (Exception e) {
            return null;
        }
    }

    public String getProductImageUrl(String productId) throws ProductImageUrlNotFoundException, EclipseTokenException {
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("Products/")
            .path(productId)
            .path("/ImageUrl")
            .queryParam("thumbnail", true)
            .queryParam("keyword", true)
            .encode()
            .toUriString();

        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ProductImageUrlNotFoundException();
            }
            throw e;
        }

        return response.getBody();
    }

    /**
     * Upload given encoded file data to Eclipse
     *
     * @param accountId account file is associated with
     * @param encodedFileData file data
     * @return DTO with file information from Eclipse
     * @throws InvalidIdException when Eclipse throws an error on file upload for account id given
     */
    public DocumentImagingFileDTO uploadTaxDocument(String accountId, String encodedFileData)
        throws InvalidIdException, EclipseTokenException {
        DocumentImagingFile documentImagingFile = buildDocumentImagingFile(accountId, encodedFileData);
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("sessionToken", sessionToken.getSessionToken());
        HttpEntity<DocumentImagingFile> request = new HttpEntity<>(documentImagingFile, httpHeaders);
        String uploadDocumentUrl = eclipseApiEndpoint + "/DocumentImagingFiles/";
        ResponseEntity<DocumentImagingFile> response;
        try {
            response = restTemplate.exchange(uploadDocumentUrl, HttpMethod.POST, request, DocumentImagingFile.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Id")) {
                throw new InvalidIdException(accountId);
            } else {
                throw e;
            }
        }
        return new DocumentImagingFileDTO(response.getBody());
    }

    /**
     * Build the body of the file upload endpoint for Eclipse
     *
     * @param accountId account file is associated with
     * @param encodedFileData file data
     * @return Object body for file upload
     */
    private DocumentImagingFile buildDocumentImagingFile(String accountId, String encodedFileData) {
        DocumentImagingFile documentImagingFile = new DocumentImagingFile();
        documentImagingFile.setBase64Document(encodedFileData);
        DocumentImagingFileInformation fileInformation = new DocumentImagingFileInformation();
        fileInformation.setFileExtension("PDF");
        fileInformation.attachDateTime(LocalDateTime.now());
        fileInformation.setProfileId("TAX_CERTS");
        fileInformation.setAttachUserId(accountId);
        fileInformation.setDeleted(false);
        fileInformation.setDescriptionIndexed(false);
        fileInformation.setPendingStorageLocationIds(new ArrayList<>());
        fileInformation.setStorageLocationIds(new ArrayList<>());
        DocumentImagingFileLink documentImagingFileLink = new DocumentImagingFileLink();
        documentImagingFileLink.setLinkId(accountId);
        documentImagingFileLink.setLinkType(DocumentImagingAttachmentType.CUSTOMER);
        documentImagingFileLink.setLinkDisabled(false);
        documentImagingFileLink.setAllLinksDisabled(false);
        fileInformation.setLinks(Collections.singletonList(documentImagingFileLink));
        documentImagingFile.setFileInformation(fileInformation);

        return documentImagingFile;
    }

    public PreviouslyPurchasedProductsDTO getPreviouslyPurchasedProducts(
        String accountId,
        int currentPage,
        int pageSize
    ) throws EclipseException, EclipseTokenException {
        ReorderPadInquiryRequest reorderPadInquiryRequest = new ReorderPadInquiryRequest();
        ReorderPadInquiry reorderPadInquiry = new ReorderPadInquiry();
        reorderPadInquiry.setEntityId(accountId);
        Security security = eclipseSessionService.getRequestSecurity(accountId, null, null, true);
        reorderPadInquiry.setSecurity(security);
        reorderPadInquiryRequest.setReorderPadInquiry(reorderPadInquiry);

        ReorderPadInquiryResponseWrapper reorderPadInquiryResponseWrapper = sendRequest(
            reorderPadInquiryRequest,
            ReorderPadInquiryResponseWrapper.class
        )
            .orElseThrow();

        if (
            !reorderPadInquiryResponseWrapper
                .getReorderPadInquiryResponse()
                .getStatusResult()
                .getSuccess()
                .equals("Yes")
        ) {
            String errorMessages = reorderPadInquiryResponseWrapper
                .getReorderPadInquiryResponse()
                .getStatusResult()
                .getErrorMessageList()
                .toString();
            throw new EclipseException(errorMessages);
        }

        return new PreviouslyPurchasedProductsDTO(
            reorderPadInquiryResponseWrapper.getReorderPadInquiryResponse(),
            currentPage,
            pageSize
        );
    }

    /**
     * Warehouse Picking Services
     */

    /**
     * Query Eclipse API to fetch all orders ready for picking for a specific branch
     * @param branchId
     * @param userId
     * @param orderType
     * @return
     * @throws EclipseTokenException
     */
    public PickingTasksResponseDTO getPickingTasks(String branchId, String userId, OrderMode orderType)
        throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("PickTasks")
            .queryParam("branchId", "{branchId}")
            .queryParam("userId", "{userId}")
            .queryParam("orderType", "{orderType}")
            .encode()
            .toUriString();

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("branchId", branchId);
        queryParams.put("userId", userId);
        queryParams.put("orderType", orderType.getValue());

        ResponseEntity<PickingTasksResponseDTO> response;

        try {
            response =
                restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.GET,
                    eclipseRequest,
                    PickingTasksResponseDTO.class,
                    queryParams
                );
        } catch (Exception e) {
            //TODO: check & catch exceptions
            throw e;
        }
        return response.getBody();
    }

    /**
     * Assign a pick task to a user via Eclipse API
     * @param list
     * @return
     * @throws EclipseTokenException
     */
    public WarehousePickTaskList assignPickingTasks(WarehousePickTaskList list) throws EclipseTokenException {
        String assignPickingTaskUri = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("PickTasks")
            .toUriString();

        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<WarehousePickTaskList> eclipseRequest = new HttpEntity<>(list, httpHeaders);

        ResponseEntity<WarehousePickTaskList> response;

        try {
            response =
                restTemplate.exchange(
                    assignPickingTaskUri,
                    HttpMethod.PUT,
                    eclipseRequest,
                    WarehousePickTaskList.class
                );
        } catch (Exception e) {
            //TODO: check & catch exceptions
            throw e;
        }
        return response.getBody();
    }

    public WarehouseTaskUserPicksDTO getUserPicks(String branchId, String userId) throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("UserPick")
            .queryParam("branchId", branchId)
            .queryParam("userId", userId)
            .encode()
            .toUriString();

        ResponseEntity<WarehouseTaskUserPicksDTO> response;

        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, WarehouseTaskUserPicksDTO.class);
        } catch (Exception e) {
            //TODO: check & catch exceptions
            throw e;
        }
        return response.getBody();
    }

    /**
     * Get Serial Number for Product
     * @param warehouseId
     * @return
     * @throws ParseException
     * @throws InvalidSerializedProductException
     */
    public ProductSerialNumbersResponseDTO getSerialNumbers(String warehouseId)
        throws InvalidSerializedProductException, EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("SerialNumbers")
            .queryParam("warehouseId", warehouseId)
            .encode()
            .toUriString();

        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response =
                restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.GET,
                    eclipseRequest,
                    ProductSerialNumbersResponseDTO.class
                );
        } catch (Exception e) {
            if (e.getMessage().contains("This is not a serialized product")) {
                throw new InvalidSerializedProductException();
            }
            throw e;
        }
        return response.getBody();
    }

    /**
     * Update Serial Numbers with Eclipse REST API
     * @param warehouseId
     * @param serialNumbers
     * @return
     * @throws EclipseTokenException
     */
    public ProductSerialNumbersResponseDTO updateSerialNumbers(
        String warehouseId,
        WarehouseSerialNumbers serialNumbers
    ) throws InvalidSerializedProductException, EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehouseSerialNumbers> eclipseRequest = new HttpEntity<>(serialNumbers, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "SerialNumbers", warehouseId)
            .encode()
            .toUriString();

        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response =
                restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.PUT,
                    eclipseRequest,
                    ProductSerialNumbersResponseDTO.class
                );
        } catch (Exception e) {
            if (e.getMessage().contains("This is not a serialized product")) {
                throw new InvalidSerializedProductException();
            }
            throw e;
        }
        return response.getBody();
    }

    public WarehousePickComplete completeUserPick(String pickId, WarehousePickComplete userPick)
        throws PickNotFoundException, InvalidToteException, EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehousePickComplete> eclipseRequest = new HttpEntity<>(userPick, httpHeaders);
        ResponseEntity<WarehousePickComplete> response;

        String pickingUri = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("UserPick/")
            .path(pickId)
            .toUriString();

        try {
            response = restTemplate.exchange(pickingUri, HttpMethod.PUT, eclipseRequest, WarehousePickComplete.class);
        } catch (HttpClientErrorException e) {
            if (e.getMessage() != null && e.getMessage().contains("Pick Not Found.")) {
                throw new PickNotFoundException(pickId);
            } else if (e.getMessage() != null && e.getMessage().contains("Tote")) {
                throw new InvalidToteException();
            }
            throw e;
        }
        return response.getBody();
    }

    public WarehouseCloseTask closeTask(WarehouseCloseTask closeTask) throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehouseCloseTask> eclipseRequest = new HttpEntity<>(closeTask, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "CloseTask", closeTask.getInvoiceNumber())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseCloseTask> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseCloseTask.class);
        } catch (Exception e) {
            throw e;
        }
        return response.getBody();
    }

    public WarehouseToteTask updateToteTask(WarehouseToteTask toteTask)
        throws EclipseTokenException, ToteUnavailableException, InvalidToteException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehouseToteTask> eclipseRequest = new HttpEntity<>(toteTask, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "ToteTask", toteTask.getTote())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseToteTask> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseToteTask.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not available to be staged")) {
                throw new ToteUnavailableException(toteTask.getTote());
            } else if (e.getMessage() != null && e.getMessage().contains("is not in file")) {
                throw new InvalidToteException();
            }
            throw e;
        }
        return response.getBody();
    }

    public WarehouseTotePackages updateTotePackages(WarehouseTotePackages totePackages)
        throws EclipseTokenException, ToteLockedException, InvalidToteException, InvalidInvoiceException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehouseTotePackages> eclipseRequest = new HttpEntity<>(totePackages, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "TotePackages", totePackages.getTote())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseTotePackages> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseTotePackages.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("is currently Locked")) {
                throw new ToteLockedException(totePackages.getTote());
            } else if (e.getMessage() != null && e.getMessage().contains("is not in file")) {
                throw new InvalidToteException();
            } else if (e.getMessage() != null && e.getMessage().contains("Invoice number provided")) {
                throw new InvalidInvoiceException();
            }
            throw e;
        }
        return response.getBody();
    }

    public CustomerSearchResponseDTO getCustomerSearch(CustomerSearchRequestDTO request) throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<WarehouseCloseTask> eclipseRequest = new HttpEntity<>(httpHeaders);
        CustomerSearchRequestDTO.validate(request);
        MultiValueMap<String, String> queryParams = populateCustomerSearchParams(request);
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Customers")
            .queryParams(queryParams)
            .encode()
            .build()
            .toUriString();

        ResponseEntity<CustomerSearchResponseDTO> response;
        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, CustomerSearchResponseDTO.class);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    /**
     * Gets a customer by id from eclipse
     * @param customerId
     * @return a customer object
     * @throws EclipseTokenException
     */
    public Customer getCustomerById(String customerId) throws EclipseTokenException, CustomerNotFoundException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Customers", customerId)
            .encode()
            .toUriString();

        ResponseEntity<Customer> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, Customer.class);
        } catch (HttpClientErrorException e) {
            throw new CustomerNotFoundException(customerId);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    /**
     * Gets a territory by id from eclipse
     * @param territoryId
     * @return a territory entity with list of branches that belong to that branch
     * @throws EclipseTokenException
     */
    public Territory getTerritoryById(String territoryId) throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Territories", territoryId)
            .encode()
            .toUriString();

        ResponseEntity<Territory> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, Territory.class);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    private MultiValueMap<String, String> populateCustomerSearchParams(CustomerSearchRequestDTO request) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (request.getKeyword() != null && !request.getKeyword().equals("")) {
            queryParams.add("keyword", request.getKeyword().replace(' ', '+'));
        }
        if (request.getId() != null && request.getId().size() > 0) {
            for (String id : request.getId()) {
                queryParams.add("id", id);
            }
        }
        if (request.getIncludeTotalItems() != null) {
            queryParams.add("includeTotalItems", request.getIncludeTotalItems().toString());
        }
        if (request.getStartIndex() != null) {
            queryParams.add("startIndex", request.getStartIndex().toString());
        }
        if (request.getBranchId() != null) {
            queryParams.add("branchId", request.getBranchId());
        }
        if (request.getPageSize() != null) {
            queryParams.add("pageSize", request.getPageSize().toString());
        }
        if (request.getInsideSalesRep() != null) {
            queryParams.add("insideSalesRep", request.getInsideSalesRep());
        }
        if (request.getOutsideSalesRep() != null) {
            queryParams.add("outsideSalesRep", request.getOutsideSalesRep());
        }
        if (request.getTerritoryId() != null) {
            queryParams.add("territoryId", request.getTerritoryId());
        }
        if (request.getType() != null) {
            queryParams.add("Type", request.getType());
        }
        if (request.getUpdatedAfter() != null) {
            queryParams.add(
                "updatedAfter",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(request.getUpdatedAfter())
            );
        }
        return queryParams;
    }
}
