package com.reece.platform.inventory.util;

import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.dto.internal.MincronAllCountsDTO;
import com.reece.platform.inventory.dto.internal.MincronCountDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationSummaryDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.erpsystem.ERPSystem;
import com.reece.platform.inventory.external.eclipse.*;
import com.reece.platform.inventory.external.mincron.MincronAddToCountDTO;
import com.reece.platform.inventory.external.mincron.MincronItemDTO;
import com.reece.platform.inventory.external.mincron.MincronLocationDTO;
import com.reece.platform.inventory.external.mincron.MincronPostCountDTO;
import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.model.ProductSearch.ProductSearchMetadata;
import com.reece.platform.inventory.model.ProductSearch.ProductSearchResult;
import com.reece.platform.inventory.model.ProductSearch.ProductWebReference;
import com.reece.platform.inventory.model.variance.VarianceDetails;
import com.reece.platform.inventory.model.variance.VarianceSummary;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestCommon {

    public static final Date date = new Date();

    public static final UUID testUUID = UUID.fromString("7398275d-435a-4ade-b20e-1159e81ea49c");
    public static final UUID testUUID_2 = UUID.fromString("df3cb6ce-de47-4c98-9cad-fb5efb71cc8e");

    public static final WriteInDTO mockWriteInDTO_1 = new WriteInDTO(
        testUUID,
        "testing1",
        "testing1",
        "testing1",
        "testing1",
        "testing1",
        1,
        "testing1",
        false,
        "",
        date,
        "",
        date
    );

    public static final WriteInDTO mockWriteInDTO_2 = new WriteInDTO(
        testUUID_2,
        "testing2",
        "testing2",
        "testing2",
        "testing2",
        "testing2",
        2,
        "testing2",
        false,
        "",
        date,
        "",
        date
    );

    public static final VarianceSummaryDTO mockVarianceSummaryDTO = new VarianceSummaryDTO();

    public static final VarianceDetails mockVarianceDetails = new VarianceDetails();
    public static final VarianceDetailsDTO mockVarianceDetailsDTO = new VarianceDetailsDTO();

    public static final ArrayList<VarianceLocationSummaryDTO> mockVarianceLocationSummaryList = new ArrayList<>();
    public static final VarianceLocationSummaryDTO mockVarianceLocationSummary = new VarianceLocationSummaryDTO();
    public static final VarianceLocationDTO mockVarianceLocationDTO = new VarianceLocationDTO(
        "Testing",
        "Testing",
        21L,
        21L,
        21.00,
        21.00,
        false,
        new ArrayList<>()
    );
    public static final UpdateCountDTO mockUpdateCountDTO = new UpdateCountDTO("Testing", 1, "Testing");

    public static final CountItem mockCountItem = new CountItem();

    public static final LocationSummaryDTO mockLocationSummaryDTO = new LocationSummaryDTO("testing", false, 21L, 21L);

    public static final List<LocationSummaryDTO> mockLocationSummaryDTOList = List.of(
        mockLocationSummaryDTO,
        mockLocationSummaryDTO
    );

    public static final Count mockCount = new Count();

    public static final Branch mockBranch = new Branch();

    public static final CountStatusDTO mockCountStatusDTO = new CountStatusDTO(
        UUID.randomUUID(),
        "Testing",
        "Testing",
        "Testing",
        CountStatus.IN_PROGRESS,
        "Testing",
        date,
        21
    );

    public static final MetricsDTO mockMetricsDTO = new MetricsDTO(
        "COMPLETED",
        21,
        21,
        21,
        "2020-12-05 6:58:01", //will cause exception
        "2020-12-05 6:58:02",
        "Testing"
    );

    public static final LocationCount mockLocationCount = new LocationCount();

    public static final CountDTO mockCountDTO = new CountDTO(
        "Testing",
        "Testing",
        "Testing",
        ERPSystemName.ECLIPSE,
        CountStatus.IN_PROGRESS,
        "Testing",
        date
    );

    public static final LocationProductDTO mockLocationProductDTO = new LocationProductDTO(
        "Testing",
        "Testing",
        "Testing",
        "Testing",
        "Testing",
        "Testing",
        "Testing",
        21,
        CountLocationItemStatus.UNCOUNTED,
        21,
        "1"
    );

    public static final List<LocationProductDTO> mockLocationProductDTOList = List.of(mockLocationProductDTO);

    public static final LocationDTO mockLocationDTO = new LocationDTO(
        "testing",
        "testing",
        21L,
        21L,
        false,
        mockLocationProductDTOList
    );

    public static final LocationDTO mockLocationDTO_EmptyArray = new LocationDTO(
        "testing",
        "testing",
        21L,
        21L,
        false,
        new ArrayList<>()
    );

    public static final DeleteCountsDTO mockDeleteCountsDTO = new DeleteCountsDTO(ERPSystemName.MINCRON, "testing");

    public static final String ECLIPSE_BRANCH_ID = "1003";
    public static final String MINCRON_BRANCH_ID = "043";

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
    private static final String testCountId = "testCountId";
    private static final String testCountId2 = "testCountId2";
    private static final UUID uuid1 = UUID.randomUUID();
    private static final UUID uuid2 = UUID.randomUUID();

    public static final ERPSystem mockERPSystem = new ERPSystem() {
        @Override
        public CountDTO validateCount(String branchId, String countId) {
            return mockCountDTO;
        }

        @Override
        public List<LocationDTO> loadAllLocationItems(String branchId, String countId) {
            return List.of(mockLocationDTO, mockLocationDTO_EmptyArray);
        }

        @Override
        public List<LocationProductDTO> loadLocationItems(String branchId, String countId, String locationId) {
            return List.of(mockLocationProductDTO, mockLocationProductDTO);
        }

        @Override
        public void updateCount(String branchId, String countId, String locationId, List<PostCountDTO> itemsToUpdate) {}

        @Override
        public void addToCount(String branchId, String countId, String locationId, String prodNum, int quantity) {}
    };

    public static ResponseEntity<VarianceSummary> createSampleVariancesummaryResponse() {
        return new ResponseEntity<>(createSampleVariancesummary(), HttpStatus.OK);
    }

    public static VarianceSummary createSampleVariancesummary() {
        VarianceSummary testVarianceSummary = new VarianceSummary();
        testVarianceSummary.setTotalQuantity(100);
        testVarianceSummary.setGrossTotalVarianceCost(8923.0);
        testVarianceSummary.setNetTotalVarianceCost(9999.9);
        testVarianceSummary.setTotalNumberOfProducts(20);
        testVarianceSummary.setTotalNumberOfLocations(99);
        return testVarianceSummary;
    }

    public static ProductSearchResultDTO createProductSearchResultDTO() {
        ProductSearchResponseDTO productSearchResponseDTO = new ProductSearchResponseDTO();
        ProductSearchMetadata metadata = new ProductSearchMetadata();
        metadata.setStartIndex(0);
        metadata.setTotalItems(50);
        metadata.setPageSize(1);

        ProductSearchResult productSearchResult = new ProductSearchResult();
        productSearchResult.setId(99);
        productSearchResult.setDescription("Sample");
        productSearchResult.setCatalogNumber("sampleCatalogue");
        productSearchResult.setDescription("samleDescription");

        ImageUrls imageUrls = new ImageUrls();
        imageUrls.setLarge("THUMB");
        imageUrls.setMedium("MediumTHUMB");
        imageUrls.setThumb("LargeTHUMB");
        imageUrls.setSmall("smallTHUMB");

        ProductWebReference productWebReference = new ProductWebReference();
        productWebReference.setWebReferenceId("THUMB");
        productWebReference.setWebReferenceParameters("TextUrl");

        ProductWebReference[] webReferencesTestArr = new ProductWebReference[1];
        webReferencesTestArr[0] = productWebReference;

        productSearchResult.setWebReferences(webReferencesTestArr);

        productSearchResponseDTO.setMetadata(metadata);
        productSearchResponseDTO.setResults(Collections.singletonList(productSearchResult));

        return new ProductSearchResultDTO(productSearchResponseDTO);
    }

    public static final ProductSerialNumberRequestDTO createSampleProductSerialNumberRequestDTO() {
        ProductSerialNumberRequestDTO productSerialNumberRequestDTO = new ProductSerialNumberRequestDTO();
        productSerialNumberRequestDTO.setBranchId(testBranch);
        productSerialNumberRequestDTO.setSerialNumberList(Collections.singletonList(createSampleSerialList()));
        productSerialNumberRequestDTO.setWarehouseId(testWareHouseId);
        productSerialNumberRequestDTO.setIgnoreStockCheck(Boolean.TRUE);
        return productSerialNumberRequestDTO;
    }

    public static final SerialListDTO createSampleSerialList() {
        SerialListDTO serialListDTO = new SerialListDTO();
        serialListDTO.setSerial(testSerial);
        serialListDTO.setLine(1);
        return serialListDTO;
    }

    public static final WarehouseStagePickDTO createSampleWarehouseStagePickDTO() {
        WarehouseStagePickDTO warehouseStagePickDTO = new WarehouseStagePickDTO();
        warehouseStagePickDTO.setBranchId(testBranch);
        warehouseStagePickDTO.setInvoiceNumber(testInvoiceNumber);
        warehouseStagePickDTO.setTote(testTote);

        return warehouseStagePickDTO;
    }

    public static final WarehouseTotePackagesDTO createSampleWarehouseTotePackagesDTO() {
        WarehouseTotePackagesDTO warehouseTotePackagesDTO = new WarehouseTotePackagesDTO();
        warehouseTotePackagesDTO.setBranchId(testBranch);
        warehouseTotePackagesDTO.setInvoiceNumber(testInvoiceNumber);
        warehouseTotePackagesDTO.setTote(testTote);
        warehouseTotePackagesDTO.setPackageList(Collections.singletonList(createSamplePackageDTO()));
        return warehouseTotePackagesDTO;
    }

    public static final PackageDTO createSamplePackageDTO() {
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setPackageType(testPackageType);
        packageDTO.setPackageQuantity(1);
        return packageDTO;
    }

    public static final WarehouseCloseTaskRequestDTO createSampleWarehouseCloseTaskRequestDTO() {
        WarehouseCloseTaskRequestDTO warehouseCloseTaskRequestDTO = new WarehouseCloseTaskRequestDTO();
        warehouseCloseTaskRequestDTO.setBranchId(testBranch);
        warehouseCloseTaskRequestDTO.setInvoiceNumber(testInvoiceNumber);
        warehouseCloseTaskRequestDTO.setTote(testTote);
        warehouseCloseTaskRequestDTO.setSkipStagedWarningFlag(Boolean.TRUE);
        warehouseCloseTaskRequestDTO.setSkipInvalidLocationWarningFlag(Boolean.TRUE);
        warehouseCloseTaskRequestDTO.setUpdateLocationOnly(Boolean.TRUE);
        return warehouseCloseTaskRequestDTO;
    }

    public static final WarehousePickCompleteDTO createSampleWarehousePickCompleteDTO() {
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

    public static CustomerSearchInputDTO mockCustomerSearchInputDTO() {
        CustomerSearchInputDTO customerSearchInputDTO = new CustomerSearchInputDTO();
        customerSearchInputDTO.setId(List.of("1013"));
        customerSearchInputDTO.setKeyword("test");
        customerSearchInputDTO.setPageSize(10);
        customerSearchInputDTO.setCurrentPage(1);
        return customerSearchInputDTO;
    }

    public static  final MincronPostCountDTO mockMincronPostCountDTO = new MincronPostCountDTO("8662", "test", 442);

    public static final MincronAddToCountDTO mockMincronAddToCountDTO = new MincronAddToCountDTO("7352", "8355", "8662", "5242", 465);

    public static EclipseCountStatusDTO eclipseCountStatusDTO() {
        EclipseCountStatusDTO eclipseCountStatusDTO = new EclipseCountStatusDTO();
        eclipseCountStatusDTO.setBranchNum("test");
        eclipseCountStatusDTO.setNumItems(6373);
        eclipseCountStatusDTO.setCountId("test");
        eclipseCountStatusDTO.setCountDate("2023-04-29");
        return eclipseCountStatusDTO;
    }

    public static EclipseProductDTO eclipseProductDTO() {
        EclipseProductDTO eclipseProductDTO = new EclipseProductDTO();
        eclipseProductDTO.setProductId("test");
        eclipseProductDTO.setProductDescription("test desc");
        eclipseProductDTO.setUom("I");
        eclipseProductDTO.setImageUrl("test.jpg");
        eclipseProductDTO.setCatalogNumber("test");
        eclipseProductDTO.setControlNum("test");
        eclipseProductDTO.setLocationId("test");
        return eclipseProductDTO;
    }

    public static EclipseCountDTO eclipseCountDTO() {
        EclipseCountDTO eclipseCountDTO = new EclipseCountDTO();
        eclipseCountDTO.setCountDescription("test desc");
        eclipseCountDTO.setProducts(Collections.singletonList(eclipseProductDTO()));
        eclipseCountDTO.setCreatedAt("2023-04-13");
        eclipseCountDTO.setBranchId("test");
        eclipseCountDTO.setCountId(5446);
        return eclipseCountDTO;
    }

    public static EclipseBatchDTO eclipseBatchDTO() {
        EclipseBatchDTO eclipseBatchDTO = new EclipseBatchDTO();
        eclipseBatchDTO.setCountId("2324");
        eclipseBatchDTO.setBranchName("test");
        eclipseBatchDTO.setBranchId("7462");
        return eclipseBatchDTO;
    }

    public static EclipseLocationItemDTO eclipseLocationItemDTO() {
        EclipseLocationItemDTO eclipseLocationItemDTO = new EclipseLocationItemDTO();
        eclipseLocationItemDTO.setControlNum("6434");
        eclipseLocationItemDTO.setImageUrl("tst.jpg");
        eclipseLocationItemDTO.setDescription("test desc");
        eclipseLocationItemDTO.setProductId("7432");
        eclipseLocationItemDTO.setCatalogNumber("7245");
        eclipseLocationItemDTO.setUnitOfMeasureName("test");
        eclipseLocationItemDTO.setUnitOfMeasureQuantity("32");
        return eclipseLocationItemDTO;
    }

    public static ProductSearchMetadata productSearchMetadata() {
        ProductSearchMetadata productSearchMetadata = new ProductSearchMetadata();
        productSearchMetadata.setPageSize(2);
        productSearchMetadata.setTotalItems(10);
        productSearchMetadata.setStartIndex(0);
        return productSearchMetadata;
    }

    public static ProductSearchResult productSearchResult() {
        ProductWebReference productWebReference = new ProductWebReference();
        productWebReference.setWebReferenceId("THUMB");
        productWebReference.setWebReferenceDescription("test desc");
        productWebReference.setWebReferenceParameters("test.icon");
        ProductWebReference[] productWebReferences = new ProductWebReference[]{productWebReference};
        ProductSearchResult productSearchResult = new ProductSearchResult();
        productSearchResult.setDescription("test dsc");
        productSearchResult.setUpc("test");
        productSearchResult.setCatalogNumber("6225");
        productSearchResult.setId(3526);
        productSearchResult.setWebReferences(productWebReferences);
        return productSearchResult;
    }

    public static ProductSearchRequestDTO productSearchRequestDTO() {
        ProductSearchRequestDTO productSearchRequestDTO = new ProductSearchRequestDTO();
        productSearchRequestDTO.setSearchTerm("test data");
        productSearchRequestDTO.setCurrentPage(1);
        productSearchRequestDTO.setPageSize(10);
        productSearchRequestDTO.setSearchInputType(3);
        productSearchRequestDTO.setErpSystem("ECLIPSE");
        productSearchRequestDTO.setCategoryLevel(1);
        productSearchRequestDTO.setResultFields(new ArrayList<>());
        return productSearchRequestDTO;
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

    public static EclipseSearchMetadata eclipseSearchMetadata() {
        EclipseSearchMetadata eclipseSearchMetadata = new EclipseSearchMetadata();
        eclipseSearchMetadata.setPageSize(10);
        eclipseSearchMetadata.setTotalItems(4232);
        eclipseSearchMetadata.setStartIndex(0);
        return eclipseSearchMetadata;
    }

    public static CustomerSearchResult customerSearchResult() {
        ShipToId shipToId = new ShipToId();
        shipToId.setShipToId(3932);
        CustomerSearchResult customerSearchResult = new CustomerSearchResult();
        customerSearchResult.setName("test");
        customerSearchResult.setEdiId("3316");
        customerSearchResult.setIsBranchCash(false);
        customerSearchResult.setIsProspect(true);
        customerSearchResult.setId("6204");
        customerSearchResult.setIsBillTo(true);
        customerSearchResult.setIsShipTo(true);
        customerSearchResult.setShipToLists(Collections.singletonList(shipToId));
        return customerSearchResult;
    }

    public static VarianceDetailsDTO varianceDetailsDTO() {
        VarianceDetails varianceDetails = new VarianceDetails();
        varianceDetails.setLocation("testLocation");
        varianceDetails.setErpProductID("testProduct");
        varianceDetails.setPercentDeviance(65.98);
        varianceDetails.setNotCountedFlag(false);
        varianceDetails.setOnHandCost(55.0);
        varianceDetails.setQtyDeviance(41);
        VarianceDetailsDTO varianceDetailsDTO = new VarianceDetailsDTO();
        varianceDetailsDTO.setCounts(Collections.singletonList(varianceDetails));
        return varianceDetailsDTO;
    }

    public static MincronAllCountsDTO mincronAllCountsDTO() {
        MincronCountDTO mincronCountDTO = new MincronCountDTO();
        mincronCountDTO.setCountDate("2023-04-29");
        mincronCountDTO.setCountId("7352");
        mincronCountDTO.setBranchNum("8342");
        mincronCountDTO.setNumItems(422);
        MincronAllCountsDTO mincronAllCountsDTO = new MincronAllCountsDTO();
        mincronAllCountsDTO.setCounts(Collections.singletonList(mincronCountDTO));
        mincronAllCountsDTO.setMoreThan100Counts(false);
        return mincronAllCountsDTO;
    }

    public static com.reece.platform.inventory.external.mincron.MincronCountDTO mincronCountDTO() {
        com.reece.platform.inventory.external.mincron.MincronCountDTO mincronCountDTO = new com.reece.platform.inventory.external.mincron.MincronCountDTO();
        mincronCountDTO.setBranchName("test");
        mincronCountDTO.setBranchNumber("8355");
        mincronCountDTO.setCountId("7352");
        return mincronCountDTO;
    }

    public static MincronLocationDTO mincronLocationDTO() {
        MincronItemDTO mincronItemDTO = new MincronItemDTO();
        mincronItemDTO.setItemNum("6724");
        mincronItemDTO.setUom("I");
        mincronItemDTO.setTagNum("7366");
        mincronItemDTO.setCatalogNum("9622");
        mincronItemDTO.setProdNum("5673");
        mincronItemDTO.setProdDesc("test desc");
        MincronLocationDTO mincronLocationDTO = new MincronLocationDTO();
        mincronLocationDTO.setLocationId("8662");
        mincronLocationDTO.setItems(Collections.singletonList(mincronItemDTO));
        mincronLocationDTO.setTotalQuantity(4123);
        mincronLocationDTO.setItemCount(67);
        return mincronLocationDTO;
    }

    public static List<Count> createSampleListOfCounts() {
        Count count1 = new Count();
        Count count2 = new Count();
        Branch branch1 = new Branch();
        Branch branch2 = new Branch();
        List<Count> countList = new ArrayList<>();
        countList.add(count1);
        countList.add(count2);
        count1.setStatus(CountStatus.COMPLETE);
        count1.setErpCountId(testCountId);
        count1.setId(uuid1);
        count1.setBranch(branch1);
        count2.setStatus(CountStatus.COMPLETE);
        count2.setErpCountId(testCountId2);
        count2.setId(uuid2);
        count2.setBranch(branch2);
        branch1.setErpBranchNum(testBranch);
        branch2.setErpBranchNum(testBranch + "2");
        return countList;
    }

}
