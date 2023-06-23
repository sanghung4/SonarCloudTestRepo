package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import com.reece.punchoutcustomersync.dto.kourier.CustomerProductBranchPriceDto;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceDto;
import com.reece.punchoutcustomersync.dto.kourier.CustomersPriceProductDto;
import com.reece.punchoutcustomersync.interfaces.KourierClient;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KourierServiceTest {
    @Mock
    private KourierClient kourierClient;

    @InjectMocks
    private KourierService subject;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(subject, "kourierPricingBatchSize", "50");
    }

    @Test
    public void givenACustomerWithCatalogProductsWhenGetProductDocumentsExpectProductDocumentsForEachCatalogProduct() {
        // Given:
        // A Customer:
        CustomerDao customer = TestUtils.generateCustomer();
        // with a catalog that has a CatalogProduct associated:
        CatalogDao catalog = TestUtils.generateCatalog();
        // and CatalogProducts:
        CatalogProductDao catalogProductA = TestUtils.generateMapping();
        CatalogProductDao catalogProductB = TestUtils.generateMapping();
        catalogProductB.setPartNumber("part-number-bravo");
        catalog.setMappings(Set.of(catalogProductA, catalogProductB));
        customer.setCatalogs(Set.of(catalog));

        // When:
        // Mocking the elastic search client results:
        CustomersPriceProductDto productPriceA = CustomersPriceProductDto.builder()
                .productId(catalogProductA.getPartNumber()).build();
        CustomersPriceProductDto productPriceB = CustomersPriceProductDto.builder()
                .productId(catalogProductB.getPartNumber()).build();
        List<CustomersPriceProductDto> productPrices = new ArrayList<>();
        productPrices.add(productPriceA);
        productPrices.add(productPriceB);
        CustomerProductBranchPriceDto customerProductBranchPrice = CustomerProductBranchPriceDto.builder()
                .products(productPrices)
                .build();
        CustomersPriceDto customersPrice = CustomersPriceDto.builder()
                .customerProductBranchPrice(customerProductBranchPrice)
                .build();
        Call<CustomersPriceDto> productPricesCall = mock(Call.class);
        doAnswer(invocation -> {
            Callback<CustomersPriceDto> callback = invocation.getArgument(0, Callback.class);

            callback.onResponse(productPricesCall, Response.success(customersPrice));

            return null;
        }).when(productPricesCall).enqueue(any(Callback.class));
        when(kourierClient.getCustomerProductsPricing(any(String.class), any(String.class), any(String.class)))
                .thenReturn(productPricesCall);
        // and calling the get product document for the given customer:
        List<CustomersPriceProductDto> productsPricingResponse = subject.getCustomerProductsPricing(customer, catalog.getMappings().stream().collect(Collectors.toList()));

        // Then:
        Assertions.assertEquals(catalog.getMappings().size(), productsPricingResponse.size());
        Optional<CustomersPriceProductDto> resultProductPriceA = productsPricingResponse.stream()
                .filter(i -> i.getProductId().equalsIgnoreCase(catalogProductA.getPartNumber()))
                .findFirst();
        Assertions.assertTrue(resultProductPriceA.isPresent());
        Assertions.assertEquals(resultProductPriceA.get().getProductId(), catalogProductA.getPartNumber());
        Optional<CustomersPriceProductDto> resultProductPriceB = productsPricingResponse.stream()
                .filter(i -> i.getProductId().equalsIgnoreCase(catalogProductB.getPartNumber()))
                .findFirst();
        Assertions.assertTrue(resultProductPriceB.isPresent());
        Assertions.assertEquals(resultProductPriceB.get().getProductId(), catalogProductB.getPartNumber());
    }
    @Test
    public void givenACustomerWithNoCatalogProductsWhenGetProductDocumentsExpectNoProductDocuments() {
        // Given:
        // A Customer without a catalog or catalog products:
        CustomerDao customer = TestUtils.generateCustomer();
        List<CatalogProductDao> catalogProducts = new ArrayList<>();

        // When:
        // Calling the get product document for the given customer:
        List<CustomersPriceProductDto> productsPricingResponse = subject.getCustomerProductsPricing(customer, catalogProducts);

        // Then:
        Assertions.assertEquals(0, productsPricingResponse.size());
    }
}
