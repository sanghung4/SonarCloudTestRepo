package com.reece.punchoutcustomersync.configurations;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthorizationInterceptor implements Interceptor {

    private final String bearerToken;

    public AuthorizationInterceptor(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Add authorization header with bearer token
        Request authorizedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        return chain.proceed(authorizedRequest);
    }
}
