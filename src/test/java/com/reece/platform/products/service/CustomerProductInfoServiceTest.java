package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.repository.ListLineItemsDAO;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

public class CustomerProductInfoServiceTest {

    @Mock
    private ErpService erpService;

    @Mock
    private ProductService productService;

    @MockBean
    private ListLineItemsDAO listLineItemsDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPriceAndAvailability() throws ExecutionException, InterruptedException {
        val testProductId1 = "32058";
        val testProductId2 = "32049";
        val testProductNumbers = List.of(testProductId1, testProductId2);
        val testEclipseProductData = TestUtils.loadResponseJson(
            "eclipse-product-data-response.json",
            ProductResponse.class
        );

        val testSelectedBranchId = "1002";

        val testUserId = UUID.randomUUID();
        val testShipToId = UUID.randomUUID();
        val testErpUserInformation = TestUtils.randomErpUserInformation();
        val testIsEmployee = TestUtils.randomBoolean();
        val testAccountNo = "811824";

        List<String> testPartNumbersRequest = Arrays.asList("84051", "84043", "45094");
        when(erpService.getEclipseProductDataAsync(testProductNumbers, testErpUserInformation, testIsEmployee))
            .thenReturn(CompletableFuture.completedFuture(testEclipseProductData));
        when(productService.getSelectedCartBranch(testUserId, testShipToId))
            .thenReturn(Optional.of(testSelectedBranchId));

        List<String[]> listsResponse = new ArrayList<String[]>();
        String[] listActual = new String[2];
        listActual[0] = "102137";
        listActual[1] = "f17539a9-d85c-4bcb-8000-2317ba135f44,858d6931-4359-4f25-96cd-0e63c896e0ec";
        listsResponse.add(listActual);
        when(
            productService.getAllListIdsByPartNumbersAndErpAccountId(
                testPartNumbersRequest,
                testAccountNo,
                ErpEnum.ECLIPSE
            )
        )
            .thenReturn(listsResponse);
        val customerProductInfoService = new CustomerProductInfoService(erpService, productService);

        val result = customerProductInfoService.getPriceAndAvailability(
            testProductNumbers,
            testErpUserInformation,
            testIsEmployee,
            testShipToId,
            testUserId
        );

        val expectedProductData = testEclipseProductData.getMassProductInquiryResponse().getProductList().getProducts();

        assertEquals(testProductId1, result.get(0).getPartNumber());
        assertEquals(testProductId2, result.get(1).getPartNumber());

        assertEquals(expectedProductData.get(0).getPricing().getCustomerPrice(), result.get(0).getPrice().toString());
        assertEquals(expectedProductData.get(1).getPricing().getCustomerPrice(), result.get(1).getPrice().toString());

        val expectedBranch1 = expectedProductData
            .get(0)
            .getAvailabilityList()
            .getBranchAvailabilityList()
            .stream()
            .filter(b -> b.getBranch().getBranchId().equals(testSelectedBranchId))
            .findFirst()
            .orElseThrow();

        assertEquals(
            expectedBranch1.getBranch().getBranchName(),
            result.get(0).getStock().getHomeBranch().getBranchName()
        );
        assertEquals(
            expectedBranch1.getNowQuantity().getQuantity().getQuantity(),
            result.get(0).getStock().getHomeBranch().getAvailability()
        );

        val expectedBranch2 = expectedProductData
            .get(1)
            .getAvailabilityList()
            .getBranchAvailabilityList()
            .stream()
            .filter(b -> b.getBranch().getBranchId().equals(testSelectedBranchId))
            .findFirst()
            .orElseThrow();

        assertEquals(
            expectedBranch2.getBranch().getBranchName(),
            result.get(1).getStock().getHomeBranch().getBranchName()
        );
        assertEquals(
            expectedBranch2.getNowQuantity().getQuantity().getQuantity(),
            result.get(1).getStock().getHomeBranch().getAvailability()
        );
    }
}
