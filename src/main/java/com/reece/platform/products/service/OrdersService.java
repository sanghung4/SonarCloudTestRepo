package com.reece.platform.products.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.external.appsearch.model.PageRequest;
import com.reece.platform.products.external.mincron.MincronServiceClient;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.PreviouslyPurchasedEclipseProductResponse;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.OrderStatus;
import com.reece.platform.products.model.entity.QuoteStatus;
import com.reece.platform.products.model.entity.User;
import com.reece.platform.products.model.repository.OrderStatusDAO;
import com.reece.platform.products.model.repository.QuoteStatusDAO;
import com.reece.platform.products.model.repository.UserDAO;
import com.reece.platform.products.orders.model.WebStatus;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.utilities.FeaturesUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@Transactional(readOnly = true)
public class OrdersService {

    @Value("${eclipse_service_url}")
    private String eclipseServiceUrl;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    public static final String SQL_STATE_CONNECTION_FAILURE_CODE = "08S01";
    public static final int MAX_RETRIES = 4;
    private static final int PAGE_SIZE = 10;
    private static final int MAX_ALLOWABLE_ORDERS = 15000;

    private final MincronServiceClient mincronServiceClient;

    private final SearchService searchService;

    private final BranchesService branchesService;

    @Autowired
    public OrdersService(
        RestTemplate rt,
        ProductService productService,
        ErpService erpService,
        OrderStatusDAO orderStatusDAO,
        QuoteStatusDAO quoteStatusDAO,
        NotificationService notificationService,
        CartService cartService,
        AccountService accountService,
        MincronServiceClient mincronServiceClient,
        SearchService searchService,
        BranchesService branchesService
    ) {
        this.restTemplate = rt;
        this.productService = productService;
        this.erpService = erpService;
        this.orderStatusDAO = orderStatusDAO;
        this.quoteStatusDAO = quoteStatusDAO;
        this.notificationService = notificationService;
        this.cartService = cartService;
        this.accountService = accountService;
        this.mincronServiceClient = mincronServiceClient;
        this.searchService = searchService;
        this.branchesService = branchesService;
    }

    private final RestTemplate restTemplate;
    private final ProductService productService;
    private final ErpService erpService;
    private final OrderStatusDAO orderStatusDAO;
    private final QuoteStatusDAO quoteStatusDAO;
    private final NotificationService notificationService;
    private final CartService cartService;
    private final AccountService accountService;

    public List<GetOrderResponseDTO> getQuotesByAccountId(
        String accountId,
        String orderPostStartDate,
        String orderPostEndDate,
        Integer page
    ) throws EclipseException {
        ResponseEntity<GetOrderResponseDTO[]> res;

        if (orderPostStartDate == null || orderPostEndDate == null) {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            orderPostEndDate = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, -30);
            orderPostStartDate = dateFormat.format(calendar.getTime());
        }

        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/accounts/%s/quotes/", eclipseServiceUrl, accountId))
            .queryParam("orderPostStartDate", orderPostStartDate)
            .queryParam("orderPostEndDate", orderPostEndDate)
            .queryParam("page", page)
            .build()
            .toUriString();

        try {
            res = restTemplate.getForEntity(url, GetOrderResponseDTO[].class);
        } catch (HttpClientErrorException e) {
            throw new EclipseException("Unable to retrieve information from Eclipse", HttpStatus.NOT_FOUND);
        }

        var quotes = res.getBody();

        var quoteIds = Arrays.stream(quotes).map(GetOrderResponseDTO::getOrderNumber).collect(Collectors.toList());
        var userEditedQuotes = quoteStatusDAO
            .findAllById(quoteIds)
            .stream()
            .map(QuoteStatus::getQuoteId)
            .collect(Collectors.toList());

        return Arrays
            .stream(quotes)
            .filter(q -> !userEditedQuotes.contains(q.getOrderNumber()))
            .collect(Collectors.toList());
    }

    @Transactional
    public GetOrderResponseDTO approveQuote(ApproveQuoteDTO approveQuoteDTO, String authorization) {
        var order = erpService.approveQuote(approveQuoteDTO);

        // Flag as approved
        var status = new QuoteStatus(approveQuoteDTO.getQuoteId(), QuoteStatusEnum.ACCEPTED);
        quoteStatusDAO.save(status);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(order.getOrderNumber());
        orderStatus.setWebStatus(WebStatus.valueOf(order.getWebStatus()));
        orderStatus.setErpAccountId(approveQuoteDTO.getBillToEntityId());

        UUID ecommShipToId = accountService.getEcommShipToId(
            approveQuoteDTO.getShipToEntityId(),
            ErpEnum.ECLIPSE.name()
        );

        orderStatus.setShipToId(ecommShipToId);
        orderStatusDAO.save(orderStatus);

        return order;
    }

    /**
     * Rejects a quote by saving a quote status of REJECTED in the db
     * @param quoteId
     * @return QuoteStatus
     */
    @Transactional
    public QuoteStatus rejectQuote(String accountId, String quoteId, ErpUserInformation erpUserInformation)
        throws EclipseException, ElasticsearchException, JsonProcessingException {
        // Fetch the order first to make sure the user has permission to view/edit
        getOrderById(accountId, quoteId, "001", erpUserInformation, null);
        var quoteStatus = new QuoteStatus(quoteId, QuoteStatusEnum.REJECTED);
        return quoteStatusDAO.save(quoteStatus);
    }

    public OrdersResponseDTO getOrdersByAccountId(
        String erpAccountId,
        String orderPostStartDate,
        String orderPostEndDate,
        String erpName
    ) throws EclipseException, MincronException, ParseException {
        GetOrderResponseDTO[] orders;
        OrdersResponseDTO ordersResponseDTO = new OrdersResponseDTO();

        boolean isEclipse = erpName.equals(ErpEnum.ECLIPSE.name());
        String url;
        if (isEclipse) {
            url =
                UriComponentsBuilder
                    .fromHttpUrl(String.format("%s/accounts/%s/orders/", eclipseServiceUrl, erpAccountId))
                    .queryParam("orderPostStartDate", orderPostStartDate)
                    .queryParam("orderPostEndDate", orderPostEndDate)
                    .build()
                    .toUriString();
        } else {
            url = getMincronOrdersListRequestUrl(orderPostStartDate, orderPostEndDate, erpAccountId);
        }
        if (!isEclipse && !FeaturesUtil.isWaterworksEnabled(accountService.getFeatures())) {
            // When account is Waterworks and WATERWORKS feature is DISABLED, don't make any ERP requests and return empty orders list.
            return ordersResponseDTO;
        }

        if (isEclipse) {
            try {
                orders = restTemplate.getForEntity(url, GetOrderResponseDTO[].class).getBody();
            } catch (HttpClientErrorException e) {
                throw new EclipseException("Unable to retrieve information from Eclipse", HttpStatus.NOT_FOUND);
            }
        } else {
            try {
                PageDTO<GetOrderResponseMincronDTO> orderResponseMincronBody;
                ResponseEntity<PageDTO<GetOrderResponseMincronDTO>> orderResponseMincron = null;
                Exception sqlException = null;
                for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
                    try {
                        orderResponseMincron =
                            restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
                        break;
                    } catch (Exception e) {
                        if (e.getMessage().contains(SQL_STATE_CONNECTION_FAILURE_CODE)) {
                            sqlException = e;
                            continue;
                        }
                        throw new MincronException(e.getMessage());
                    }
                }
                if (orderResponseMincron == null) throw new MincronException(sqlException.getMessage());
                orderResponseMincronBody = orderResponseMincron.getBody();
                List<GetOrderResponseDTO> orderDtoList = new ArrayList<>();
                for (GetOrderResponseMincronDTO getOrderResponseMincronDTO : orderResponseMincronBody.getResults()) {
                    GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO(getOrderResponseMincronDTO);
                    orderDtoList.add(getOrderResponseDTO);
                }
                orders = orderDtoList.toArray(new GetOrderResponseDTO[0]);
                PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
                paginationResponseDTO.setTotalItemCount(orderResponseMincronBody.getTotalRows());
                ordersResponseDTO.setPagination(paginationResponseDTO);
            } catch (HttpClientErrorException e) {
                throw new MincronException(
                    String.format("Unable to retrieve information from Mincron: %s", e.getMessage())
                );
            }
        }

        ordersResponseDTO.setOrders(Arrays.asList(orders));

        return ordersResponseDTO;
    }

    /**
     * Builds the request URl to fetch orders from Mincron
     *
     * @param orderPostStartDate start date filter
     * @param orderPostEndDate end date filter
     * @param erpAccountId account orders are associated with
     * @return String URL for Mincron orderList
     * @throws ParseException thrown when given date filter formats are incorrect
     */
    private String getMincronOrdersListRequestUrl(
        String orderPostStartDate,
        String orderPostEndDate,
        String erpAccountId
    ) throws ParseException {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat waterworksDateFormat = new SimpleDateFormat("yyyyMMdd");
        return UriComponentsBuilder
            .fromHttpUrl(String.format("%s/orders", mincronServiceUrl))
            .queryParam("orderType", "order")
            .queryParam("orderStatus", "ALL")
            .queryParam("fromDate", waterworksDateFormat.format(displayDateFormat.parse(orderPostStartDate)))
            .queryParam("toDate", waterworksDateFormat.format(displayDateFormat.parse(orderPostEndDate)))
            .queryParam("accountId", erpAccountId)
            .queryParam("maxRows", String.valueOf(MAX_ALLOWABLE_ORDERS))
            .queryParam("startRow", "1")
            .build()
            .toUriString();
    }

    public GetOrderResponseDTO getOrderById(
        String accountId,
        String orderId,
        String invoiceNumber,
        ErpUserInformation erpUserInformation,
        MincronOrderStatus mincronOrderStatus
    ) throws EclipseException, ElasticsearchException, JsonProcessingException {
        return getOrderById(accountId, orderId, invoiceNumber, erpUserInformation, mincronOrderStatus, null);
    }

    public GetOrderResponseDTO getOrderById(
        String accountId,
        String orderId,
        String invoiceNumber,
        ErpUserInformation erpUserInformation,
        MincronOrderStatus mincronOrderStatus,
        String branchId
    ) throws JsonProcessingException, ElasticsearchException, EclipseException, OrderNotFoundException {
        GetOrderResponseDTO responseDTO;

        if (!erpUserInformation.getErpSystemName().equals(ErpEnum.MINCRON.name())) {
            try {
                responseDTO =
                    restTemplate
                        .getForEntity(
                            eclipseServiceUrl +
                            "/accounts/" +
                            accountId +
                            "/orders/" +
                            orderId +
                            "?invoiceNumber=" +
                            invoiceNumber,
                            GetOrderResponseDTO.class
                        )
                        .getBody();
            } catch (HttpClientErrorException e) {
                throw new EclipseException("Unable to retrieve information from Eclipse", HttpStatus.NOT_FOUND);
            }

            List<String> productIds = responseDTO
                .getLineItems()
                .stream()
                .map(OrderLineItemResponseDTO::getErpPartNumber)
                .distinct()
                .collect(Collectors.toList());
            Map<String, ProductDTO> productDTOS = productService
                .getProductsByNumber(productIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ProductDTO::getPartNumber, p -> p));

            var eclipseProductData = erpService.getProductPricing(accountId, branchId, productIds).getProducts();

            responseDTO
                .getLineItems()
                .forEach(l -> {
                    var eclipseProduct = eclipseProductData
                        .stream()
                        .filter(ep -> ep.getProductId().equals(l.getErpPartNumber()))
                        .findFirst();
                    if (eclipseProduct.isPresent()) {
                        var product = eclipseProduct.get();
                        l.setAvailableQuantity(product.getBranchAvailableQty());
                    }

                    var product = productDTOS.get(l.getErpPartNumber());

                    if (product != null) {
                        l.setImageUrls(product.getImageUrls());
                        l.setProductName(product.getName());
                        l.setManufacturerName(product.getManufacturerName());
                        l.setManufacturerNumber(product.getManufacturerNumber());
                        l.setProductId(product.getId());
                    } else {
                        l.setStatus("NonPDW");
                    }
                });
        } else {
            Optional<OrderHeader> orderHeader = mincronServiceClient.getOrderHeader(
                orderId,
                mincronOrderStatus.getOrderType()
            );
            responseDTO =
                orderHeader
                    .map(GetOrderResponseDTO::new)
                    .orElseThrow(() -> new OrderNotFoundException(erpUserInformation.getErpAccountId(), orderId));
            if (!orderHeader.get().isDelivery()) {
                val branch = getBranch(String.valueOf(orderHeader.get().getBranchNumber()));
                responseDTO.setShipAddress(new EclipseAddressResponseDTO(branch));
            }

            val productLineItems = mincronServiceClient.getOrderItemList(
                erpUserInformation.getErpAccountId(),
                mincronOrderStatus.getOrderType(),
                orderId
            );

            List<ProductLineItem> filteredProductLineItems = setAndFilterLineComments(productLineItems);

            val customerPartNumbers = filteredProductLineItems
                .stream()
                .map(ProductLineItem::getProductNumber)
                .collect(Collectors.toList());

            val products = searchService
                .getProductsByCustomerPartNumber(
                    "mincron-ecomm-products",
                    customerPartNumbers,
                    new PageRequest(customerPartNumbers.size(), 1)
                )
                .stream()
                .collect(
                    Collectors.toMap(
                        p -> {
                            val customerNumbers = p.getCustomerPartNumber();
                            return customerNumbers.get(0);
                        },
                        p -> p
                    )
                );

            responseDTO.setLineItems(
                filteredProductLineItems
                    .stream()
                    .map(lineItem -> {
                        val product = products.get(lineItem.getProductNumber());
                        if (product == null) {
                            log.warn(
                                "No product found in search engine for productNumber: '{}'",
                                lineItem.getProductNumber()
                            );
                        }

                        return new OrderLineItemResponseDTO(product, lineItem);
                    })
                    .collect(Collectors.toList())
            );
        }
        return responseDTO;
    }

    private List<ProductLineItem> setAndFilterLineComments(List<ProductLineItem> productLineItems) {
        List<ProductLineItem> filteredLineItems = new ArrayList<>();

        ProductLineItem lastProductItem = null;
        for (ProductLineItem productLineItem : productLineItems) {
            if (productLineItem.getDisplayOnly() != null && productLineItem.getDisplayOnly().equals("Y")) {
                if (lastProductItem != null) {
                    lastProductItem.setLineComments(
                        (lastProductItem.getLineComments() == null ? "" : lastProductItem.getLineComments()) +
                        productLineItem.getDescription()
                    );
                }
            } else {
                lastProductItem = productLineItem;
                filteredLineItems.add(productLineItem);
            }
        }

        return filteredLineItems;
    }

    @Cacheable(
        value = "previously-purchased-products",
        key = "{#erpUserInformation.erpAccountId, #currentPage, #pageSize }"
    )
    public PreviouslyPurchasedProductResponseDTO getPreviouslyPurchasedProducts(
        ErpUserInformation erpUserInformation,
        Integer currentPage,
        Integer pageSize
    ) throws EclipseException, ElasticsearchException {
        PreviouslyPurchasedEclipseProductResponse previouslyPurchasedProducts;
        try {
            previouslyPurchasedProducts =
                erpService.getPreviouslyPurchasedProductsFromEclipse(
                    erpUserInformation.getErpAccountId(),
                    currentPage,
                    pageSize
                );
        } catch (HttpClientErrorException e) {
            throw new EclipseException("Unable to retrieve information from Eclipse", HttpStatus.NOT_FOUND);
        }

        var productDtos = previouslyPurchasedProducts
            .getProducts()
            .stream()
            .map(p -> {
                var productDTO = new ProductDTO();
                productDTO.setPartNumber(p.getProduct().getPartIdentifiers().getEclipsePartNumber());
                productDTO.setErp(ErpEnum.ECLIPSE.name());
                productDTO.setId(String.format("MSC-%s", p.getProduct().getPartIdentifiers().getEclipsePartNumber()));
                // TODO - tw, right now this is hardcoded to Eclipse
                return productDTO;
            })
            .collect(Collectors.toList());

        var productsWithErpInfo = productService
            .getProductDetails(productDtos)
            .stream()
            .collect(Collectors.toMap(ProductDTO::getPartNumber, Function.identity()));

        var pppProducts = previouslyPurchasedProducts
            .getProducts()
            .stream()
            .map(p -> {
                var productDto = productsWithErpInfo.get(p.getProduct().getPartIdentifiers().getEclipsePartNumber());
                var ppp = new PreviouslyPurchasedProductDTO();
                ppp.setLastOrder(p.getLastOrder());
                ppp.setQuantity(p.getQuantity());
                ppp.setProduct(productDto);
                return ppp;
            })
            .collect(Collectors.toList());

        return new PreviouslyPurchasedProductResponseDTO(pppProducts, previouslyPurchasedProducts.getPagination());
    }

    public PageDTO<ContractDTO> getContracts(
        String erpAccountId,
        String pageNumber,
        String searchFilter,
        String fromDate,
        String toDate,
        String sortOrder,
        String sortDirection
    ) throws MincronException {
        ResponseEntity<PageDTO<ContractDTO>> res = null;

        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/contracts", mincronServiceUrl))
            .queryParam("accountId", erpAccountId)
            .queryParam(
                "startRow",
                Integer.parseInt(pageNumber) > 1
                    ? String.valueOf((Integer.parseInt(pageNumber) - 1) * PAGE_SIZE + 1)
                    : "1"
            )
            .queryParam("maxRows", String.valueOf(PAGE_SIZE))
            .queryParam("searchFilter", searchFilter)
            .queryParam("fromDate", fromDate)
            .queryParam("toDate", toDate)
            .queryParam("sortOrder", sortOrder)
            .queryParam("sortDirection", sortDirection)
            .build()
            .toUriString();

        Exception sqlException = null;
        for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
            try {
                res = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
                break;
            } catch (Exception e) {
                if (e.getMessage().contains(SQL_STATE_CONNECTION_FAILURE_CODE)) {
                    sqlException = e;
                    continue;
                }
                throw new MincronException(e.getMessage());
            }
        }
        if (res == null) throw new MincronException(sqlException.getMessage());
        return res.getBody();
    }

    public Optional<ContractHeaderDTO> getContract(String erpAccountId, String contractNumber) {
        return mincronServiceClient
            .getContractHeader(contractNumber)
            .map(contractHeader -> {
                val branch = getBranch(contractHeader.getBranch().getBranchNumber());
                val dto = new ContractHeaderDTO(contractHeader, branch);
                dto.getCustomerInfo().setCustomerNumber(erpAccountId);
                return dto;
            })
            .map(contractHeader -> {
                val productLineItems = mincronServiceClient.getContractItemList(erpAccountId, contractNumber);

                val customerPartNumbers = productLineItems
                    .stream()
                    .map(ProductLineItem::getProductNumber)
                    .collect(Collectors.toList());

                val products = searchService
                    .getProductsByCustomerPartNumber(
                        "mincron-ecomm-products",
                        customerPartNumbers,
                        new PageRequest(customerPartNumbers.size(), 1)
                    )
                    .stream()
                    .collect(
                        Collectors.toMap(
                            p -> {
                                val customerNumbers = p.getCustomerPartNumber();
                                return customerNumbers.get(0);
                            },
                            p -> p
                        )
                    );

                contractHeader.setContractProducts(
                    productLineItems
                        .stream()
                        .map(lineItem -> {
                            val product = products.get(lineItem.getProductNumber());
                            if (product == null) {
                                log.warn(
                                    "No product found in search engine for productNumber: '{}'",
                                    lineItem.getProductNumber()
                                );
                            }
                            return new ContractHeaderDTO.ContractProduct(product, lineItem);
                        })
                        .collect(Collectors.toList())
                );

                return contractHeader;
            });
    }

    private BranchResponseDTO getBranch(String branchNumber) {
        BranchResponseDTO branchResponseDTO;
        try {
            /*
            TODO: potential refactor is to just get the branch record from the DB using the entity_id
             and avoid the formatMincronBranchId method altogether
               */
            branchResponseDTO = branchesService.getBranch(formatMincronBranchId(branchNumber));
        } catch (com.reece.platform.products.branches.exception.BranchNotFoundException e) {
            return new BranchResponseDTO();
        }

        return branchResponseDTO;
    }

    private static String formatMincronBranchId(String branchEntityId) {
        StringBuilder branchNumber = new StringBuilder(String.format("600%s", branchEntityId.trim()));
        while (branchNumber.length() > 4) branchNumber.deleteCharAt(1);
        return branchNumber.toString();
    }

    public ProductDetailsResponseDTO getProductDetails(ProductDetailRequestDTO productDetailRequestDTO)
        throws MincronException {
        ResponseEntity<ProductDetailsResponseDTO> res = null;

        val requestEntity = new HttpEntity<>(productDetailRequestDTO);
        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/contracts/orders/product-details", mincronServiceUrl))
            .build()
            .toUriString();

        res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ProductDetailsResponseDTO.class);

        if (res == null) throw new MincronException("Order details failed");
        return res.getBody();
    }

    public SubmitOrderReviewResponseDTO orderReview(SubmitOrderReviewRequestDTO submitOrderReviewRequest)
        throws MincronException {
        ResponseEntity<SubmitOrderReviewResponseDTO> res = null;

        val requestEntity = new HttpEntity<>(submitOrderReviewRequest);
        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/contracts/orders/review", mincronServiceUrl))
            .build()
            .toUriString();

        res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SubmitOrderReviewResponseDTO.class);

        if (res == null) throw new MincronException("Order review failed");
        return res.getBody();
    }

    public ContractCreateCartReturnTableDTO submitContractToReleaseOrder(SubmitOrderRequestDTO submitOrderRequest)
        throws JsonProcessingException, BranchNotFoundPricingAndAvailabilityException {
        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/contracts/orders/submit", mincronServiceUrl))
            .queryParam("application", submitOrderRequest.getApplication())
            .queryParam("accountId", submitOrderRequest.getAccountId())
            .queryParam("userId", "JHILLIN")
            .queryParam("shoppingCartId", submitOrderRequest.getShoppingCartId())
            .toUriString();
        System.out.println("url" + url);
        var response = restTemplate
            .postForEntity(url, submitOrderRequest, ContractCreateCartReturnTableDTO.class)
            .getBody();
        var orderID = response.getReturnTable();
        submitOrderRequest.setOrderNumber(orderID);
        submitOrderRequest.setWebStatus("PENDING");

        submitOrderRequest
            .getLineItems()
            .forEach(item ->
                item.setUnitPrice(
                    BigDecimal.valueOf(item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP).floatValue()
                )
            );

        submitOrderRequest.setErpSystemName(String.valueOf(ErpEnum.MINCRON));
        Format formatDate = new SimpleDateFormat("MM/dd/yy");
        String orderDate = formatDate.format(new Date());
        submitOrderRequest.setOrderDate(orderDate);

        // TODO: place in queue instead of doing synchronous
        notificationService.sendOrderSubmittedEmail(
            buildOrderNotificationDTO(
                submitOrderRequest.getShipToId(),
                submitOrderRequest,
                submitOrderRequest.getShipBranchNumber()
            )
        );

        return response;
    }

    public SalesOrderSubmitNotificationDTO buildOrderNotificationDTO(
        UUID shipToId,
        SubmitOrderRequestDTO submitOrderRequest,
        String branchId
    ) throws BranchNotFoundPricingAndAvailabilityException {
        BranchDTO branch;

        UserDTO user;
        try {
            user = accountService.getUser(submitOrderRequest.getUserId(), submitOrderRequest.getAuthorization());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(submitOrderRequest.getUserId());
            } else {
                throw e;
            }
        }

        try {
            if (branchId != null) {
                BranchResponseDTO branchResponse = branchesService.getBranchByEntityId(
                    branchId,
                    ErpEnum.MINCRON.name()
                );
                branch = new BranchDTO(branchResponse);
            } else {
                branch = accountService.getHomeBranch(shipToId.toString());
            }
        } catch (Exception e) {
            throw new BranchNotFoundPricingAndAvailabilityException(shipToId.toString());
        }

        BranchOrderInfoDTO branchOrderInfoDTO = buildBranchOrderInfoDTO(
            submitOrderRequest,
            branch,
            submitOrderRequest.getDeliveryMethod()
        );
        submitOrderRequest.setBranchInfo(branchOrderInfoDTO);

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = new SalesOrderSubmitNotificationDTO(
            submitOrderRequest,
            submitOrderRequest.getDeliveryMethod().equals(DeliveryMethodEnum.DELIVERY.name()),
            branch,
            user.getFirstName(),
            Arrays.asList(user.getEmail(), branch.getActingBranchManagerEmail())
        );

        return salesOrderSubmitNotificationDTO;
    }

    private BranchOrderInfoDTO buildBranchOrderInfoDTO(
        SubmitOrderRequestDTO submitOrderRequest,
        BranchDTO branch,
        String deliveryMethod
    ) {
        BranchOrderInfoDTO branchOrderInfoDTO = submitOrderRequest.getBranchInfo() == null
            ? new BranchOrderInfoDTO()
            : submitOrderRequest.getBranchInfo();
        branchOrderInfoDTO.setBranchHours(branch.getBusinessHours());
        branchOrderInfoDTO.setBranchPhone(branch.getPhone());

        if (deliveryMethod.equals(DeliveryMethodEnum.DELIVERY.name())) {
            EclipseAddressResponseDTO address = new EclipseAddressResponseDTO(submitOrderRequest.getShipToAddress());
            branchOrderInfoDTO.setStreetLineOne(address.getStreetLineOne());
            branchOrderInfoDTO.setStreetLineTwo(address.getStreetLineTwo());
            branchOrderInfoDTO.setCity(address.getCity());
            branchOrderInfoDTO.setPostalCode(address.getPostalCode());
            branchOrderInfoDTO.setState(address.getState());
        } else {
            branchOrderInfoDTO.setBranchName(branch.getName());
            branchOrderInfoDTO.setStreetLineOne(branch.getAddress1());
            branchOrderInfoDTO.setStreetLineTwo(branch.getAddress2());
            branchOrderInfoDTO.setCity(branch.getCity());
            branchOrderInfoDTO.setPostalCode(branch.getZip());
            branchOrderInfoDTO.setState(branch.getState());
        }

        return branchOrderInfoDTO;
    }

    public ContractCreateCartReturnTableDTO deleteCartItems(
        String application,
        String accountId,
        String userId,
        String shoppingCartId,
        String branchNumber
    ) {
        ResponseEntity<ContractCreateCartReturnTableDTO> res = null;

        String url = UriComponentsBuilder
            .fromHttpUrl(String.format("%s/contracts/orders/delete-cart", mincronServiceUrl))
            .queryParam("application", application)
            .queryParam("accountId", accountId)
            .queryParam("userId", userId)
            .queryParam("shoppingCartId", shoppingCartId)
            .queryParam("branchNumber", branchNumber)
            .build()
            .toUriString();

        res = restTemplate.exchange(url, HttpMethod.DELETE, null, ContractCreateCartReturnTableDTO.class);

        if (res == null) throw new MincronException("Delete failed");
        return res.getBody();
    }
}
