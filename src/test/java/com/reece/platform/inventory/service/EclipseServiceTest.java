package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.dto.kourier.Product;
import com.reece.platform.inventory.dto.kourier.ProductSearchResponse;
import com.reece.platform.inventory.dto.kourier.ShippingDetailsResponseDTO;
import com.reece.platform.inventory.dto.kourier.ShippingTextResponseDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.exception.*;
import com.reece.platform.inventory.external.eclipse.*;
import com.reece.platform.inventory.mapper.WarehouseCloseTaskMapper;
import com.reece.platform.inventory.model.variance.VarianceSummary;
import com.reece.platform.inventory.util.TestCommon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.reece.platform.inventory.util.TestCommon.mockCloseOrderRequestDTO;
import static com.reece.platform.inventory.util.TestCommon.mockPickingTaskDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EclipseServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WarehouseCloseTaskMapper warehouseCloseTaskMapper;

    @InjectMocks
    private EclipseService eclipseService = new EclipseService(restTemplate, null, warehouseCloseTaskMapper);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(eclipseService, "eclipseBaseUrl", "test.com");
        ReflectionTestUtils.setField(eclipseService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(eclipseService, "warehouseCloseTaskMapper", warehouseCloseTaskMapper);
    }

    @Test
    public void getCounts_Success() {

        EclipseCountStatusResponseDTO eclipseCountStatusResponseDTO = new EclipseCountStatusResponseDTO();
        eclipseCountStatusResponseDTO.setCounts(Collections.singletonList(TestCommon.eclipseCountStatusDTO()));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseCountStatusResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(eclipseCountStatusResponseDTO, HttpStatus.OK));

        List<EclipseCountStatusDTO> result = eclipseService.getCounts(new Date(), new Date());
        assertNotNull(result);
        assertEquals(result.get(0).getNumItems(), 6373);
        assertEquals(result.get(0).getBranchNum(), "test");
        assertEquals(result.get(0).getCountDate(), "2023-04-29");

    }

    @Test
    public void getCounts_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseCountStatusResponseDTO.class)))
                .thenThrow(EclipseConnectException.class);
        assertThrows(EclipseConnectException.class, () -> eclipseService.getCounts(new Date(), new Date()));

    }

    @Test
    public void getCount_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseCountDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.eclipseCountDTO(), HttpStatus.OK));

        EclipseCountDTO result = eclipseService.getCount("test");
        assertNotNull(result.getProducts());
        assertEquals(result.getCountId(), 5446);
        assertEquals(result.getProducts().get(0).getImageUrl(), "test.jpg");
        assertEquals(result.getProducts().get(0).getUom(), "I");
        assertEquals(result.getCountDescription(), "test desc");
        assertEquals(result.getCreatedAt(), "2023-04-13");

    }

    @Test
    public void getCount_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseCountDTO.class)))
                .thenThrow(InvalidEclipseCredentialsException.class);
        assertThrows(InvalidEclipseCredentialsException.class, () -> eclipseService.getCount("test"));

    }

    @Test
    public void validateBatch_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseBatchDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.eclipseBatchDTO(), HttpStatus.OK));

        EclipseBatchDTO result = eclipseService.validateBatch("test", "test");
        assertEquals(result.getCountId(), "2324");
        assertEquals(result.getBranchId(), "7462");
        assertEquals(result.getBranchName(), "test");

    }

    @Test
    public void validateBatch_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseBatchDTO.class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.validateBatch("test", "test"));

    }

    @Test
    public void getLocationItems_Success() {

        EclipseLocationItemDTO[] eclipseLocationItemDTOS = new EclipseLocationItemDTO[]{TestCommon.eclipseLocationItemDTO()};
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseLocationItemDTO[].class)))
                .thenReturn(new ResponseEntity<>(eclipseLocationItemDTOS, HttpStatus.OK));

        List<EclipseLocationItemDTO> result = eclipseService.getLocationItems("test", "test");
        assertEquals(result.get(0).getProductId(), "7432");
        assertEquals(result.get(0).getImageUrl(), "tst.jpg");
        assertEquals(result.get(0).getControlNum(), "6434");

    }

    @Test
    public void getLocationItems_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(EclipseLocationItemDTO[].class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.getLocationItems("test", "test"));

    }

    @Test
    public void updateCount_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        eclipseService.updateCount("test", "test", new ArrayList<EclipsePostCountDTO>());

    }

    @Test
    public void updateCount_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Void.class)))
                .thenThrow(InvalidEclipseCredentialsException.class);
        assertThrows(InvalidEclipseCredentialsException.class, () -> eclipseService.updateCount("test", "test", new ArrayList<EclipsePostCountDTO>()));

    }

    @Test
    public void addToCount_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(EclipseAddToCountResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        eclipseService.addToCount("test", "test", "6324", 23);

    }

    @Test
    public void addToCount_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(EclipseAddToCountResponse.class)))
                .thenThrow(EclipseAddToCountException.class);
        assertThrows(EclipseAddToCountException.class, () -> eclipseService.addToCount("test", "test", "6324", 23));

    }

    @Test
    public void getProductSearch_Success() {

        ProductSearchResponseDTO productSearchResponseDTO = new ProductSearchResponseDTO();
        productSearchResponseDTO.setMetadata(TestCommon.productSearchMetadata());
        productSearchResponseDTO.setResults(Collections.singletonList(TestCommon.productSearchResult()));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ProductSearchResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(productSearchResponseDTO, HttpStatus.OK));

        ProductSearchResultDTO result = eclipseService.getProductSearch(TestCommon.productSearchRequestDTO());
        assertNotNull(result.getProducts());
        assertEquals(result.getPagination().getPageSize(), 2);
        assertEquals(result.getProducts().get(0).getId(), "3526");
        assertEquals(result.getProducts().get(0).getUpc(), "test");
        assertEquals(result.getProducts().get(0).getPartNumber(), "6225");
        assertEquals(result.getProducts().get(0).getName(), "test dsc");
        assertEquals(result.getProducts().get(0).getImageUrls().getThumb(), "test.icon");

    }

    @Test
    public void getProductSearch_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ProductSearchResponseDTO.class)))
                .thenThrow(InvalidEclipseCredentialsException.class);
        assertThrows(InvalidEclipseCredentialsException.class, () -> eclipseService.getProductSearch(TestCommon.productSearchRequestDTO()));

    }

    @Test
    public void getPickingTasks_Success() {

        PickingTasksResponseDTO pickingTasksResponseDTO = new PickingTasksResponseDTO();
        pickingTasksResponseDTO.setResults(Collections.singletonList(mockPickingTaskDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PickingTasksResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(pickingTasksResponseDTO, HttpStatus.OK));

        PickingTasksResponseDTO result = eclipseService.getPickingTasks("test", "test");
        assertNotNull(result.getResults());
        assertEquals(result.getResults().get(0).getBranchId(), "5232");
        assertEquals(result.getResults().get(0).getOrderId(), "2557");
        assertEquals(result.getResults().get(0).getAssignedUserId(), "testUser");

    }

    @Test
    public void getPickingTasks_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PickingTasksResponseDTO.class)))
                .thenThrow(EclipseConnectException.class);
        assertThrows(EclipseConnectException.class, () -> eclipseService.getPickingTasks("test", "test"));

    }

    @Test
    public void assignPickingTasks_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(PickTasksListDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mockPickTasksListDTO(), HttpStatus.OK));

        PickTasksListDTO result = eclipseService.assignPickingTasks(TestCommon.mockPickTasksListDTO());
        assertNotNull(result.getWarehousePickTasksList());
        assertNotNull(result.getWarehousePickTasksWarnings());
        assertEquals(result.getWarehousePickTasksList().get(0).getAssignedUserId(), "testUser");
        assertEquals(result.getWarehousePickTasksList().get(0).getInvoiceId(), "8832");
        assertEquals(result.getWarehousePickTasksWarnings().get(0).getTaskOrderId(), "9743");
        assertEquals(result.getWarehousePickTasksWarnings().get(0).getTaskOrderGid(), 423);

    }

    @Test
    public void assignPickingTasks_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(PickTasksListDTO.class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.assignPickingTasks(TestCommon.mockPickTasksListDTO()));

    }

    @Test
    public void getUserPicks_Success() {

        WarehouseUserPicksDTO warehouseUserPicksDTO = new WarehouseUserPicksDTO();
        warehouseUserPicksDTO.setResults(Collections.singletonList(TestCommon.warehouseUserPickDTO()));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WarehouseUserPicksDTO.class)))
                .thenReturn(new ResponseEntity<>(warehouseUserPicksDTO, HttpStatus.OK));

        WarehouseUserPicksDTO result = eclipseService.getUserPicks("2256", "8322", "8435");
        assertNotNull(result.getResults());
        assertEquals(result.getResults().get(0).getOrderId(), "8435");
        assertEquals(result.getResults().get(0).getBranchId(), "2256");
        assertEquals(result.getResults().get(0).getProductId(), "2324");

    }

    @Test
    public void getUserPicks_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WarehouseUserPicksDTO.class)))
                .thenThrow(InvalidSerializedProductException.class);
        assertThrows(InvalidSerializedProductException.class, () -> eclipseService.getUserPicks("test", "test", ""));

    }

    @Test
    public void completeUserPick_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehousePickCompleteDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.createSampleWarehousePickCompleteDTO(), HttpStatus.OK));

        WarehousePickCompleteDTO result = eclipseService.completeUserPick("test", TestCommon.createSampleWarehousePickCompleteDTO());
        assertEquals(result.getUom(), "testUom");
        assertEquals(result.getBranchId(), "testBranch");
        assertEquals(result.getUserId(), "testuserId");
        assertEquals(result.getWarehouseID(), "TestWareHouseId");

    }

    @Test
    public void completeUserPick_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehousePickCompleteDTO.class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.completeUserPick("test", TestCommon.createSampleWarehousePickCompleteDTO()));

    }

    @Test
    public void getSerialNumber_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductSerialNumbersResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mockProductSerialNumbersResponseDTO(), HttpStatus.OK));

        ProductSerialNumbersResponseDTO result = eclipseService.getSerialNumber("test");
        assertNotNull(result.getResults());
        assertEquals(result.getResults().get(0).getInvoiceId(), "2556");
        assertEquals(result.getResults().get(0).getOrderId(), "8525");
        assertEquals(result.getResults().get(0).getProductId(), "3632");
        assertEquals(result.getResults().get(0).getSerialList().get(0).getSerial(), "TestSerial");

    }

    @Test
    public void getSerialNumber_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductSerialNumbersResponseDTO.class)))
                .thenThrow(EclipseConnectException.class);
        assertThrows(EclipseConnectException.class, () -> eclipseService.getSerialNumber("test"));

    }

    @Test
    public void updateSerialNumbers_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(ProductSerialNumbersResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mockProductSerialNumbersResponseDTO(), HttpStatus.OK));

        ProductSerialNumbersResponseDTO result = eclipseService.updateSerialNumbers("test", TestCommon.createSampleProductSerialNumberRequestDTO());
        assertNotNull(result.getResults());
        assertEquals(result.getResults().get(0).getLocation(), "8322");
        assertEquals(result.getResults().get(0).getDescription(), "test desc");
        assertEquals(result.getResults().get(0).getWarehouseId(), "41567");
        assertEquals(result.getResults().get(0).getNonStockSerialNumbers().get(0).getLine(), 1);

    }

    @Test
    public void updateSerialNumbers_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(ProductSerialNumbersResponseDTO.class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.updateSerialNumbers("test", TestCommon.createSampleProductSerialNumberRequestDTO()));

    }

    @Test
    public void closePickTask_Success() {

        WarehouseCloseTaskTotes warehouseCloseTaskTotes = new WarehouseCloseTaskTotes("testTote", "8334");
        WarehouseCloseTaskDTO warehouseCloseTaskDTO = new WarehouseCloseTaskDTO("testInvoiceNumber", "testBranch", "8334", Collections.singletonList(warehouseCloseTaskTotes), true, true, true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseCloseTaskDTO.class)))
                .thenReturn(new ResponseEntity<>(warehouseCloseTaskDTO, HttpStatus.OK));

        WarehouseCloseTaskDTO result = eclipseService.closePickTask(TestCommon.createSampleWarehouseCloseTaskRequestDTO());
        assertNotNull(result.getTotes());
        assertEquals(result.getBranchId(), "testBranch");
        assertEquals(result.getFinalLocation(), "8334");
        assertEquals(result.getSkipStagedWarningFlag(), true);
        assertEquals(result.getTotes().get(0).getTote(), "testTote");

    }

    @Test
    public void closePickTask_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseCloseTaskDTO.class)))
                .thenThrow(InvalidSerializedProductException.class);
        assertThrows(InvalidSerializedProductException.class, () -> eclipseService.closePickTask(TestCommon.createSampleWarehouseCloseTaskRequestDTO()));

    }

    @Test
    public void closeOrder_Success() {

        CloseOrderResponseDTO closeOrderResponseDTO = new CloseOrderResponseDTO(true, "4145", "7526", "test", "test msg", false, false, true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CloseOrderResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(closeOrderResponseDTO, HttpStatus.OK));

        CloseOrderResponseDTO result = eclipseService.closeOrder(mockCloseOrderRequestDTO);
        assertEquals(result.getOrderId(), "4145");
        assertEquals(result.getErrorCode(), "test");
        assertEquals(result.getPickerId(), "7526");
        assertTrue(result.isStatus());

    }

    @Test
    public void closeOrder_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CloseOrderResponseDTO.class)))
                .thenThrow(InvalidEclipseCredentialsException.class);
        assertThrows(InvalidEclipseCredentialsException.class, () -> eclipseService.closeOrder(mockCloseOrderRequestDTO));

    }

    @Test
    public void stagePickTask_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseToteTaskDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mockWarehouseToteTaskDTO(), HttpStatus.OK));

        WarehouseToteTaskDTO result = eclipseService.stagePickTask(TestCommon.mockWarehouseToteTaskDTO());
        assertEquals(result.getLocation(), "testLocation");
        assertEquals(result.getTote(), "testTote");
        assertEquals(result.getInvoiceNumber(), "testInvoiceNumber");
        assertEquals(result.getBranchId(), "testBranch");

    }

    @Test
    public void stagePickTask_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseToteTaskDTO.class)))
                .thenThrow(EclipseConnectException.class);
        assertThrows(EclipseConnectException.class, () -> eclipseService.stagePickTask(TestCommon.mockWarehouseToteTaskDTO()));

    }

    @Test
    public void getCustomerSearch_Success() {

        CustomerSearchResponseDTO customerSearchResponseDTO = new CustomerSearchResponseDTO();
        customerSearchResponseDTO.setMetadata(TestCommon.eclipseSearchMetadata());
        customerSearchResponseDTO.setResults(Collections.singletonList(TestCommon.customerSearchResult()));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CustomerSearchResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(customerSearchResponseDTO, HttpStatus.OK));

        CustomerSearchResponseDTO result = eclipseService.getCustomerSearch(TestCommon.mockCustomerSearchInputDTO());
        assertNotNull(result.getResults());
        assertEquals(result.getResults().get(0).getId(), "6204");
        assertEquals(result.getResults().get(0).getName(), "test");
        assertEquals(result.getResults().get(0).getEdiId(), "3316");
        assertTrue(result.getResults().get(0).getIsProspect());
        assertFalse(result.getResults().get(0).getIsBranchCash());
        assertEquals(result.getMetadata().getTotalItems(), 4232);

    }

    @Test
    public void getCustomerSearch_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(CustomerSearchResponseDTO.class)))
                .thenThrow(HttpServerErrorException.class);
        assertThrows(HttpServerErrorException.class, () -> eclipseService.getCustomerSearch(TestCommon.mockCustomerSearchInputDTO()));

    }

    @Test
    public void getVarianceSummary_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceSummary.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.createSampleVariancesummary(), HttpStatus.OK));

        VarianceSummaryDTO result = eclipseService.getVarianceSummary("test");
        assertEquals(result.getNetTotalCost(), "9999.9");
        assertEquals(result.getGrossTotalCost(), "8923.0");
        assertEquals(result.getProductQuantity(), 20);
        assertEquals(result.getDifferenceQuantity(), 100);

    }

    @Test
    public void getVarianceSummary_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceSummary.class)))
                .thenThrow(InvalidSerializedProductException.class);
        assertThrows(InvalidSerializedProductException.class, () -> eclipseService.getVarianceSummary("test"));

    }

    @Test
    public void getVarianceDetails_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceDetailsDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.varianceDetailsDTO(), HttpStatus.OK));

        VarianceDetailsDTO result = eclipseService.getVarianceDetails("test");
        assertNotNull(result.getCounts());
        assertEquals(result.getCounts().get(0).getLocation(), "testLocation");
        assertEquals(result.getCounts().get(0).getErpProductID(), "testProduct");
        assertEquals(result.getCounts().get(0).getPercentDeviance(), 65.98);
        assertFalse(result.getCounts().get(0).getNotCountedFlag());

    }

    @Test
    public void getVarianceDetails_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceDetailsDTO.class)))
                .thenThrow(EclipseConnectException.class);
        assertThrows(EclipseConnectException.class, () -> eclipseService.getVarianceDetails("test"));

    }

    @Test
    public void stagePickTotePackages_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseTotePackagesDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.createSampleWarehouseTotePackagesDTO(), HttpStatus.OK));

        WarehouseTotePackagesDTO result = eclipseService.stagePickTotePackages(TestCommon.createSampleWarehouseTotePackagesDTO());
        assertNotNull(result.getPackageList());
        assertEquals(result.getTote(), "testTote");
        assertEquals(result.getInvoiceNumber(), "testInvoiceNumber");
        assertEquals(result.getBranchId(), "testBranch");

    }

    @Test
    public void stagePickTotePackages_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(WarehouseTotePackagesDTO.class)))
                .thenThrow(InvalidSerializedProductException.class);
        assertThrows(InvalidSerializedProductException.class, () -> eclipseService.stagePickTotePackages(TestCommon.createSampleWarehouseTotePackagesDTO()));

    }

    @Test
    public void getKourierProductSearch_Success() {

        Product product = new Product("3632", "disp", "8255", "test", "test.jpg");
        ProductSearchResponse productSearchResponse = new ProductSearchResponse("test", "test msg", 4123, Collections.singletonList(product));
        com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO productSearchResponseDTO = new com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO(Collections.singletonList(productSearchResponse));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(productSearchResponseDTO, HttpStatus.OK));

        com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO result = eclipseService.getKourierProductSearch("test", "disp", "test");
        assertNotNull(result.getProdSearch());
        assertEquals(result.getProdSearch().get(0).productIdCount, 4123);
        assertEquals(result.getProdSearch().get(0).getErrorCode(), "test");
        assertEquals(result.getProdSearch().get(0).getProducts().get(0).getProductId(), "3632");
        assertEquals(result.getProdSearch().get(0).getProducts().get(0).getPartNumber(), "8255");

    }

    @Test
    public void getKourierProductSearch_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO.class)))
                .thenThrow(InvalidEclipseCredentialsException.class);
        assertThrows(InvalidEclipseCredentialsException.class, () -> eclipseService.getKourierProductSearch("test", "disp", "test"));

    }

    @Test
    public void getShippingDetails_Success() {

        ShippingTextResponseDTO shippingTextResponseDTO = new ShippingTextResponseDTO("2556", "done", "test", "test msg", List.of("test instruction"), true, false);
        ShippingDetailsResponseDTO shippingDetailsResponseDTO = new ShippingDetailsResponseDTO(Collections.singletonList(shippingTextResponseDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ShippingDetailsResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(shippingDetailsResponseDTO, HttpStatus.OK));

        ShippingDetailsResponseDTO result = eclipseService.getShippingDetails("test");
        assertNotNull(result.getShippingtext());
        assertEquals(result.getShippingtext().get(0).getInvoiceNumber(), "2556");
        assertEquals(result.getShippingtext().get(0).getStatus(), "done");
        assertEquals(result.getShippingtext().get(0).getShippingInstructions().get(0), "test instruction");
        assertTrue(result.getShippingtext().get(0).getNoBackorder());

    }

    @Test
    public void getShippingDetails_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ShippingDetailsResponseDTO.class)))
                .thenThrow(NoEclipseCredentialsException.class);
        assertThrows(NoEclipseCredentialsException.class, () -> eclipseService.getShippingDetails("test"));

    }

}
