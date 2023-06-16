package com.reece.platform.eclipse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseEclipseService {

    @Autowired
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
}
