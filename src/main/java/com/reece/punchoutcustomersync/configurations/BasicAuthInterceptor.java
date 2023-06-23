package com.reece.punchoutcustomersync.configurations;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;

import java.io.IOException;

public class BasicAuthInterceptor implements Interceptor {

    private final String username;
    private final String password;

    public BasicAuthInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64Utils.encodeToString(credentials.getBytes());

        Request originalRequest = chain.request();

        // Add authorization header with bearer token
        Request authorizedRequest = originalRequest.newBuilder()
                .header("Authorization", "Basic " + encodedCredentials)
                .build();

        return chain.proceed(authorizedRequest);
    }
}
