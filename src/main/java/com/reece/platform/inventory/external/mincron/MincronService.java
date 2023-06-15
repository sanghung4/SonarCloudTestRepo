package com.reece.platform.inventory.external.mincron;

import com.reece.platform.inventory.dto.internal.MincronAllCountsDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.model.variance.VarianceSummary;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class MincronService {

    private final RestTemplate restTemplate;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    public MincronAllCountsDTO getCounts() {
        val url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "counts")
            .build()
            .toUriString();

        val result = restTemplate.getForEntity(url, MincronAllCountsDTO.class);

        return result.getBody();
    }

    public MincronCountDTO getCount(String branchId, String countId) {
        val url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "branches", branchId, "counts", countId)
            .build()
            .toUriString();

        val result = restTemplate.getForEntity(url, MincronCountDTO.class);
        return result.getBody();
    }

    public List<String> getAllLocations(String branchId, String countId) {
        val url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "branches", branchId, "counts", countId, "locations")
            .build()
            .toUriString();

        val result = restTemplate.getForEntity(url, String[].class);

        return Arrays.asList(Objects.requireNonNull(result.getBody()));
    }

    public MincronLocationDTO getLocation(String branchNum, String countId, String locationId) {
        val url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "branches", "{branchNum}", "counts", "{countId}", "locations", "{locationId}")
            .buildAndExpand(branchNum, countId, locationId)
            .toUriString();

        val result = restTemplate.getForEntity(url, MincronLocationDTO.class);
        return result.getBody();
    }

    public void updateCount(String branchNum, String countId, List<MincronPostCountDTO> listOfProducts) {
        var url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "branches", branchNum, "counts", countId)
            .build()
            .toUriString();

        var requestBody = new MincronPostCountWrapperDTO();
        requestBody.setUpdates(listOfProducts);

        val headers = new HttpHeaders();
        val request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
    }

    public void addToCount(String branchNum, String countId, String locationId, String prodNum, int countedQty) {
        var url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "branches", branchNum, "counts", countId, "locations", locationId, "items")
            .build()
            .toUriString();

        restTemplate.postForEntity(
            url,
            new MincronAddToCountDTO(branchNum, countId, locationId, prodNum, countedQty),
            Void.class
        );
    }

    public VarianceSummaryDTO getMincronVarianceSummary(String countId, String branchId) {
        var url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "varianceSummary", branchId, countId)
            .build()
            .toUriString();

        ResponseEntity<VarianceSummary> response = restTemplate.getForEntity(url, VarianceSummary.class);

        return VarianceSummaryDTO.fromEntity(response.getBody());
    }

    public VarianceDetailsDTO getMincronVarianceDetails(String countId, String branchId) {
        var url = UriComponentsBuilder
            .newInstance()
            .uri(URI.create(mincronServiceUrl))
            .pathSegment("inventory", "varianceDetails", branchId, countId)
            .build()
            .toUriString();

        ResponseEntity<VarianceDetailsDTO> response = restTemplate.getForEntity(url, VarianceDetailsDTO.class);

        return response.getBody();
    }
}
