package com.reece.platform.picking.service;

import com.reece.platform.picking.dto.*;
import com.reece.platform.picking.dto.kourier.ShippingDetailsResponseDTO;
import com.reece.platform.picking.dto.kourier.ShippingTextResponseDTO;
import com.reece.platform.picking.exception.EclipseConnectException;
import com.reece.platform.picking.exception.InvalidEclipseCredentialsException;
import com.reece.platform.picking.exception.InvalidSerializedProductException;
import com.reece.platform.picking.exception.NoEclipseCredentialsException;
import com.reece.platform.picking.external.eclipse.EclipseService;
import com.reece.platform.picking.mapper.WarehouseCloseTaskMapper;
import com.reece.platform.picking.util.TestCommon;
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
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.reece.platform.picking.util.TestCommon.mockCloseOrderRequestDTO;
import static com.reece.platform.picking.util.TestCommon.mockPickingTaskDTO;
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
    private EclipseService eclipseService = new EclipseService(restTemplate, warehouseCloseTaskMapper);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(eclipseService, "eclipseBaseUrl", "test.com");
        ReflectionTestUtils.setField(eclipseService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(eclipseService, "warehouseCloseTaskMapper", warehouseCloseTaskMapper);
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
