package com.reece.platform.eclipse.external.ec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.exceptions.EclipseConnectException;
import com.reece.platform.eclipse.exceptions.InvalidEclipseCredentialsException;
import com.reece.platform.eclipse.exceptions.NextLocationNotFoundException;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EclipseConnectService {
    private static final Pattern SERVER_ERROR_PATTERN = Pattern.compile("pServerCode:(\\d+)");
    private static final String SESSION_ID_HEADER = "X-Session-Id";
    private final RestTemplate restTemplate;
    private final EclipseCredentialsValidationCache validationCache;
    private final int eclipseCredentialsValidationTimeout;

    public EclipseConnectService(@Value("${eclipse_connect_url}") String eclipseConnectUrl, RestTemplateBuilder restTemplateBuilder, EclipseCredentialsValidationCache validationCache, @Value("${eclipse_credentials_validatation_timeout}") String eclipseCredentialsValidatationTimeout) {
        restTemplate = restTemplateBuilder.rootUri(eclipseConnectUrl).build();
        this.validationCache = validationCache;
        this.eclipseCredentialsValidationTimeout = Integer.parseInt(eclipseCredentialsValidatationTimeout);
    }

    public EclipseBatchDTO validateBatch(EclipseCredentials credentials, String branchNum, String batchNum) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "validate")
                .queryParam("branchNum", branchNum)
                .queryParam("batchNum", batchNum)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseBatchDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public List<String> getAllLocations(EclipseCredentials credentials, String branchNum, String batchNum) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "allLocations")
                .queryParam("branchNum", branchNum)
                .queryParam("batchNum", batchNum)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, String[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public List<EclipseLocationDTO> getAllProducts(EclipseCredentials credentials, String branchNum, String batchNum) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "allProducts")
                .queryParam("branchNum", branchNum)
                .queryParam("batchNum", batchNum)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseLocationDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public EclipseNextLocationDTO getNextLocation(EclipseCredentials credentials, String batchNum, String locationNum) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "nextLocation")
                .queryParam("batchNum", batchNum)
                .queryParam("locationNum", locationNum)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseNextLocationDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                throw new NextLocationNotFoundException();
            }

            throw handleException(e);
        }
    }

    public List<EclipseLocationItemDTO> getLocationItems(EclipseCredentials credentials, String batchNum, String locationNum) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "location")
                .queryParam("batchNum", batchNum)
                .queryParam("locationNum", locationNum)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            val response = restTemplate.exchange(url, HttpMethod.GET, request, EclipseLocationItemDTO[].class);

            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public void updateCount(EclipseCredentials credentials, EclipseUpdateCountDTO eclipseUpdateCountDTO) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "update")
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(eclipseUpdateCountDTO, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public EclipseLocationItemDTO addToCount(EclipseCredentials credentials, EclipseUpdateCountDTO eclipseUpdateCountDTO) {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("counts", "addToCount")
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(eclipseUpdateCountDTO, headers);

        try {
            val resp = restTemplate.exchange(url, HttpMethod.POST, request, EclipseLocationItemDTO.class);
            return resp.getBody();
        } catch (HttpClientErrorException e) {
            throw handleException(e);
        }
    }

    public boolean validateCredentials(EclipseCredentials credentials) {
        if (Boolean.TRUE.equals(validationCache.get(credentials.toStorageFormat()))) {
            return true;
        }

        val url = UriComponentsBuilder.newInstance()
                .pathSegment("credentials", "validate")
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getPassword());
        headers.set(SESSION_ID_HEADER, credentials.getSessionId());
        val request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, request, Void.class);
            validationCache.add(credentials.toStorageFormat(), true, eclipseCredentialsValidationTimeout);
            return true;
        } catch (HttpClientErrorException e) {
            if (HttpStatus.UNAUTHORIZED == e.getStatusCode()) {
                validationCache.remove(credentials.toStorageFormat());
                return false;
            }

            throw handleException(e);
        }
    }

    @SneakyThrows
    private HttpClientErrorException handleException(HttpClientErrorException e) {
        if (HttpStatus.BAD_REQUEST == e.getStatusCode()) {
            val objectMapper = new ObjectMapper();
            val serverError = objectMapper.readValue(e.getResponseBodyAsString(), EclipseConnectErrorDTO.class);
            val matcher = SERVER_ERROR_PATTERN.matcher(serverError.getMessage());
            if (matcher.find()) {
                val errorCode = matcher.group(1);

                switch (errorCode) {
                    case "80011":
                        throw new InvalidEclipseCredentialsException();
                }
            }

            throw new EclipseConnectException(serverError.getMessage());
        }

        return e;
    }
}
