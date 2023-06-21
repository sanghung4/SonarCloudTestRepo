package com.reece.platform.products.service;

import static com.reece.platform.products.external.mincron.MincronServiceClient.MAX_RETRIES;
import static com.reece.platform.products.external.mincron.MincronServiceClient.SQL_STATE_CONNECTION_FAILURE_CODE;

import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.exceptions.MincronException;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.eclipse.PreviouslyPurchasedProduct.PreviouslyPurchasedEclipseProductResponse;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ErpService {

    @Value("${eclipse_service_url}")
    private String eclipseServiceUrl;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    @Autowired
    public ErpService(RestTemplate rt) {
        this.restTemplate = rt;
    }

    private final RestTemplate restTemplate;

    /**
     * Retrieve product data information from Eclipse given the product id
     *
     * @param productId product to retrieve information for
     * @param erpUserInformation user login and erp account id to retrieve product pricing and availability
     * @return product data response
     */
    public ProductResponse getEclipseProductData(
        String productId,
        ErpUserInformation erpUserInformation,
        Boolean isEmployee
    ) {
        val url = eclipseServiceUrl + "/product/" + productId + "?isEmployee=" + isEmployee.toString();
        return restTemplate.postForEntity(url, erpUserInformation, ProductResponse.class).getBody();
    }

    /**
     * Retrieve product data information from Eclipse given the product id
     *
     * @param productIds products to retrieve information for
     * @param erpUserInformation user login and erp account id to retrieve product pricing and availability
     * @return product data response
     */
    public ProductResponse getEclipseProductData(
        List<String> productIds,
        ErpUserInformation erpUserInformation,
        Boolean isEmployee
    ) {
        val url = UriComponentsBuilder
            .fromHttpUrl(eclipseServiceUrl)
            .path("/products")
            .query("productIds={productIds}&isEmployee={isEmployee}")
            .buildAndExpand(String.join(",", productIds), isEmployee)
            .toUriString();
        try {
            return restTemplate.postForEntity(url, erpUserInformation, ProductResponse.class).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<ProductResponse> getEclipseProductDataAsync(
        List<String> productIds,
        ErpUserInformation erpUserInformation,
        Boolean isEmployee
    ) {
        try {
            var response = getEclipseProductData(productIds, erpUserInformation, isEmployee);
            return CompletableFuture.completedFuture(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }

    /**
     * Gets the paginated previously purchased products from Eclipse
     * @param erpAccountId the account id in the erp aka "12345" NOT a UUID
     * @param currentPage indexed from 0
     * @param pageSize number of items on the page
     * @return a paginated response of the previously purchased products
     * @throws EclipseException
     */
    public PreviouslyPurchasedEclipseProductResponse getPreviouslyPurchasedProductsFromEclipse(
        String erpAccountId,
        Integer currentPage,
        Integer pageSize
    ) throws EclipseException {
        String url = UriComponentsBuilder
            .fromHttpUrl(
                String.format(
                    "%s/account/%s/previouslyPurchasedProducts?currentPage=%s&pageSize=%s",
                    eclipseServiceUrl,
                    erpAccountId,
                    currentPage,
                    pageSize
                )
            )
            .build()
            .toUriString();

        return restTemplate.getForEntity(url, PreviouslyPurchasedEclipseProductResponse.class).getBody();
    }

    /**
     * Submit a sales order to Eclipse service with given sales order information
     *
     * @param salesOrderDTO sales order information to submit to Eclipse
     * @return order response DTO
     */
    public GetOrderResponseDTO submitSalesOrder(CreateSalesOrderRequestDTO salesOrderDTO) {
        return restTemplate
            .postForEntity(eclipseServiceUrl + "/orders/", salesOrderDTO, GetOrderResponseDTO.class)
            .getBody();
    }

    /**
     * Sends a quote to Eclipse to duplicate as an actual order
     *
     * @param approveQuoteDTO sales order information to submit to Eclipse
     * @return order response DTO
     */
    public GetOrderResponseDTO approveQuote(ApproveQuoteDTO approveQuoteDTO) {
        return restTemplate
            .postForEntity(eclipseServiceUrl + "/quotes/approve", approveQuoteDTO, GetOrderResponseDTO.class)
            .getBody();
    }

    /**
     * Submit a sales order preview to Eclipse service with given sales order information
     *
     * @param salesOrderDTO sales order information to submit to Eclipse
     * @return order response DTO
     */
    public GetOrderResponseDTO submitSalesOrderPreview(CreateSalesOrderRequestDTO salesOrderDTO) {
        return restTemplate
            .postForEntity(eclipseServiceUrl + "/orders/preview", salesOrderDTO, GetOrderResponseDTO.class)
            .getBody();
    }

    /**
     * Fetch invoices for given account in Eclipse ERP
     *
     * @param accountId account to fetch invoices for
     * @return list of invoice DTOs
     */
    public GetInvoiceResponseDTO getEclipseInvoices(
        String accountId,
        String shipTo,
        Date startDate,
        Date endDate,
        String invoiceStatus
    ) throws HttpClientErrorException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("accountId", accountId);
        var df = new SimpleDateFormat("MM/dd/yy");
        if (shipTo != null) {
            queryParams.add("shipTo", shipTo);
        }
        if (invoiceStatus != null) {
            queryParams.add("invoiceStatus", invoiceStatus);
        }
        if (startDate != null) {
            queryParams.add("startDate", df.format(startDate));
        }
        if (endDate != null) {
            queryParams.add("endDate", df.format(endDate));
        }

        URI uri = UriComponentsBuilder
            .fromUriString(eclipseServiceUrl + "/accounts/" + accountId + "/invoices")
            .buildAndExpand(queryParams)
            .toUri();
        uri = UriComponentsBuilder.fromUri(uri).queryParams(queryParams).build().toUri();

        return restTemplate.getForEntity(uri.toString(), GetInvoiceResponseDTO.class, queryParams).getBody();
    }

    /**
     * Fetch invoices for given account in Mincron ERP
     *
     * @param accountId account to fetch invoices for
     * @return list of invoice DTOs
     */
    public List<InvoiceDTO> getMincronInvoices(String accountId) throws HttpClientErrorException {
        ResponseEntity<List<InvoiceDTO>> invoiceListResponse = null;
        Exception sqlException = null;
        for (int retryCount = 0; retryCount < MAX_RETRIES; retryCount++) {
            try {
                invoiceListResponse =
                    restTemplate.exchange(
                        String.format("%s/%s/%s", mincronServiceUrl, "invoices", "?accountId=" + accountId),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                    );
                break;
            } catch (Exception e) {
                if (e.getMessage().contains(SQL_STATE_CONNECTION_FAILURE_CODE)) {
                    sqlException = e;
                    continue;
                }
                throw new MincronException(e.getMessage());
            }
        }
        if (invoiceListResponse == null) throw new MincronException(sqlException.getMessage());
        return invoiceListResponse.getBody();
    }

    /**
     * Fetch Territory by Id
     * @param territoryId
     * @return
     * @throws HttpClientErrorException
     */
    public GetTerritoryResponseDTO getTerritoryById(String territoryId) throws HttpClientErrorException {
        return restTemplate
            .getForEntity(eclipseServiceUrl + "/territories/" + territoryId, GetTerritoryResponseDTO.class)
            .getBody();
    }

    @Async("taskExecutor")
    public CompletableFuture<ProductPricingResponseDTO> getProductPricingAsync(
        String customerId,
        String branchId,
        List<String> productIds
    ) {
        try {
            var response = getProductPricing(customerId, branchId, productIds);
            return CompletableFuture.completedFuture(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }

    /**
     * Get Product Pricing from Eclipse
     * @param customerId
     * @param branchId
     * @param productIds
     * @return
     */
    public ProductPricingResponseDTO getProductPricing(String customerId, String branchId, List<String> productIds) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("customerId", customerId);
        queryParams.add("branchId", branchId);
        queryParams.add("productIds", String.join(",", productIds));
        queryParams.add("useCache", "true");

        URI uri = UriComponentsBuilder
            .fromUriString(eclipseServiceUrl + "/products/price")
            .buildAndExpand(queryParams)
            .toUri();
        uri = UriComponentsBuilder.fromUri(uri).queryParams(queryParams).build().toUri();

        try {
            return restTemplate.getForEntity(uri.toString(), ProductPricingResponseDTO.class, queryParams).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }

    /**
     * Get Product Inventory at Branches
     * @param productId
     * @return
     */
    public ProductInventoryResponseDTO getProductInventory(String productId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("productId", productId);
        queryParams.add("useCache", "true");

        URI uri = UriComponentsBuilder
            .fromUriString(eclipseServiceUrl + "/products/inventory")
            .buildAndExpand(queryParams)
            .toUri();
        uri = UriComponentsBuilder.fromUri(uri).queryParams(queryParams).build().toUri();

        try {
            return restTemplate.getForEntity(uri.toString(), ProductInventoryResponseDTO.class, queryParams).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new EclipseException(e.getMessage(), e.getStatusCode());
        }
    }
}
