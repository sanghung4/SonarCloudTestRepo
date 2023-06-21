package com.reece.platform.products.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.DTO.ApproveQuoteDTO;
import com.reece.platform.products.model.DTO.GetOrderResponseDTO;
import com.reece.platform.products.model.DTO.OrdersResponseDTO;
import com.reece.platform.products.model.DTO.PreviouslyPurchasedProductResponseDTO;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.MincronOrderStatus;
import com.reece.platform.products.orders.OrderStatusService;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.OrdersService;
import com.reece.platform.products.utilities.CaseInsensitiveEnumMapper;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final AuthorizationService authorizationService;
    private final OrderStatusService orderStatusService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MincronOrderStatus.class, new CaseInsensitiveEnumMapper(MincronOrderStatus.class));
    }

    /**
     * Get quotes for account within given date range
     *
     * @param accountId account ot fetch quotes for
     * @param orderPostStartDate start date of quotes to fetch
     * @param orderPostEndDate end date of quotes to fetch
     * @param page page number for pagination
     * @return list of quotes
     * @throws EclipseException thrown when Eclipse has an error
     */
    @GetMapping("accounts/{accountId}/quotes")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<GetOrderResponseDTO> getQuotes(
        @PathVariable String accountId,
        @RequestParam String orderPostStartDate,
        @RequestParam String orderPostEndDate,
        @RequestParam(required = false) Integer page
    ) throws EclipseException {
        return ordersService.getQuotesByAccountId(accountId, orderPostStartDate, orderPostEndDate, page);
    }

    /**
     /**
     * Get order details for a single quote.
     *
     * @param accountId entity identifier
     * @param quoteId quote number
     * @param invoiceNumber order invoice number
     * @return order details if order exists
     * @throws JsonProcessingException
     * @throws ElasticsearchException
     * @throws EclipseException if call to eclipse returns a HttpClientErrorException
     */
    @GetMapping("accounts/{accountId}/quotes/{quoteId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody GetOrderResponseDTO getQuote(
        @PathVariable String accountId,
        @PathVariable String quoteId,
        @RequestParam String userId,
        @RequestParam String password,
        @RequestParam String erpAccountId,
        @RequestParam String name,
        @RequestParam String erpSystemName,
        @RequestParam(required = false) String invoiceNumber
    ) throws JsonProcessingException, ElasticsearchException, EclipseException {
        var erpUserInfo = new ErpUserInformation(userId, password, erpAccountId, name, erpSystemName, null);
        return ordersService.getOrderById(accountId, quoteId, invoiceNumber, erpUserInfo, null);
    }

    @PostMapping("accounts/{accountId}/quotes/{quoteId}/accept")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody GetOrderResponseDTO acceptQuote(
        @RequestHeader(name = "authorization") String authorization,
        @RequestBody ApproveQuoteDTO approveQuoteDTO
    ) throws UserUnauthorizedException {
        if (!authorizationService.userCanManageQuotes(authorization)) throw new UserUnauthorizedException();
        return ordersService.approveQuote(approveQuoteDTO, authorization);
    }

    @PostMapping("accounts/{accountId}/quotes/{quoteId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void rejectQuote(
        @PathVariable String accountId,
        @PathVariable String quoteId,
        @RequestBody ErpUserInformation erpUserInformation,
        @RequestHeader(name = "authorization") String authorization
    ) throws EclipseException, ElasticsearchException, JsonProcessingException, UserUnauthorizedException {
        if (!authorizationService.userCanManageQuotes(authorization)) throw new UserUnauthorizedException();
        ordersService.rejectQuote(accountId, quoteId, erpUserInformation);
    }

    @GetMapping("accounts/{erpAccountId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody OrdersResponseDTO getOrders(
        @PathVariable String erpAccountId,
        @RequestParam(required = false) String orderPostStartDate,
        @RequestParam(required = false) String orderPostEndDate,
        @RequestParam(required = false) String erpName
    ) throws EclipseException, MincronException, ParseException {
        return ordersService.getOrdersByAccountId(erpAccountId, orderPostStartDate, orderPostEndDate, erpName);
    }

    /**
     /**
     * Get order details for a single order.
     *
     * @param orderId order number
     * @param invoiceNumber order invoice number
     * @return order details if order exists
     * @throws JsonProcessingException
     * @throws ElasticsearchException
     * @throws EclipseException if call to eclipse returns a HttpClientErrorException
     */
    @GetMapping("accounts/{accountId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody GetOrderResponseDTO getOrder(
        @PathVariable String accountId,
        @PathVariable String orderId,
        @RequestParam String userId,
        @RequestParam String password,
        @RequestParam String erpAccountId,
        @RequestParam String name,
        @RequestParam String erpSystemName,
        @RequestParam(required = false) String invoiceNumber,
        @RequestParam(required = false) MincronOrderStatus orderStatus
    ) throws JsonProcessingException, ElasticsearchException, EclipseException {
        var erpUserInfo = new ErpUserInformation(userId, password, erpAccountId, name, erpSystemName, null);
        return ordersService.getOrderById(erpAccountId, orderId, invoiceNumber, erpUserInfo, orderStatus);
    }

    /**
     * If web status has changed on the order, update the e-comm database and make call to the notifications service
     * to notify customer of order status update.
     */
    @PutMapping("orders/status-update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void updateOrderStatus() {
        orderStatusService.updateAllOrderStatuses();
    }

    @GetMapping("accounts/previously-purchased-products")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody PreviouslyPurchasedProductResponseDTO getPreviouslyPurchasedProduct(
        @RequestParam Integer currentPage,
        @RequestParam Integer pageSize,
        @RequestParam String userId,
        @RequestParam String password,
        @RequestParam String erpAccountId,
        @RequestParam String name,
        @RequestParam String erpSystemName,
        @RequestParam(required = false) String customerNumber
    ) throws EclipseException, ElasticsearchException {
        var erpUserInfo = new ErpUserInformation(userId, password, erpAccountId, name, erpSystemName,customerNumber);
        var ppProductResponseDTO = ordersService.getPreviouslyPurchasedProducts(erpUserInfo, currentPage, pageSize);
        if (customerNumber != null && !customerNumber.isEmpty()) {
            try {
                ppProductResponseDTO
                    .getProducts()
                    .forEach(p ->
                        p.getProduct().setCustomerPartNumbers(p.getProduct().getCustomerPartNumbers(), customerNumber)
                    );
            } catch (Exception e) {}
        }

        return ppProductResponseDTO;
    }

    /**
     *
     * @param erpAccountId mincron bill-to erp account Id
     * @param pageNumber page number
     * @param searchFilter query string used to filter results
     * @param fromDate if specified, contracts from this date on will be returned
     * @param toDate if specified, contracts up to this date will be returned
     * @return list of contracts for given mincron bill-to erp account Id
     * @throws MincronException for any error coming from mincron service
     */
    @GetMapping(value = "accounts/{erpAccountId}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody PageDTO<ContractDTO> getContracts(
        @PathVariable String erpAccountId,
        @RequestParam(required = false) String pageNumber,
        @RequestParam(required = false) String searchFilter,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) String sortOrder,
        @RequestParam(required = false) String sortDirection
    ) throws MincronException {
        return ordersService.getContracts(
            erpAccountId,
            pageNumber,
            searchFilter,
            fromDate,
            toDate,
            sortOrder,
            sortDirection
        );
    }

    @GetMapping(
        value = "accounts/{erpAccountId}/contracts/{contractNumber}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody ContractHeaderDTO getContractHeader(
        @PathVariable String erpAccountId,
        @PathVariable String contractNumber
    ) {
        return ordersService
            .getContract(erpAccountId, contractNumber)
            .orElseThrow(() -> new ContractNotFoundException(erpAccountId, contractNumber));
    }

    /**
     * Retrieve tax information by submitting order review
     * @requestBody Needs contract details and product details
     * @return Order details for given cart and contract
     * @throws MincronException for any error coming from mincron service
     */
    @PostMapping("contracts/orderReview")
    public @ResponseBody SubmitOrderWrapperDTO submitOrderReview(
        @RequestBody SubmitOrderReviewRequestDTO submitOrderReviewRequest
    ) throws MincronException {
        SubmitOrderReviewResponseDTO orderReviewResponse = ordersService.orderReview(submitOrderReviewRequest);
        return orderReviewResponse.getReturnTable();
    }

    /**
     * Submit order from cart
     * @requestBody Needs contract details and product details
     * @return Order number
     * @throws MincronException if there is an internal server error when making API request
     */
    @PutMapping("contract-order/submit")
    public @ResponseBody String submitContractToReleaseOrder(
        @RequestBody SubmitOrderRequestDTO submitOrderRequest,
        @RequestHeader(name = "authorization") String authorization
    ) throws MincronException, JsonProcessingException, BranchNotFoundPricingAndAvailabilityException {
        submitOrderRequest.setAuthorization(authorization);
        ContractCreateCartReturnTableDTO orderNumber = ordersService.submitContractToReleaseOrder(submitOrderRequest);
        return orderNumber.getReturnTable();
    }

    @DeleteMapping("contracts/delete-cart")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteCart(
        @RequestParam String application,
        @RequestParam String accountId,
        @RequestParam String userId,
        @RequestParam String shoppingCartId,
        @RequestParam String branchNumber
    ) throws MincronException {
        ordersService.deleteCartItems(application, accountId, userId, shoppingCartId, branchNumber);
        return shoppingCartId;
    }

    /**
     * Retrieve item availablity information
     * @requestBody Needs product and branch details
     * @return item availability in given branch
     * @throws MincronException for any error coming from mincron service
     */
    @PostMapping("contracts/productDetail")
    public @ResponseBody ProductDetailWrapperDTO getProductDetails(
        @RequestBody ProductDetailRequestDTO productDetailRequestDTO
    ) throws MincronException {
        ProductDetailsResponseDTO productDetails = ordersService.getProductDetails(productDetailRequestDTO);
        return productDetails.getReturnTable();
    }
}
