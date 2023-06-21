package com.reece.platform.eclipse.service.Kourier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.external.ec.EclipseErrorDTO;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.DTO.ProductPriceResponseDTO;
import com.reece.platform.eclipse.model.DTO.kourier.*;
import com.reece.platform.eclipse.model.DTO.kourier.ProductSearchResponseDTO;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.exceptions.*;

import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class KourierService {

    @Value("${kourier_username}")
    private String kourierUsername;

    @Value("${kourier_password}")
    private String kourierPassword;

    @Value("${kourier_api_endpoint}")
    private String kourierApiEndpoint;

    @Value("${splitqty.validateSerialNums:false}")
    private boolean validateSerialNums;


    @Autowired
    private final EclipseService eclipseService;

    @Autowired
    @Qualifier("json")
    private final RestTemplate restTemplate;

    public VarianceSummaryDTO getVarianceSummary(String countId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("erpCountID", countId);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/PHYSICAL/VARSUM")
                .queryParams(queryParams)
                .encode()
                .toUriString();
        ResponseEntity<VarianceSummaryDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, VarianceSummaryDTO.class);
        } catch (Exception e) {
            throw e;
        }
        return response.getBody();
    }

    public VarianceDetailsDTO getVarianceDetails(String countId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("erpCountID", countId);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/PHYSICAL/VARIANCE")
                .queryParams(queryParams)
                .encode()
                .toUriString();
        ResponseEntity<VarianceDetailsDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, VarianceDetailsDTO.class);
        } catch (Exception e) {
            throw e;
        }
        return response.getBody();
    }

    public ProductPriceResponseDTO getProductPrice(String productId, String branch, String customerId,
                                                   String userId, Date effectiveDate, String correlationId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("productId", productId);
        queryParams.add("erpBranchNum", branch);
        queryParams.add("customerId", customerId);
        queryParams.add("userId", userId);
        queryParams.add("effectiveDate", ObjectUtils.isNotEmpty(effectiveDate) ? effectiveDate.toString() : "");
        queryParams.add("correlationId", correlationId);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/PRODUCT/PRODPRICE")
                .queryParams(queryParams)
                .encode()
                .toUriString();
        ResponseEntity<ProductPriceResponseDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, ProductPriceResponseDTO.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Exception occured in Eclipse getProductPrice():" + e);
            return null;
        }
    }


    public AccountInquiryResponseDTO getInvoices(
            String accountId,
            String shipTo,
            Date startDate,
            Date endDate,
            String invoiceStatus
    ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/MAX/INVHIST")
                .queryParams(populateInvoiceSearchParams(
                        accountId,
                        shipTo,
                        startDate,
                        endDate,
                        invoiceStatus))
                .encode()
                .toUriString();

        ResponseEntity<InvoiceWrapperDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, InvoiceWrapperDTO.class);
            return new AccountInquiryResponseDTO(response.getBody().getInvoices().get(0));
        } catch (Exception e) {
            log.error("Exception occured in Kourier getInvoices:" + e);
            return null;
        }
    }

    private MultiValueMap<String, String> populateInvoiceSearchParams(
            String accountId,
            String shipTo,
            Date startDate,
            Date endDate,
            String invoiceStatus
    ) {
        var df = new SimpleDateFormat("MM/dd/yy");
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (shipTo != null) {
            queryParams.add("customerId", shipTo);
        } else {
            queryParams.add("customerId", accountId);
        }
        if (invoiceStatus != null) {
            queryParams.add("invoiceStatus", invoiceStatus);
        }
        if (startDate != null) {
            queryParams.add("begDate", df.format(startDate));
        }
        if (endDate != null) {
            queryParams.add("endDate", df.format(endDate));
        }
        return queryParams;
    }


    public SplitQuantityResponseDTO splitQuantity(
            SplitQuantityRequestDTO splitQuantityRequest
    ) throws KourierException, SplitQuantityException, EclipseTokenException, InvalidSerializedProductException {

        if (validateSerialNums && (splitQuantityRequest.getSerialNumbers() != null) &&
                (splitQuantityRequest.getSerialNumbers().getSerial() != null) &&
                !splitQuantityRequest.getSerialNumbers().getSerial().isEmpty()
        ) {
            boolean allSerialsValid = validateSerialNumbers(splitQuantityRequest.getProduct().getWarehouseId(),
                    splitQuantityRequest.getProduct().getProductId(),
                    splitQuantityRequest.getSerialNumbers().getSerial());
            if (!allSerialsValid) {
                throw new SplitQuantityException("Invalid Serial numbers found!");
            }
        }

        SplitTaskRequestDTO splitTaskRequest = convertSplitQtyToKourierRequest(splitQuantityRequest);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/WHS/SPLIT")
                .encode()
                .toUriString();
        ResponseEntity<SplitTaskResponseDTO> response;
        try {
            HttpEntity<SplitTaskRequestDTO> request = new HttpEntity<>(splitTaskRequest, httpHeaders);
            response = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, SplitTaskResponseDTO.class);
            SplitQuantityResponseDTO splitQuantityResponseDTO = convertKourierResponseToSplitQty(response.getBody());
            return splitQuantityResponseDTO;
        } catch (RestClientException e) {
            throw new KourierException(String.format("Exception calling SplitQuantity: %s", e.getMessage()));
        }
    }

    private SplitQuantityResponseDTO convertKourierResponseToSplitQty(
            SplitTaskResponseDTO splitTaskResponseDTO
    ) throws SplitQuantityException {
        if ((splitTaskResponseDTO == null) ||
                (splitTaskResponseDTO.getTrans() == null) ||
                splitTaskResponseDTO.getTrans().isEmpty()) {
            throw new SplitQuantityException("Invalid Kourier response for SplitQuantity");
        }

        SplitTaskResponse splitTaskResponse = splitTaskResponseDTO.getTrans().get(0);
        SplitQuantityResponseDTO splitQuantityResponseDTO = new SplitQuantityResponseDTO();
        splitQuantityResponseDTO.setProductId(splitTaskResponse.getProductId());
        splitQuantityResponseDTO.setOrderId(splitTaskResponse.getOrderId());
        splitQuantityResponseDTO.setIsSplit(splitTaskResponse.getIsSplit());
        if (!splitTaskResponse.getInvalidSerialNums().isEmpty()) {
            splitQuantityResponseDTO.setInvalidSerialNums(new SerialListDTO(1, splitTaskResponse.getInvalidSerialNums()));
        }
        splitQuantityResponseDTO.setSuccessStatus(splitTaskResponse.getSuccessStatus());

        splitQuantityResponseDTO.setErrorMessage(splitTaskResponse.getErrorMessage());
        splitQuantityResponseDTO.setErrorCode(splitTaskResponse.getErrorCode());
        return splitQuantityResponseDTO;
    }

    private SplitTaskRequestDTO convertSplitQtyToKourierRequest(
            SplitQuantityRequestDTO splitQuantityRequestDTO
    ) throws SplitQuantityException {
        SplitTaskRequest splitTaskRequest = new SplitTaskRequest();
        CompletePickInputDTO completePickInputDTO = splitQuantityRequestDTO.getProduct();

        if (splitQuantityRequestDTO.getPickedItemsCount() == null) {
            throw new SplitQuantityException("Missing PickedItemsCount in SplitQuantity request");
        }
        splitTaskRequest.setQty(splitQuantityRequestDTO.getPickedItemsCount());

        if (completePickInputDTO.getOrderId() == null) {
            throw new SplitQuantityException("Missing OrderId in SplitQuantity request");
        }
        splitTaskRequest.setOrderId(completePickInputDTO.getOrderId());

        if (completePickInputDTO.getProductId() == null) {
            throw new SplitQuantityException("Missing ProductId in SplitQuantity request");
        }
        splitTaskRequest.setProductId(completePickInputDTO.getProductId());

        if (completePickInputDTO.getLocation() == null) {
            throw new SplitQuantityException("Missing Location in SplitQuantity request");
        }
        splitTaskRequest.setLocation(completePickInputDTO.getLocation());

        if (completePickInputDTO.getUserId() == null) {
            throw new SplitQuantityException("Missing PickerId/UserId in SplitQuantity request");
        }
        splitTaskRequest.setPickerId(completePickInputDTO.getUserId());

        if (completePickInputDTO.getTote() == null) {
            throw new SplitQuantityException("Missing ToteId in SplitQuantity request");
        }
        splitTaskRequest.setToteId(completePickInputDTO.getTote());

        if ((splitQuantityRequestDTO.getSerialNumbers() != null) &&
                (splitQuantityRequestDTO.getSerialNumbers().getSerial() != null) &&
                !splitQuantityRequestDTO.getSerialNumbers().getSerial().isEmpty()) {
            splitTaskRequest.setSerialNums(splitQuantityRequestDTO.getSerialNumbers().getSerial());
        }

        SplitTaskRequestDTO splitTaskRequestDTO = new SplitTaskRequestDTO();
        splitTaskRequestDTO.setSplitTask(Collections.singletonList(splitTaskRequest));
        return splitTaskRequestDTO;
    }

    private boolean validateSerialNumbers(
            String warehouseId,
            String productId,
            String serialNumbers
    ) throws EclipseTokenException, InvalidSerializedProductException {
        ProductSerialNumbersResponseDTO productSerialNumbersResponseDTO =
                eclipseService.getSerialNumbers(warehouseId);

        List<ProductSerialNumberDTO> productSerialNumberDTOList = productSerialNumbersResponseDTO.getResults();
        Optional<ProductSerialNumberDTO> productSerialNumberDTO = productSerialNumberDTOList.stream()
                .filter(p -> p.equals(productId))
                .findFirst();

        Set<String> validSerials = new HashSet<>();
        if (productSerialNumberDTO.isPresent()) {
            productSerialNumberDTO.get().getSerialList().forEach(sl -> {
                validSerials.add(sl.getSerial());
            });
        }

        if (!validSerials.isEmpty()) {
            String[] requestSerialArr = serialNumbers.split(",");
            List<String> invalidSerials = new ArrayList<>();
            for (String str : requestSerialArr) {
                if (!validSerials.contains(str)) {
                    invalidSerials.add(str);
                }
            }

            if (!invalidSerials.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public CloseOrderResponseDTO closeOrder(
            CloseOrderRequestDTO closeOrderRequestDTO
    ) throws KourierException, CloseOrderException {
        CloseOrderRequest closeOrderRequest = convertCloseOrderToKourierRequest(closeOrderRequestDTO);
        log.info("Close Task Request Packet:{}", closeOrderRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/WHS/CLOSE")
                .encode()
                .toUriString();
        ResponseEntity<CloseOrderResponseDTO> response;
        try {
            HttpEntity<CloseOrderRequest> request = new HttpEntity<>(closeOrderRequest, httpHeaders);
            response = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, CloseOrderResponseDTO.class);
            log.info("Close Order Response Packet:{}", response);
            if (ObjectUtils.isNotEmpty(response)) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (RestClientException e) {
            throw new KourierException(String.format("Exception calling Kourier closeOrder API for orderId %s with errorMessage: %s", closeOrderRequestDTO.getOrderId(), e.getMessage()));
        }
    }

    private CloseOrderRequest convertCloseOrderToKourierRequest(CloseOrderRequestDTO closeOrderRequestDTO) throws CloseOrderException {
        CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
        CloseOrder closeOrder = new CloseOrder();
        if (Objects.nonNull(closeOrderRequestDTO)) {
            if (StringUtils.isEmpty(closeOrderRequestDTO.getOrderId())) {
                throw new CloseOrderException("Missing orderId in CloseOrder request");
            }
            closeOrder.setOrderId(closeOrderRequestDTO.getOrderId());
            if (StringUtils.isEmpty(closeOrderRequestDTO.getPickerId())) {
                throw new CloseOrderException("Missing pickerId in CloseOrder request");
            }
            closeOrder.setPickerId(closeOrderRequestDTO.getPickerId());
            closeOrderRequest.setCloseOrder(closeOrder);
        }
        return closeOrderRequest;
    }

    @SneakyThrows
    private HttpClientErrorException handleException(HttpClientErrorException e) {
        val objectMapper = new ObjectMapper();
        val eclipseError = objectMapper.readValue(e.getResponseBodyAsByteArray(), EclipseErrorDTO.class);

        switch (eclipseError.getCode()) {
            case "INVALID_ECLIPSE_CREDENTIALS":
                throw new InvalidEclipseCredentialsException();
            case "NO_ECLIPSE_CREDENTIALS":
                throw new NoEclipseCredentialsException();
            case "INVALID_SERIALIZED_PRODUCT":
                throw new InvalidSerializedProductException();
            default:
                throw new EclipseConnectException(eclipseError.getMessage());
        }
    }

    /**
     * Query Kourier API for product search results
     *
     * @Param keywords
     */

    public ProductSearchResponseDTO getProductSearch(String keywords, String displayName)
            throws UnsupportedEncodingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("keywords", URLEncoder.encode(keywords, "UTF-8"));
        queryParams.add("displayName", displayName);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("ARS/PRODUCT/SEARCH")
                .queryParams(queryParams)
                .encode()
                .toUriString();

        String urlTemplateEncoded= urlTemplate.replace("%252F","/");
        ResponseEntity<ProductSearchResponseDTO> response;
        try {
            response = restTemplate.exchange(urlTemplateEncoded, HttpMethod.GET, request, ProductSearchResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public CustomersPriceDTO getProductPrice(ProductPricingAndAvailabilityRequestDTO requestParams) throws UnsupportedEncodingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        String useCache = requestParams.isUseCache() ? "Y" : "N";

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("CustomerID", requestParams.getCustomerId());
        queryParams.add("Branch", requestParams.getBranchId());
        queryParams.add("ProductID", String.join(",", requestParams.getProductIds()));
        queryParams.add("IncludeInventory", "Y");
        queryParams.add("UseOHCache", useCache);

        // Controls what fields are returned in the respond. Can add more in the future if needed.
        queryParams.add("Fields", "CustomerId,Branch,ProductID,CatalogId,SellPrice,OrderUOM,OrderPerQty,TotalAvailableQty,BranchAvailableQty");

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("KAPI/PRODUCTS/PRICE")
                .queryParams(queryParams)
                .encode()
                .toUriString();
        ResponseEntity<com.reece.platform.eclipse.model.DTO.kourier.ProductPriceResponseDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, com.reece.platform.eclipse.model.DTO.kourier.ProductPriceResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody().getCustomersPrice();
    }

    public ProductInventoryDTO getProductInventory(ProductInventoryRequestDTO requestParams) throws UnsupportedEncodingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        String useCache = requestParams.isUseCache() ? "Y" : "N";

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("ProductID", requestParams.getProductId());
        queryParams.add("WithInventory", "Y");
        queryParams.add("UseOHCache", useCache);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(kourierApiEndpoint)
                .path("KAPI/PRODUCTS/ONHAND")
                .queryParams(queryParams)
                .encode()
                .toUriString();

        ResponseEntity<ProductInventoryResponseDTO> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, ProductInventoryResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }

        return response.getBody().getRecord().get(0);
    }
}



