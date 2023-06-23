package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import com.reece.punchoutcustomersync.dto.max.EngineDto;
import com.reece.punchoutcustomersync.dto.max.ProductDocumentDto;
import com.reece.punchoutcustomersync.interfaces.ElasticSearchClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElasticSearchServiceTest {
    @Mock
    private ElasticSearchClient elasticSearchClient;

    @InjectMocks
    private ElasticSearchService subject;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(subject, "elasticSearchDocumentsBatchSize", "50");
    }

    @Test
    public void givenACustomerWithCatalogProductsWhenGetProductDocumentsExpectProductDocumentsForEachCatalogProduct() throws IOException {
        // Given:
        // A Customer:
        CustomerDao customer = TestUtils.generateCustomer();
        // with a catalog that has a CatalogProduct associated:
        CatalogDao catalog = TestUtils.generateCatalog();
        // and CatalogProducts:
        CatalogProductDao catalogProductA = TestUtils.generateMapping();
        ProductDao productA = ProductDao.builder()
                .partNumber(catalogProductA.getPartNumber())
                .maxSyncDatetime(null)
                .build();
        catalogProductA.setProduct(productA);
        CatalogProductDao catalogProductB = TestUtils.generateMapping();
        catalogProductB.setPartNumber("part-number-bravo");
        ProductDao productB = ProductDao.builder()
                .partNumber(catalogProductB.getPartNumber())
                .maxSyncDatetime(null)
                .build();
        catalogProductB.setProduct(productB);
        catalog.setMappings(Set.of(catalogProductA, catalogProductB));
        customer.setCatalogs(Set.of(catalog));

        // When:
        // Mocking the elastic search client results:
        EngineDto engine = EngineDto.builder().name("TEST-ENGINE").build();
        Call<EngineDto> engineCall = mock(Call.class);
        when(engineCall.execute()).thenReturn(Response.success(engine));
        when(elasticSearchClient.getEngine()).thenReturn(engineCall);
        ProductDocumentDto productDocumentA = ProductDocumentDto.builder()
                        .id(catalogProductA.getPartNumber()).build();
        ProductDocumentDto productDocumentB = ProductDocumentDto.builder()
                        .id(catalogProductB.getPartNumber()).build();
        List<ProductDocumentDto> productDocuments = new ArrayList<>();
        productDocuments.add(productDocumentA);
        productDocuments.add(productDocumentB);
        Call<List<ProductDocumentDto>> productDocumentsCall = mock(Call.class);
        doAnswer(invocation -> {
            Callback<List<ProductDocumentDto>> callback = invocation.getArgument(0, Callback.class);

            callback.onResponse(productDocumentsCall, Response.success(productDocuments));

            return null;
        }).when(productDocumentsCall).enqueue(any(Callback.class));
        when(elasticSearchClient.getProductAttributes(any(String.class), anyList())).thenReturn(productDocumentsCall);
        // and calling the get product document for the given customer:
        List<ProductDocumentDto> productDocumentsResponse = subject.getProductDocumentsForCustomer(customer, catalog.getMappings().stream().collect(Collectors.toList()));

        // Then:
        Assertions.assertEquals(catalog.getMappings().size(), productDocumentsResponse.size());
        Optional<ProductDocumentDto> resultProductDocumentA = productDocumentsResponse.stream()
                .filter(i -> i.getId().equalsIgnoreCase(catalogProductA.getPartNumber()))
                .findFirst();
        Assertions.assertTrue(resultProductDocumentA.isPresent());
        Assertions.assertEquals(resultProductDocumentA.get().getId(), catalogProductA.getPartNumber());
        Optional<ProductDocumentDto> resultProductDocumentB = productDocumentsResponse.stream()
                .filter(i -> i.getId().equalsIgnoreCase(catalogProductB.getPartNumber()))
                .findFirst();
        Assertions.assertTrue(resultProductDocumentB.isPresent());
        Assertions.assertEquals(resultProductDocumentB.get().getId(), catalogProductB.getPartNumber());
    }
    @Test
    public void givenACustomerWithNoCatalogProductsWhenGetProductDocumentsExpectNoProductDocuments() throws IOException {
        // Given:
        // A Customer without a catalog or catalog products:
        CustomerDao customer = TestUtils.generateCustomer();
        List<CatalogProductDao> catalogProducts = new ArrayList<>();

        // When:
        // Calling the get product document for the given customer:
        List<ProductDocumentDto> productDocumentsResponse = subject.getProductDocumentsForCustomer(customer, catalogProducts);

        // Then:
        Assertions.assertEquals(0, productDocumentsResponse.size());
    }
}
