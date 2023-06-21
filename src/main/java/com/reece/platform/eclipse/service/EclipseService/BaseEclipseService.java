package com.reece.platform.eclipse.service.EclipseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseEclipseService {
    @Autowired
    @Qualifier("xml")
    protected RestTemplate restTemplateXML;

    @Autowired
    @Qualifier("json")
    protected RestTemplate restTemplate;

    @Value("${eclipse_endpoint}")
    protected String eclipseEndpoint;

    @Value("${login_id}")
    protected String loginId;

    @Value("${password}")
    protected String password;

    @Value("${eclipse_api_endpoint}")
    protected String eclipseApiEndpoint;

    @Value("${api_login_id}")
    protected String apiLoginId;

    @Value("${api_password}")
    protected String apiPassword;

    @Value("${eclipse_api_connect_timeout:30}")
    protected int eclipseApiConnectTimeout;

    @Value("${eclipse_api_read_timeout:300}")
    protected int eclipseApiReadTimeout;

    public <T,V> Optional<T> sendRequest(V requestObject, Class<T> responseClass) {
        // Create request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<V> request = new HttpEntity<>(requestObject, requestHeaders);

        // Send request
        try {
            ResponseEntity<T> res = restTemplateXML.postForEntity(eclipseEndpoint, request, responseClass);
            return Optional.ofNullable(res.getBody());
        }catch (RestClientException e){
            log.error("XML request to Eclipse failed", e);
            return Optional.empty();
        }
    }
}
