package com.reece.platform.products.service;

import com.reece.platform.products.model.DTO.AccountResponseDTO;
import com.reece.platform.products.model.DTO.BranchDTO;
import com.reece.platform.products.model.DTO.FeatureDTO;
import com.reece.platform.products.model.DTO.UserDTO;
import java.util.*;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    @Value("${account_service_url}")
    private String accountServiceUrl;

    @Autowired
    public AccountService(RestTemplate rt) {
        this.restTemplate = rt;
    }

    @Getter
    private final RestTemplate restTemplate;

    /**
     * Retrieve account data information from Account Management Service given the account id
     *
     * @param accountId account to retrieve information for
     * @return account data response
     */
    public AccountResponseDTO getAccountData(String accountId, String authorization, String brand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorization);
        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        val response = restTemplate.exchange(
            accountServiceUrl + "/account/{accountId}?brand={brand}",
            HttpMethod.GET,
            request,
            AccountResponseDTO[].class,
            accountId,
            brand
        );

        return response.getBody()[0];
    }

    /**
     * Retrieve e-commerce shipTo Id from account management service given the accountId and erpId
     * @param accountId shipTo account id
     * @param erpName
     * @return
     */
    public UUID getEcommShipToId(String accountId, String erpName) {
        val response = restTemplate.getForEntity(
            accountServiceUrl + "/account/{accountId}/find-ecomm-ship-to-id/{erpName}",
            UUID.class,
            accountId,
            erpName
        );
        return response.getBody();
    }

    /**
     * Retrieve e-commerce billTo Id from account management service given the accountId and erpId
     * @param accountId shipTo account id
     * @param erpName
     * @return
     */
    public UUID getEcommBillToId(String accountId, String erpName) {
        val response = restTemplate.getForEntity(
            accountServiceUrl + "/account/{accountId}/find-ecomm-bill-to-id/{erpName}",
            UUID.class,
            accountId,
            erpName
        );
        return response.getBody();
    }

    public UserDTO getUser(String userIdOrEmail, String authorization) {
        var url = String.format("%s/users/%s?includeShipTos=true", accountServiceUrl, userIdOrEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorization);
        HttpEntity request = new HttpEntity(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, UserDTO.class).getBody();
    }

    /**
     * Retrieve home branch data from account management service
     * @param shipToAccountId
     * @return
     */
    public BranchDTO getHomeBranch(String shipToAccountId) {
        val url = accountServiceUrl + "/account/" + shipToAccountId + "/home-branch";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity(headers);
        val result = restTemplate.exchange(url, HttpMethod.GET, request, BranchDTO.class).getBody();
        return result;
    }

    public List<FeatureDTO> getFeatures() {
        val url = accountServiceUrl + "/features/";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FeatureDTO[]> request = new HttpEntity<>(headers);
        val result = restTemplate.exchange(url, HttpMethod.GET, request, FeatureDTO[].class).getBody();
        return Arrays.asList(result);
    }
}
