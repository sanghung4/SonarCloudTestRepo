package com.reece.punchoutcustomersync.configurations;
import com.reece.punchoutcustomersync.interfaces.KourierClient;
import com.reece.punchoutcustomersync.interfaces.ElasticSearchClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;

import org.springframework.context.annotation.Configuration;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfiguration {
    @Value("${elasticsearch.client.baseURL}")
    private String maxClientURL;

    @Value("${elasticsearch.client.apiToken}")
    private String maxClientApiToken;

    @Value("${kourier.client.baseURL}")
    private String kourierClientURL;

    @Value("${kourier.client.username}")
    private String kourierClientUsername;

    @Value("${kourier.client.password}")
    private String kourierClientPassword;

    @Bean
    public ElasticSearchClient maxClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.readTimeout(120, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(90, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new AuthorizationInterceptor(maxClientApiToken));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(maxClientURL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ElasticSearchClient.class);
    }

    @Bean
    public KourierClient kourierClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.readTimeout(420, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(300, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new BasicAuthInterceptor(kourierClientUsername, kourierClientPassword));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(kourierClientURL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(KourierClient.class);
    }
}
