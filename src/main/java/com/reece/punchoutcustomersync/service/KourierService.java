package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceDto;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import com.reece.punchoutcustomersync.interfaces.KourierClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KourierService {

    @Autowired
    private KourierClient kourierClient;

    @Value("${kourier.getProductPricingBatchSize}")
    private String kourierPricingBatchSize;

    @Async
    public List<CustomersPriceProductDto> getCustomerProductsPricing(CustomerDao customer, List<CatalogProductDao> catalogProducts) {
        List<CustomersPriceProductDto> customersProductsPrices = new ArrayList<>();

        if (customer.getCatalogs() == null || customer.getCatalogs().isEmpty()) {
            return customersProductsPrices;
        }

        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        List<String> customerCatalogProductErpIdsList = catalogProducts.stream()
                .filter(i -> i.getLastPullDatetime() == null || i.getLastPullDatetime().before(yesterday))
                .map(i -> i.getPartNumber())
                .collect(Collectors.toList());

        log.info(customerCatalogProductErpIdsList.size()+" products are ready to be updated by Kourier.");
        if (customerCatalogProductErpIdsList.isEmpty()) {
            return customersProductsPrices;
        }

        customersProductsPrices.addAll(executeBatchPricing(customer, customerCatalogProductErpIdsList));

        return customersProductsPrices;
    }

    private List<CustomersPriceProductDto> executeBatchPricing(CustomerDao customer, List<String> customerCatalogProductErpIds) {
        List<CustomersPriceProductDto> customerProductPricings = new ArrayList<CustomersPriceProductDto>();
        List<List<String>> batchCustomerCatalogProductErpIds = ListUtils.partition(customerCatalogProductErpIds, Integer.parseInt(kourierPricingBatchSize));

        for (List<String> batchCustomerCatalogProductErpId : batchCustomerCatalogProductErpIds) {
            String productIds = String.join(",", batchCustomerCatalogProductErpId);
            log.info("Kourier product ids " +productIds);

            // Make an asynchronous call using CompletableFuture
            CompletableFuture<Response<CustomersPriceDto>> future = new CompletableFuture<>();
            Call<CustomersPriceDto> customersProductPricingCall = kourierClient.getCustomerProductsPricing(customer.getCustomerId(), customer.getBranchId(), productIds);

            customersProductPricingCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<CustomersPriceDto> call, Response<CustomersPriceDto> response) {
                    if (response.isSuccessful()) {
                        future.complete(response);
                    } else {
                        future.completeExceptionally(new HttpClientErrorException(HttpStatus.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(Call<CustomersPriceDto> call, Throwable t) {
                    future.completeExceptionally(t);
                }
            });

            // Handle the future result
            future.thenAccept(response -> {
                // Handle successful response
                customerProductPricings.addAll(response.body().getCustomerProductBranchPrice().getProducts());
            }).exceptionally(error -> {
                // Handle error
                log.error(error.getMessage());
                return null;
            });

            future.join();
        }

        return customerProductPricings;
    }
}
