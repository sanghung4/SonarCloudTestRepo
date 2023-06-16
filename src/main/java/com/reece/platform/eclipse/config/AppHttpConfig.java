package com.reece.platform.eclipse.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppHttpConfig {

    @Value("${okHttp.maxIdleConnections:100}")
    private int maxIdleConnections;

    @Value("${okHttp.keepAliveDuration:60}")
    private int keepAliveDuration;

    @Value("${okHttp.connectTimeout:60}")
    private int connectTimeout;

    @Value("${okHttp.writeTimout:60}")
    private int writeTimout;

    @Value("${okHttp.readTimout:60}")
    private int readTimeout;

    @Bean
    @Primary
    public RestTemplate okHttp3Template() {
        RestTemplate restTemplate = new RestTemplate();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(
            maxIdleConnections,
            keepAliveDuration,
            TimeUnit.SECONDS
        );

        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        jsonConverter.getObjectMapper().setDateFormat(new SimpleDateFormat());

        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(builder.build()));
        restTemplate.setMessageConverters(List.of(jsonConverter));

        return restTemplate;
    }
}
