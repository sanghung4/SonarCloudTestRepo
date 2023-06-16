package com.reece.platform.mincron.service;

import static com.reece.platform.mincron.constants.MincronConstants.*;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.mincron.dto.*;
import com.reece.platform.mincron.dto.kerridge.*;
import com.reece.platform.mincron.dto.variance.VarianceDetailsResponseDTO;
import com.reece.platform.mincron.dto.variance.VarianceSummaryDTO;
import com.reece.platform.mincron.exception.MincronException;
import com.reece.platform.mincron.exception.NotFoundException;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class MincronService {

    private final RestTemplate restTemplate;

    @Value("${mincron_base_url}")
    private String mincronBaseUrl;

    /**
     * Get All Counts
     * @return
     * @throws IOException
     */
    public MincronCountsDTO getAllCounts() throws IOException {
        var url = getUriComponentBuilder().pathSegment("AvailCounts").build().toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, AvailCountsResponseDTO.class);
            return new MincronCountsDTO(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error getting all counts");
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error getting all counts");
            throw e;
        }
    }

    /**
     * Validate Count
     * @param branchId
     * @param countId
     * @return
     * @throws IOException
     * @throws MincronException
     */
    public CountDTO validateCount(String branchId, String countId) throws IOException, MincronException {
        var url = getUriComponentBuilder()
            .pathSegment("ValidateCount")
            .queryParam(BRANCH_PATH, branchId)
            .queryParam(COUNT_ID_PATH, countId)
            .build()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, ValidateCountResponseDTO.class);
            return new CountDTO(response.getBody(), countId);
        } catch (HttpClientErrorException e) {
            log.error("Error validating count {} at branch {}", countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error validating count {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Get Locations for Count
     * @param branchId
     * @param countId
     * @return
     * @throws IOException
     */
    public List<String> getLocations(String branchId, String countId) throws IOException {
        var url = getUriComponentBuilder()
            .pathSegment("CountLocations")
            .queryParam(BRANCH_PATH, branchId)
            .queryParam(COUNT_ID_PATH, countId)
            .build()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, CountLocationsResponseDTO.class);
            return response.getBody().getLocations().stream().map(LocationCodeDTO::getLocation).collect(toList());
        } catch (HttpClientErrorException e) {
            log.error("Error getting locations for count {} at branch {}", countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error getting locations for count {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Get Items at a Location
     * @param branchId
     * @param countId
     * @param locationId
     * @return
     * @throws IOException
     */
    public LocationDTO getLocation(String branchId, String countId, String locationId) throws IOException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add(COUNT_ID_PATH, countId);
        queryParams.add("location", locationId);

        var url = getUriComponentBuilder()
            .pathSegment("ItemsInLocation")
            .queryParams(queryParams)
            .build()
            .encode()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, CountLocationResponseDTO.class);
            return new LocationDTO(response.getBody(), locationId);
        } catch (HttpClientErrorException e) {
            log.error("Error getting location {} for count {} at branch {}", locationId, countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error getting location {} for count {} at branch {}", locationId, countId, branchId);
            throw e;
        }
    }

    public void updateCount(String branchId, String countId, MincronUpdateCountRequestDTO mincronUpdateCountRequestDTO)
        throws IOException {
        var body = mincronUpdateCountRequestDTO;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add(COUNT_ID_PATH, countId);

        var url = getUriComponentBuilder()
            .pathSegment("PostCount")
            .queryParams(queryParams)
            .build()
            .encode()
            .toUriString();

        var request = new HttpEntity<>(body, buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.PUT, request, PostCountResponseDTO.class);
            // Need to custom throw since this will return a 200 regardless
            if (!response.getBody().getIsSuccess()) {
                throw new MincronException(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error("Error updating count for count {} at branch {}", countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error updating count for count {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Get Next Location
     * @param branchId
     * @param countId
     * @param locationId
     * @return
     * @throws IOException
     */
    public NextLocationDTO getNextLocation(String branchId, String countId, String locationId) throws IOException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add(COUNT_ID_PATH, countId);
        queryParams.add("location", locationId);

        var url = getUriComponentBuilder()
            .pathSegment("NextLocation")
            .queryParams(queryParams)
            .build()
            .encode()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, NextLocationResponseDTO.class);
            if (!response.getBody().getIsSuccess()) {
                throw new NotFoundException(response.getBody());
            }
            return new NextLocationDTO(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error getting next location to {} for count {} at branch {}", locationId, countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error getting next location to {} for count {} at branch {}", locationId, countId, branchId);
            throw e;
        }
    }

    /**
     * Product Search
     * @param branchId
     * @param query
     * @param lastItem
     * @return
     * @throws IOException
     */
    public ProductSearchResultDTO searchProducts(String branchId, String query, String lastItem) throws IOException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add("find", query);

        if (!lastItem.isBlank()) {
            queryParams.add("last", lastItem);
        }

        String url = getUriComponentBuilder()
            .pathSegment("ProductSearch")
            .queryParams(queryParams)
            .encode()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());

        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, ProductSearchResponseDTO.class);
            return new ProductSearchResultDTO(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error searching for query {} at branch {}", query, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error searching for query {} at branch {}", query, branchId);
            throw e;
        }
    }

    /**
     * Variance Summary
     * @param branchId
     * @param countId
     * @return
     * @throws IOException
     */

    public VarianceSummaryDTO fetchVarianceSummary(String branchId, String countId) throws IOException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add(COUNT_ID_PATH, countId);

        String url = getUriComponentBuilder()
            .pathSegment(VARIANCE_SUMMARY_PATH)
            .queryParams(queryParams)
            .encode()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, VarianceSummaryDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Error searching variance Summary for countId {} at branch {}", countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error searching variance Summary for countId {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Variance Details
     * @param branchId
     * @param countId
     * @return
     * @throws IOException
     */

    public VarianceDetailsResponseDTO fetchVarianceDetails(String branchId, String countId) throws IOException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(BRANCH_PATH, branchId);
        queryParams.add(COUNT_ID_PATH, countId);

        String url = getUriComponentBuilder()
            .pathSegment(VARIANCE_DETAIL_PATH)
            .queryParams(queryParams)
            .encode()
            .toUriString();

        var request = new HttpEntity<>(buildHeaders());
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, request, VarianceDetailsResponseDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Error searching variance Details for countId {} at branch {}", countId, branchId);
            throw handleException(e);
        } catch (Exception e) {
            log.error("Error searching variance Details for countId {} at branch {}", countId, branchId);
            throw e;
        }
    }

    /**
     * Private Methods
     */
    private UriComponentsBuilder getUriComponentBuilder() {
        return UriComponentsBuilder.newInstance().uri(URI.create(mincronBaseUrl));
    }

    private MincronException handleException(HttpClientErrorException e) throws IOException {
        var objectMapper = new ObjectMapper();
        var mincronError = objectMapper.readValue(e.getResponseBodyAsByteArray(), MincronErrorDTO.class);
        return new MincronException(mincronError);
    }

    private HttpHeaders buildHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Encoding", "gzip, deflate, br");
        headers.set("Connection", "keep-alive");
        return headers;
    }
}
