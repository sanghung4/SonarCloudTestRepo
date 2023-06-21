package com.reece.platform.eclipse.config;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
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

    @Bean("xml")
    public RestTemplate okHttp3TemplateXML() {
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

        Jaxb2RootElementHttpMessageConverter xmlConverter = new Jaxb2RootElementHttpMessageConverter();
        xmlConverter.setSupportDtd(true);

        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(builder.build()));
        restTemplate.setMessageConverters(List.of(xmlConverter));

        return restTemplate;
    }

    @Bean("json")
    @Primary
    public RestTemplate okHttp3TemplateJSON() {
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

        Jaxb2RootElementHttpMessageConverter xmlConverter = new Jaxb2RootElementHttpMessageConverter();
        xmlConverter.setSupportDtd(true);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        jsonConverter.getObjectMapper().setDateFormat(new SimpleDateFormat());
        jsonConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Any extra properties in the JSON Kourier response will NOT give a parse error and cause us to fail.
        jsonConverter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(builder.build()));
        restTemplate.setMessageConverters(List.of(jsonConverter, xmlConverter));

        return restTemplate;
    }
}
