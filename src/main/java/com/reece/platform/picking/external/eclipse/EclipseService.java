package com.reece.platform.picking.external.eclipse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.picking.dto.*;
import com.reece.platform.picking.dto.kourier.ShippingDetailsResponseDTO;
import com.reece.platform.picking.exception.*;
import com.reece.platform.picking.mapper.WarehouseCloseTaskMapper;
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
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EclipseService {

    private final RestTemplate restTemplate;
    private final WarehouseCloseTaskMapper warehouseCloseTaskMapper;

    @Value("${eclipse_service_url}")
    private String eclipseBaseUrl;

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

    /**
     * Get all picking orders for a branch
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
