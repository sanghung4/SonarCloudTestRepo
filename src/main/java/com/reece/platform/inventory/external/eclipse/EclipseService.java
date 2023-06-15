package com.reece.platform.inventory.external.eclipse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.dto.kourier.ShippingDetailsResponseDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.exception.*;
import com.reece.platform.inventory.mapper.WarehouseCloseTaskMapper;
import com.reece.platform.inventory.model.variance.VarianceSummary;
import com.reece.platform.inventory.service.SessionIdHolder;
import com.reece.platform.inventory.util.DateUtil;
import com.reece.platform.inventory.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableRetry
public class EclipseService {

    private final RestTemplate restTemplate;
    private final SessionIdHolder sessionIdHolder;
    private final WarehouseCloseTaskMapper warehouseCloseTaskMapper;

    @Value("${eclipse_service_url}")
    private String eclipseBaseUrl;

    public List<EclipseCountStatusDTO> getCounts(Date startDate, Date endDate) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("startDate", DateUtil.formatAsDateString(startDate));
        queryParams.add("endDate", DateUtil.formatAsDateString(endDate));

        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "counts")
                .queryParams(queryParams)
                .build()
                .encode()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseCountStatusResponseDTO.class);
            return response.getBody().getCounts();
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public EclipseCountDTO getCount(String countId) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "counts", countId)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseCountDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public EclipseBatchDTO validateBatch(String branchNum, String batchNum) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "branches", branchNum, "batches", batchNum)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseBatchDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public List<EclipseLocationItemDTO> getLocationItems(String batchNum, String locationNum) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "batches", batchNum, "locations", locationNum)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseLocationItemDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public void updateCount(String batchNum, String locationNum, List<EclipsePostCountDTO> listOfProducts) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "batches", batchNum, "locations", locationNum)
                .build()
                .toUriString();

        var requestBody = new EclipsePostCountWrapperDTO();
        requestBody.setUpdates(listOfProducts);

        var request = new HttpEntity<>(requestBody, buildHeaders());

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    @SneakyThrows
    public void addToCount(String batchNum, String locationNum, String productId, int quantity) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("inventory", "batches", batchNum, "locations", locationNum, "_new")
                .build()
                .toUriString();

        val requestBody = new EclipseUpdateCountDTO(productId, quantity);

        val headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        val request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, EclipseAddToCountResponse.class);
        } catch (HttpClientErrorException he) {
            //TODO Handled as part of ERPCP-1354 & ERPCP-1393 but needs to be reverted once EAB-348 and ERPCP-1333 is completed. Start
            EclipseErrorDTO eclipseError = getEclipseErrorDTO(he);
            if (Objects.equals(eclipseError.getMessage(), Util.ECLIPSE_CONNECT_400_EXCEPTION)) {
                log.error(
                        "Got an exception while writing to eclipse for Product: " +
                                productId +
                                " batchNum : " +
                                batchNum +
                                " locationNum : " +
                                locationNum +
                                " quantity : " +
                                quantity
                );
            }
            //Handled as part of ERPCP-1354 & ERPCP-1393 but needs to be reverted once EAB-348 and ERPCP-1333 is completed. End
            else throw handleException(he);
        } catch (Exception e) {
            throw new EclipseAddToCountException(e.getMessage());
        }
    }

    @SneakyThrows
    private HttpClientErrorException handleException(HttpClientErrorException e) {
        EclipseErrorDTO eclipseError = getEclipseErrorDTO(e);

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

    public ProductSearchResultDTO getProductSearch(ProductSearchRequestDTO productSearchRequestDTO) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("product", "search")
                .build()
                .toUriString();

        var request = new HttpEntity<>(productSearchRequestDTO, buildHeaders());
        ResponseEntity<ProductSearchResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, ProductSearchResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return new ProductSearchResultDTO(response.getBody());
    }

    /**
     * Get all picking orders for a branch
     *
     * @param branchId
     * @param userId
     * @return
     */
    public PickingTasksResponseDTO getPickingTasks(String branchId, String userId) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "tasks")
                .queryParam("branchId", branchId)
                .queryParam("userId", userId)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        ResponseEntity<PickingTasksResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, PickingTasksResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    /**
     * Assign a user a pick order
     *
     * @param tasks
     * @return
     */
    public PickTasksListDTO assignPickingTasks(PickTasksListDTO tasks) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "tasks")
                .build()
                .toUriString();

        var request = new HttpEntity<>(tasks, buildHeaders());
        ResponseEntity<PickTasksListDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, PickTasksListDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    /**
     * Get User Picks from eclipse
     *
     * @param branchId
     * @param userId
     * @return
     */
    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 6000, multiplier = 2), exclude = HttpClientErrorException.class)
    public WarehouseUserPicksDTO getUserPicks(String branchId, String userId, String orderId) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "user")
                .queryParam("branchId", branchId)
                .queryParam("userId", userId)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        ResponseEntity<WarehouseUserPicksDTO> response;
        log.info("calling getUserPicks()");
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, WarehouseUserPicksDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }

        return filterUsersPickListByOrderId(response.getBody(), orderId);
    }

    private WarehouseUserPicksDTO filterUsersPickListByOrderId(WarehouseUserPicksDTO picks, String orderId) {
        val filteredProducts = picks
                .getResults()
                .stream()
                .filter(p -> Objects.equals(p.getOrderId(), orderId))
                .collect(Collectors.toList());
        picks.setResults(filteredProducts);
        return picks;
    }

    public WarehousePickCompleteDTO completeUserPick(String pickId, WarehousePickCompleteDTO userPick) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "user", "pick", pickId)
                .build()
                .toUriString();

        var request = new HttpEntity<>(userPick, buildHeaders());
        ResponseEntity<WarehousePickCompleteDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, WarehousePickCompleteDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public ProductSerialNumbersResponseDTO getSerialNumber(String warehouseId) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "tasks", warehouseId, "serialNumbers")
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, ProductSerialNumbersResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public ProductSerialNumbersResponseDTO updateSerialNumbers(
            String warehouseId,
            ProductSerialNumberRequestDTO serialNumbers
    ) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "tasks", warehouseId, "serialNumbers")
                .build()
                .toUriString();

        var request = new HttpEntity<>(serialNumbers, buildHeaders());
        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, ProductSerialNumbersResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public WarehouseCloseTaskDTO closePickTask(WarehouseCloseTaskRequestDTO closeTaskRequestDTO) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "tasks", "close")
                .build()
                .toUriString();

        WarehouseCloseTaskDTO closeTaskDTO = warehouseCloseTaskMapper.toWarehouseCloseTaskDTO(closeTaskRequestDTO);

        var request = new HttpEntity<>(closeTaskDTO, buildHeaders());
        ResponseEntity<WarehouseCloseTaskDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, WarehouseCloseTaskDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public CloseOrderResponseDTO closeOrder(CloseOrderRequestDTO closeOrderRequestDTO) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "closeOrder")
                .build()
                .toUriString();

        var request = new HttpEntity<>(closeOrderRequestDTO, buildHeaders());
        ResponseEntity<CloseOrderResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, CloseOrderResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public WarehouseToteTaskDTO stagePickTask(WarehouseToteTaskDTO stagePickDTO) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "user", "tote")
                .build()
                .toUriString();

        var request = new HttpEntity<>(stagePickDTO, buildHeaders());
        ResponseEntity<WarehouseToteTaskDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, WarehouseToteTaskDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public CustomerSearchResponseDTO getCustomerSearch(CustomerSearchInputDTO input) {
        val urlTemplate = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("customer", "_search")
                .build()
                .toUriString();

        var request = new HttpEntity<>(CustomerSearchRequestDTO.fromEntity(input), buildHeaders());

        ResponseEntity<CustomerSearchResponseDTO> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.POST, request, CustomerSearchResponseDTO.class);
        } catch (HttpServerErrorException hsee) {
            throw hsee;
        }
        return response.getBody();
    }

    public VarianceSummaryDTO getVarianceSummary(String countId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("countId", countId);

        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("variance", "summary")
                .queryParams(queryParams)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        ResponseEntity<VarianceSummary> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, VarianceSummary.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return VarianceSummaryDTO.fromEntity(response.getBody());
    }

    public VarianceDetailsDTO getVarianceDetails(String countId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("countId", countId);

        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("variance", "details")
                .queryParams(queryParams)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        ResponseEntity<VarianceDetailsDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, VarianceDetailsDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public WarehouseTotePackagesDTO stagePickTotePackages(WarehouseTotePackagesDTO totePackagesDTO) {
        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "user", "tote", "package")
                .build()
                .toUriString();

        var request = new HttpEntity<>(totePackagesDTO, buildHeaders());
        ResponseEntity<WarehouseTotePackagesDTO> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, WarehouseTotePackagesDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO getKourierProductSearch(
            String keywords,
            String displayName,
            String searchInputType
    ) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (Objects.nonNull(searchInputType) && searchInputType.equalsIgnoreCase("1")) queryParams.add(
                "keywords",
                Util.PERIOD + keywords
        );
        else queryParams.add("keywords", keywords);

        queryParams.add("displayName", displayName);

        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("product", "search")
                .queryParams(queryParams)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        ResponseEntity<com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO> response;

        try {
            response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            request,
                            com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO.class
                    );
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    public ShippingDetailsResponseDTO getShippingDetails(String invoiceNumber) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("invoiceNumber", invoiceNumber);

        val url = UriComponentsBuilder
                .newInstance()
                .uri(URI.create(eclipseBaseUrl))
                .pathSegment("picking", "shipping")
                .queryParams(queryParams)
                .build()
                .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        ResponseEntity<ShippingDetailsResponseDTO> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, ShippingDetailsResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
        return response.getBody();
    }

    private EclipseErrorDTO getEclipseErrorDTO(HttpClientErrorException e) throws java.io.IOException {
        val objectMapper = new ObjectMapper();
        return objectMapper.readValue(e.getResponseBodyAsByteArray(), EclipseErrorDTO.class);
    }

    private HttpHeaders buildHeaders() {
        var headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        return headers;
    }

}
