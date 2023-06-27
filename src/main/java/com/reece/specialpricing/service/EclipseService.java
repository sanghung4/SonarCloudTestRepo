package com.reece.specialpricing.service;

import com.reece.specialpricing.model.pojo.ProductPriceResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
@Slf4j
public class EclipseService {
    private final RestTemplate restTemplate;


    public EclipseService(@Value("${eclipse_service_url}") String eclipseConnectUrl, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.rootUri(eclipseConnectUrl).build();
    }


    public ProductPriceResponse getProductPrice(String productId, String branch, String customerId, String userId, Date effectiveDate,String correlationId) throws Exception {
        val url = UriComponentsBuilder.newInstance()
                .pathSegment("pricing", "productPrice")
                .queryParam("productId", productId)
                .queryParam("branch", branch)
                .queryParam("customerId", customerId)
                .queryParam("userId", userId)
                .queryParam("effectiveDate", !ObjectUtils.isEmpty(effectiveDate)?effectiveDate.toString():"")
                .queryParam("correlationId", correlationId)
                .build()
                .toUriString();

        val headers = new HttpHeaders();
        val eclipseRequest = new HttpEntity<>(headers);
        ResponseEntity<ProductPriceResponse> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, eclipseRequest, ProductPriceResponse.class);
            if(response!=null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException occurred in getProductPrice() while calling eclipse core service:"+e);
            throw new Exception(e);
        }
        catch (Exception e) {
            log.error("Exception occurred in Pricing Service:getProductPrice():"+e);
            throw new Exception(e);
        }

        return null;
    }
}
