package com.reece.platform.eclipse.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.dto.*;
import com.reece.platform.eclipse.dto.inventory.*;
import com.reece.platform.eclipse.dto.productsearch.EclipseAddCountDTORequest;
import com.reece.platform.eclipse.dto.productsearch.kourier.ProductKourierSearchResponseDTO;
import com.reece.platform.eclipse.enums.ApplicationType;
import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.util.StringUtil;
import com.reece.platform.eclipse.util.WMSConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class KourierService {

    @Value("${kourier_username}")
    private String kourierUsername;

    @Value("${kourier_password}")
    private String kourierPassword;

    @Value("${kourier_username_Pick}")
    private String kourierUsernamePicking;

    @Value("${kourier_password_Pick}")
    private String kourierPasswordPicking;

    @Value("${kourier_username_Pricing}")
    private String kourierUsernamePricing;

    @Value("${kourier_password_Pricing}")
    private String kourierPasswordPricing;

    @Value("${kourier_api_endpoint}")
    private String kourierApiEndpoint;

    @Value("${splitqty.validateSerialNums:false}")
    private boolean validateSerialNums;

    private final EclipseService eclipseService;

    @Autowired
    private final RestTemplate restTemplate;

    // Custom RestTemplate ObjectMapper/MessageConverter
    private MappingJackson2HttpMessageConverter createCustomMappingJacksonHttpMessageConverter() {
        ObjectMapper om = new ObjectMapper();
        // Null properties will NOT be included in the serialized JSON.
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Any extra properties in the JSON Kourier response will NOT give a parse error and cause us to fail.
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(om);
        return converter;
    }

    public KourierService(RestTemplate _restTemplate, EclipseService eclipseService) {
        restTemplate = _restTemplate;
        restTemplate.getMessageConverters().add(0, createCustomMappingJacksonHttpMessageConverter());
        restTemplate
            .getInterceptors()
            .add((request, body, execution) -> {
                ClientHttpResponse response = execution.execute(request, body);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response;
            });
        this.eclipseService = eclipseService;
    }

    public VarianceSummaryDTO getVarianceSummary(String countId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("erpCountID", countId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
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
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("erpCountID", countId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
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

    public ProductPriceResponseDTO getProductPrice(
        String productId,
        String branch,
        String customerId,
        String userId,
        Date effectiveDate,
        String correlationId
    ) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.PRICING_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("productId", productId);
        queryParams.add("erpBranchNum", branch);
        queryParams.add("customerId", customerId);
        queryParams.add("userId", userId);
        queryParams.add("effectiveDate", ObjectUtils.isNotEmpty(effectiveDate) ? effectiveDate.toString() : "");
        queryParams.add("correlationId", correlationId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path(WMSConstants.KOURIER_PRD_BASE + "PRODPRICE")
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

    /**
     * Split Quantity
     * @param splitQuantityRequest
     * @return
     * @throws KourierException
     * @throws SplitQuantityException
     * @throws EclipseTokenException
     * @throws InvalidSerializedProductException
     */
    public SplitQuantityResponseDTO splitQuantity(SplitQuantityRequestDTO splitQuantityRequest)
        throws KourierException, SplitQuantityException, EclipseTokenException, InvalidSerializedProductException {
        if (
            validateSerialNums &&
            (splitQuantityRequest.getSerialNumbers() != null) &&
            (splitQuantityRequest.getSerialNumbers().getSerial() != null) &&
            !splitQuantityRequest.getSerialNumbers().getSerial().isEmpty()
        ) {
            boolean allSerialsValid = validateSerialNumbers(
                splitQuantityRequest.getProduct().getWarehouseId(),
                splitQuantityRequest.getProduct().getProductId(),
                splitQuantityRequest.getSerialNumbers().getSerial()
            );
            if (!allSerialsValid) {
                throw new SplitQuantityException("Invalid Serial numbers found!");
            }
        }

        SplitTaskRequestDTO splitTaskRequest = convertSplitQtyToKourierRequest(splitQuantityRequest);

        var httpHeaders = buildHeaders(ApplicationType.PICKING_APP);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
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

    /**
     * Split Quantity
     * @param closeOrderRequestDTO
     * @return
     * @throws KourierException
     * @throws CloseOrderException
     */
    public CloseOrderResponseDTO closeOrder(CloseOrderRequestDTO closeOrderRequestDTO)
        throws KourierException, CloseOrderException {
        CloseOrderRequest closeOrderRequest = convertCloseOrderToKourierRequest(closeOrderRequestDTO);
        log.info("Close Task Request Packet:{}", closeOrderRequest);
        var httpHeaders = buildHeaders(ApplicationType.PICKING_APP);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
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
            throw new KourierException(
                String.format(
                    "Exception calling Kourier closeOrder API for orderId %s with errorMessage: %s",
                    closeOrderRequestDTO.getOrderId(),
                    e.getMessage()
                )
            );
        }
    }

    /**
     * ERPCP-1275 -- Start
     * Query Kourier API for product search results
     *
     * @Param keywords
     */

    public ProductKourierSearchResponseDTO getProductSearch(String keywords, String displayName)
        throws UnsupportedEncodingException {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("keywords", URLEncoder.encode(keywords, "UTF-8"));

        if (!StringUtil.hasNullorEmptyValueOrUndefined(displayName)) queryParams.add("displayName", displayName);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path(WMSConstants.KOURIER_PRD_SEARCH)
            .queryParams(queryParams)
            .encode()
            .toUriString();

        String urlTemplateEncoded = urlTemplate.replace("%252F", "/");
        ResponseEntity<ProductKourierSearchResponseDTO> response;
        try {
            response =
                restTemplate.exchange(
                    urlTemplateEncoded,
                    HttpMethod.GET,
                    request,
                    ProductKourierSearchResponseDTO.class
                );
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }

        // If no products were found, return an empty array
        response
            .getBody()
            .getProdSearch()
            .forEach(productSearch -> {
                if (productSearch.getProductIdCount() == 0) {
                    productSearch.setProducts(new ArrayList<>());
                }
            });

        return response.getBody();
    }

    @SneakyThrows
    private HttpClientErrorException handleException(HttpClientErrorException e) {
        val objectMapper = new ObjectMapper();
        val eclipseError = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorDTO.class);

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
     * ERPCP-1275 -- End
     */

    /**
     * validate serial numbers
     * @param warehouseId
     * @param productId
     * @param serialNumbers
     * @return
     * @throws EclipseTokenException
     * @throws InvalidSerializedProductException
     */
    private boolean validateSerialNumbers(String warehouseId, String productId, String serialNumbers)
        throws EclipseTokenException, InvalidSerializedProductException {
        ProductSerialNumbersResponseDTO productSerialNumbersResponseDTO = eclipseService.getSerialNumbers(warehouseId);

        List<ProductSerialNumberDTO> productSerialNumberDTOList = productSerialNumbersResponseDTO.getResults();
        Optional<ProductSerialNumberDTO> productSerialNumberDTO = productSerialNumberDTOList
            .stream()
            .filter(p -> p.equals(productId))
            .findFirst();

        Set<String> validSerials = new HashSet<>();
        if (productSerialNumberDTO.isPresent()) {
            productSerialNumberDTO
                .get()
                .getSerialList()
                .forEach(sl -> {
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

    private SplitTaskRequestDTO convertSplitQtyToKourierRequest(SplitQuantityRequestDTO splitQuantityRequestDTO)
        throws SplitQuantityException {
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

        if (
            (splitQuantityRequestDTO.getSerialNumbers() != null) &&
            (splitQuantityRequestDTO.getSerialNumbers().getSerial() != null) &&
            !splitQuantityRequestDTO.getSerialNumbers().getSerial().isEmpty()
        ) {
            splitTaskRequest.setSerialNums(splitQuantityRequestDTO.getSerialNumbers().getSerial());
        }

        SplitTaskRequestDTO splitTaskRequestDTO = new SplitTaskRequestDTO();
        splitTaskRequestDTO.setSplitTask(Collections.singletonList(splitTaskRequest));
        return splitTaskRequestDTO;
    }

    private SplitQuantityResponseDTO convertKourierResponseToSplitQty(SplitTaskResponseDTO splitTaskResponseDTO)
        throws SplitQuantityException {
        if (
            (splitTaskResponseDTO == null) ||
            (splitTaskResponseDTO.getTrans() == null) ||
            splitTaskResponseDTO.getTrans().isEmpty()
        ) {
            throw new SplitQuantityException("Invalid Kourier response for SplitQuantity");
        }

        SplitTaskResponse splitTaskResponse = splitTaskResponseDTO.getTrans().get(0);
        SplitQuantityResponseDTO splitQuantityResponseDTO = new SplitQuantityResponseDTO();
        splitQuantityResponseDTO.setProductId(splitTaskResponse.getProductId());
        splitQuantityResponseDTO.setOrderId(splitTaskResponse.getOrderId());
        splitQuantityResponseDTO.setIsSplit(splitTaskResponse.getIsSplit());
        if (!splitTaskResponse.getInvalidSerialNums().isEmpty()) {
            splitQuantityResponseDTO.setInvalidSerialNums(
                new SerialListDTO(1, splitTaskResponse.getInvalidSerialNums())
            );
        }
        splitQuantityResponseDTO.setSuccessStatus(splitTaskResponse.getSuccessStatus());

        splitQuantityResponseDTO.setErrorMessage(splitTaskResponse.getErrorMessage());
        splitQuantityResponseDTO.setErrorCode(splitTaskResponse.getErrorCode());
        return splitQuantityResponseDTO;
    }

    private CloseOrderRequest convertCloseOrderToKourierRequest(CloseOrderRequestDTO closeOrderRequestDTO)
        throws CloseOrderException {
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

    /**
     * Inventory - Get Available Counts
     * @param startDate
     * @param endDate
     * @param branchId
     * @return
     */
    public CountInfoResponseDTO getAvailableCounts(String startDate, String endDate, String branchId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (startDate != null && !startDate.isBlank()) {
            queryParams.add("begDate", startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            queryParams.add("endDate", endDate);
        }
        if (branchId != null && !branchId.isBlank()) {
            queryParams.add("erpBranchNum", branchId);
        }

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/AvailCounts")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                KourierCountInfoResponseDTO.class
            );
            var counts = response.getBody();
            return new CountInfoResponseDTO(counts);
        } catch (Exception e) {
            log.error("[EclipseService]-[Inventory]-Error fetching counts between {} and {}", startDate, endDate);
            throw e;
        }
    }

    /**
     * Inventory - Get Full Count Information
     * Includes products and locations for count
     * @param countId
     * @return
     */
    public FullCountDTO getCount(String countId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("erpCountID", countId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/Count")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, KourierFullCountDTO.class);
            return new FullCountDTO(response.getBody());
        } catch (Exception e) {
            log.error("[EclipseService]-[Inventory]-Error getting full count details for count id {}", countId);
            throw e;
        }
    }

    /**
     * Inventory - validate count
     * @param branchId
     * @param countId
     * @return
     */
    public EclipseBatchDTO validateCount(String branchId, String countId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("branch", branchId);
        queryParams.add("countID", countId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/ValidateCount")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, ValidateCountResponseDTO.class);
            var data = response.getBody();
            checkInventoryError(data.getValidateCount());
            return new EclipseBatchDTO(data);
        } catch (Exception e) {
            log.error("[EclipseService]-[Inventory]-Error validating count {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Inventory - Get All Locations
     * @param branchId
     * @param countId
     * @return
     */
    public List<String> getAllLocations(String branchId, String countId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("branch", branchId);
        queryParams.add("countID", countId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/CountLocations")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                KourierCountLocationResponseDTO.class
            );
            var data = response.getBody();
            var locations = data.getTrans().get(0);
            checkInventoryError(locations);
            return locations
                .getLocations()
                .stream()
                .map(KourierCountLocationDTO::getLocation)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(
                "[EclipseService]-[Inventory]-Error getting locations for count {} at branch {}",
                countId,
                branchId
            );
            throw e;
        }
    }

    /**
     * Inventory - Get Location Items
     * @param countId
     * @param locationId
     * @return
     */
    public List<EclipseLocationItemDTO> getLocationItems(String countId, String locationId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("countId", countId);
        queryParams.add("location", locationId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/ItemsInLocation")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                KourierItemsInLocationResponseDTO.class
            );
            var products = response.getBody().getItemsInLocation().get(0).getRequest();
            return products.stream().map(EclipseLocationItemDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(
                "[EclipseService]-[Inventory]-Error getting location items for location {} in count {}",
                locationId,
                countId
            );
            throw e;
        }
    }

    /**
     * Inventory - Get Next Location
     * @param countId
     * @param locationId
     * @return
     */
    public EclipseNextLocationDTO getNextLocation(String countId, String locationId) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("countId", countId);
        queryParams.add("location", locationId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/NextLocation")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                KourierNextLocationResponseDTO.class
            );
            var nextLocation = response.getBody().getTrans().get(0);
            checkInventoryError(nextLocation);
            return new EclipseNextLocationDTO(nextLocation);
        } catch (Exception e) {
            log.error(
                "[EclipseService]-[Inventory]-Error getting next location after {} in count {}",
                locationId,
                countId
            );
            throw e;
        }
    }

    /**
     * Inventory - Update Count
     * @param countId
     * @param locationId
     * @param updateCountDTO
     */
    public void updateCount(String countId, String locationId, UpdateCountRequestDTO updateCountDTO) {
        var body = new KourierUpdateCountRequestDTO(countId, locationId, updateCountDTO);
        var request = new HttpEntity<>(body, buildHeaders(ApplicationType.INVENTORY_APP));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("countId", countId);
        queryParams.add("location", locationId);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("ARS/Inventory/PostCounts")
            .queryParams(queryParams)
            .encode()
            .toUriString();
        try {
            var response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.PUT,
                request,
                KourierUpdateCountResponseDTO.class
            );
            var updatedCount = response.getBody().getPostCountResp().get(0);
            checkInventoryError(updatedCount);
        } catch (Exception e) {
            log.error(
                "[EclipseService]-[Inventory]-Error updating count for items in location {} of count {}",
                locationId,
                countId
            );
            throw e;
        }
    }

    private void checkInventoryError(KourierErrorDTO errorDTO) {
        if (!errorDTO.getIsSuccess() || !errorDTO.getErrorCode().isBlank()) {
            throw new KourierInventoryException(errorDTO);
        }
    }

    public KourierShippingDetailsResponseDTO getShippingDetails(String invoiceNumber) {
        var request = new HttpEntity<>(buildHeaders(ApplicationType.PICKING_APP));

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("/ARS/SHIPPING/INSTRUCTIONS")
            .queryParam("invoiceNumber", invoiceNumber)
            .encode()
            .toUriString();

        ResponseEntity<KourierShippingDetailsResponseDTO> response;

        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, request, KourierShippingDetailsResponseDTO.class);
        } catch (Exception e) {
            log.error("Exception occurred in Kourier getShippingDetails({}):", invoiceNumber);
            throw e;
        }
        return response.getBody();
    }

    private HttpHeaders buildHeaders(ApplicationType appType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (appType == ApplicationType.PICKING_APP) httpHeaders.setBasicAuth(
            kourierUsernamePicking,
            kourierPasswordPicking
        ); else if (appType == ApplicationType.PRICING_APP) httpHeaders.setBasicAuth(
            kourierUsernamePricing,
            kourierPasswordPricing
        ); else httpHeaders.setBasicAuth(kourierUsername, kourierPassword);
        httpHeaders.set("X-Connection", "ECLIPSE");
        httpHeaders.set("Connection", "keep-alive");
        return httpHeaders;
    }

    public void addToCount(String batchId, String locationId, UpdateCountDTO updateCountDTO) throws KourierException {
        EclipseAddCountDTO eclipseUpdateCountDTO = new EclipseAddCountDTO(
            kourierUsername,
            batchId,
            locationId,
            updateCountDTO
        );
        EclipseAddCountDTORequest eclipseAddCountDTORequest = new EclipseAddCountDTORequest();

        List<EclipseAddCountDTO> counts = new ArrayList<>();
        counts.add(eclipseUpdateCountDTO);

        eclipseAddCountDTORequest.setCounts(counts);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(kourierApiEndpoint)
            .path("/ARS/INVENTORY/ADDCOUNT")
            .encode()
            .toUriString();

        val headers = buildHeaders(ApplicationType.INVENTORY_APP);
        val request = new HttpEntity<>(eclipseAddCountDTORequest, headers);
        ResponseEntity<KourierAddToCountResponseDTO> resp;

        try {
            resp = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, KourierAddToCountResponseDTO.class);
            resp.getBody().getAddCountResp().get(0);

            KourierAddToCountResponseDTO kourierResponse = resp.getBody();

            if (
                !kourierResponse.getAddCountResp().get(0).getIsSuccess() ||
                !kourierResponse.getAddCountResp().get(0).getErrorCode().isBlank()
            ) {
                throw new KourierAddToCountException(
                    kourierResponse.getAddCountResp().get(0).getErrorCode(),
                    kourierResponse.getAddCountResp().get(0).getErrorMessage()
                );
            }
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        } catch (Exception e) {
            //TODO ERPCP 1354 generic exception handling is done but needs to be reverted to HttpClientErrorException once EAB-348 and ERPCP-1333 is completed
            log.error(
                "Exception occurred while calling /ARS/INVENTORY/ADDCOUNT for request -" +
                " batchId : " +
                batchId +
                " locationId : " +
                locationId +
                " productId : " +
                updateCountDTO.getProductId() +
                " quantity : " +
                updateCountDTO.getQuantity()
            );
            log.error("The exception is : " + e.toString());
            throw new KourierException(e.getMessage());
            //TODO uncomment above and remove above lines once EAB-348 and ERPCP-1333 is completed
        }
    }
}
