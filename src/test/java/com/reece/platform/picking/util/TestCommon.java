package com.reece.platform.picking.util;

import com.reece.platform.picking.dto.*;

import java.util.*;

public class TestCommon {

    private static final String testSerial = "TestSerial";
    public static final String testBranch = "testBranch";
    private static final String testWareHouseId = "TestWareHouseId";
    private static final String testInvoiceNumber = "testInvoiceNumber";
    private static final String testTote = "testTote";
    private static final String testPackageType = "testPackageType";
    private static final String testProductId = "testProductId";
    private static final String testDescription = "testDescription";
    private static final String testUom = "testUom";
    private static final String testLocation = "testLocation";
    private static final String testLocationType = "testLocationType";
    private static final String testLot = "testLot";
    private static final String testChipVia = "testChipVia";
    private static final String testuserId = "testuserId";
    private static final String testStartPickTime = "testStartPickTime";

    public static ProductSerialNumberRequestDTO createSampleProductSerialNumberRequestDTO() {
        ProductSerialNumberRequestDTO productSerialNumberRequestDTO = new ProductSerialNumberRequestDTO();
        productSerialNumberRequestDTO.setBranchId(testBranch);
        productSerialNumberRequestDTO.setSerialNumberList(Collections.singletonList(createSampleSerialList()));
        productSerialNumberRequestDTO.setWarehouseId(testWareHouseId);
        productSerialNumberRequestDTO.setIgnoreStockCheck(Boolean.TRUE);
        return productSerialNumberRequestDTO;
    }

    public static SerialListDTO createSampleSerialList() {
        SerialListDTO serialListDTO = new SerialListDTO();
        serialListDTO.setSerial(testSerial);
        serialListDTO.setLine(1);
        return serialListDTO;
    }

    public static WarehouseTotePackagesDTO createSampleWarehouseTotePackagesDTO() {
        WarehouseTotePackagesDTO warehouseTotePackagesDTO = new WarehouseTotePackagesDTO();
        warehouseTotePackagesDTO.setBranchId(testBranch);
        warehouseTotePackagesDTO.setInvoiceNumber(testInvoiceNumber);
        warehouseTotePackagesDTO.setTote(testTote);
        warehouseTotePackagesDTO.setPackageList(Collections.singletonList(createSamplePackageDTO()));
        return warehouseTotePackagesDTO;
    }

    public static PackageDTO createSamplePackageDTO() {
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setPackageType(testPackageType);
        packageDTO.setPackageQuantity(1);
        return packageDTO;
    }

    public static WarehouseCloseTaskRequestDTO createSampleWarehouseCloseTaskRequestDTO() {
        WarehouseCloseTaskRequestDTO warehouseCloseTaskRequestDTO = new WarehouseCloseTaskRequestDTO();
        warehouseCloseTaskRequestDTO.setBranchId(testBranch);
        warehouseCloseTaskRequestDTO.setInvoiceNumber(testInvoiceNumber);
        warehouseCloseTaskRequestDTO.setTote(testTote);
        warehouseCloseTaskRequestDTO.setSkipStagedWarningFlag(Boolean.TRUE);
        warehouseCloseTaskRequestDTO.setSkipInvalidLocationWarningFlag(Boolean.TRUE);
        warehouseCloseTaskRequestDTO.setUpdateLocationOnly(Boolean.TRUE);
        return warehouseCloseTaskRequestDTO;
    }

    public static WarehousePickCompleteDTO createSampleWarehousePickCompleteDTO() {
        WarehousePickCompleteDTO warehousePickCompleteDTO = new WarehousePickCompleteDTO();
        warehousePickCompleteDTO.setProductId(testProductId);
        warehousePickCompleteDTO.setDescription(testDescription);
        warehousePickCompleteDTO.setQuantity(1);
        warehousePickCompleteDTO.setUom(testUom);
        warehousePickCompleteDTO.setLocation(testLocation);
        warehousePickCompleteDTO.setLocationType(testLocationType);
        warehousePickCompleteDTO.setLot(testLot);
        warehousePickCompleteDTO.setLineId(1);
        warehousePickCompleteDTO.setShipVia(testChipVia);
        warehousePickCompleteDTO.setUserId(testuserId);
        warehousePickCompleteDTO.setBranchId(testBranch);
        warehousePickCompleteDTO.setWarehouseID(testWareHouseId);
        warehousePickCompleteDTO.setSerial(true);
        warehousePickCompleteDTO.setOverrideProduct(true);
        warehousePickCompleteDTO.setStartPickTime(testStartPickTime);
        warehousePickCompleteDTO.setIgnoreLockToteCheck(Boolean.TRUE);
        return warehousePickCompleteDTO;
    }

    public static final PickingTaskDTO mockPickingTaskDTO = new PickingTaskDTO("2557", 34, "8832", "5232", "test","testUser",43,86,"test", "7432", "test",false, 23.76, "Stage");

    public static final PickingTaskWarningDTO mockPickingTaskWarningDTO = new PickingTaskWarningDTO("9743", 423, "test info");

    public static PickTasksListDTO mockPickTasksListDTO() {
        PickTasksListDTO pickTasksListDTO = new PickTasksListDTO();
        pickTasksListDTO.setWarehousePickTasksList(Collections.singletonList(mockPickingTaskDTO));
        pickTasksListDTO.setWarehousePickTasksWarnings(Collections.singletonList(mockPickingTaskWarningDTO));
        return pickTasksListDTO;
    }

    public static ProductSerialNumberDTO mockProductSerialNumberDTO() {
        ProductSerialNumberDTO mockProductSerialNumberDTO = new ProductSerialNumberDTO();
        mockProductSerialNumberDTO.setProductId("3632");
        mockProductSerialNumberDTO.setNonStockSerialNumbers(Collections.singletonList(createSampleSerialList()));
        mockProductSerialNumberDTO.setLocation("8322");
        mockProductSerialNumberDTO.setSerialList(Collections.singletonList(createSampleSerialList()));
        mockProductSerialNumberDTO.setDescription("test desc");
        mockProductSerialNumberDTO.setGenerationId("9425");
        mockProductSerialNumberDTO.setQuantity("5");
        mockProductSerialNumberDTO.setWarehouseId("41567");
        mockProductSerialNumberDTO.setOrderId("8525");
        mockProductSerialNumberDTO.setInvoiceId("2556");
        return mockProductSerialNumberDTO;
    }

    public static ProductSerialNumbersResponseDTO mockProductSerialNumbersResponseDTO() {
        ProductSerialNumbersResponseDTO productSerialNumbersResponseDTO = new ProductSerialNumbersResponseDTO();
        productSerialNumbersResponseDTO.setResults(Collections.singletonList(mockProductSerialNumberDTO()));
        return productSerialNumbersResponseDTO;
    }

    public static final CloseOrderRequestDTO mockCloseOrderRequestDTO = new CloseOrderRequestDTO("4145", "7526");

    public static WarehouseToteTaskDTO mockWarehouseToteTaskDTO() {
        WarehouseToteTaskDTO mockWarehouseToteTaskDTO = new WarehouseToteTaskDTO();
        mockWarehouseToteTaskDTO.setTote(testTote);
        mockWarehouseToteTaskDTO.setTaskStatus("testStatus");
        mockWarehouseToteTaskDTO.setLocation(testLocation);
        mockWarehouseToteTaskDTO.setBranchId(testBranch);
        mockWarehouseToteTaskDTO.setInvoiceNumber(testInvoiceNumber);
        mockWarehouseToteTaskDTO.setUpdateLocationOnly(true);
        return mockWarehouseToteTaskDTO;
    }

    public static WarehouseUserPickDTO warehouseUserPickDTO() {
        WarehouseUserPickDTO warehouseUserPickDTO = new WarehouseUserPickDTO();
        warehouseUserPickDTO.setWarehouseID("test");
        warehouseUserPickDTO.setLocation("test");
        warehouseUserPickDTO.setDescription("7462");
        warehouseUserPickDTO.setUserId("test");
        warehouseUserPickDTO.setBranchId("2256");
        warehouseUserPickDTO.setLot("test");
        warehouseUserPickDTO.setTote("test");
        warehouseUserPickDTO.setUom("I");
        warehouseUserPickDTO.setPickGroup("test");
        warehouseUserPickDTO.setQuantity(32);
        warehouseUserPickDTO.setLineId(9624);
        warehouseUserPickDTO.setProductId("2324");
        warehouseUserPickDTO.setCutDetail("test");
        warehouseUserPickDTO.setCutGroup("test");
        warehouseUserPickDTO.setGenerationId(8353);
        warehouseUserPickDTO.setSplitId("test");
        warehouseUserPickDTO.setShipVia("test");
        warehouseUserPickDTO.setOrderId("8435");
        warehouseUserPickDTO.setIsLot("test");
        warehouseUserPickDTO.setIsParallelCut(true);
        warehouseUserPickDTO.setIsSerial(false);
        warehouseUserPickDTO.setLocationType("test");
        return warehouseUserPickDTO;
    }

}
