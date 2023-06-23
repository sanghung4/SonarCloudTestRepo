package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomersync.dto.max.EngineDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import com.reece.punchoutcustomersync.interfaces.ElasticSearchClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticSearchService {

    @Autowired
    private ElasticSearchClient elasticSearchClient;

    @Value("${elasticsearch.getDocumentsBatchSize}")
    private String elasticSearchDocumentsBatchSize;

    public List<ProductDocumentDto> getProductDocumentsForCustomer(CustomerDao customer, List<CatalogProductDao> catalogProducts) throws IOException {
        List<ProductDocumentDto> productDocuments = new ArrayList<>();

        if (customer.getCatalogs() == null || customer.getCatalogs().isEmpty()) {
            return productDocuments;
        }

        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        List<String> customerCatalogProductErpIds = catalogProducts.stream()
                .filter(i ->i.getProduct() != null && (i.getProduct().getMaxSyncDatetime() == null || i.getProduct().getMaxSyncDatetime().before(yesterday)))
                .map(i -> "MSC-" + i.getPartNumber())
                .collect(Collectors.toList());

        log.info("{} products are ready to be updated by ES.", customerCatalogProductErpIds.size());
        if (customerCatalogProductErpIds.isEmpty()) {
            return productDocuments;
        }

        // Make an asynchronous call using CompletableFuture
        EngineDto engine = elasticSearchClient.getEngine().execute().body();
        log.info("Resulting engine GET call is {}", engine.toString());

        productDocuments.addAll(executeBatchProductRetrieval(engine, customerCatalogProductErpIds));

        return productDocuments;
    }

    private List<ProductDocumentDto> executeBatchProductRetrieval(EngineDto engine, List<String> customerCatalogProductErpIds) {
        List<ProductDocumentDto> customerProductDocuments = new ArrayList<>();
        List<List<String>> batchCustomerCatalogProductErpIds = ListUtils.partition(customerCatalogProductErpIds, Integer.parseInt(elasticSearchDocumentsBatchSize));

        log.info("The total of {} products ready for ES update have been split into {} lists of size equal to or less than {}", customerCatalogProductErpIds.size(), batchCustomerCatalogProductErpIds.size(),elasticSearchDocumentsBatchSize);
        for (List<String> batchCustomerCatalogProductErpId : batchCustomerCatalogProductErpIds) {
            log.info("Attempting to find ES data for the following products: {}", batchCustomerCatalogProductErpId.stream().map(i -> "\""+i+"\"").collect(Collectors.joining(",")));
            // Make an asynchronous call using CompletableFuture
            CompletableFuture<Response<List<ProductDocumentDto>>> future = new CompletableFuture<>();
            Call<List<ProductDocumentDto>> productDocumentsCall = elasticSearchClient.getProductAttributes(engine.getName(), batchCustomerCatalogProductErpId);
            productDocumentsCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<ProductDocumentDto>> call, Response<List<ProductDocumentDto>> response) {
                    if (response.isSuccessful()) {
                        future.complete(response);
                    } else {
                        future.completeExceptionally(new HttpClientErrorException(HttpStatus.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(Call<List<ProductDocumentDto>> call, Throwable t) {
                    future.completeExceptionally(t);
                }
            });

            // Handle the future result
            future.thenAccept(response -> {
                // Handle successful response
                log.info("Retrieved {} of the {} products from the current batch session.", response.body().size(), batchCustomerCatalogProductErpId.size());
                customerProductDocuments.addAll(response.body());
            }).exceptionally(error -> {
                // Handle error
                log.error(error.getMessage());
                return null;
            });

            future.join();
        }

        return customerProductDocuments;
    }
}
