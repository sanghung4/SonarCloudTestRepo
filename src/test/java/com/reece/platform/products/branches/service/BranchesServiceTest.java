package com.reece.platform.products.branches.service;

import static com.reece.platform.products.testConstant.TestConstants.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.google.maps.errors.ApiException;
import com.reece.platform.products.branches.exception.BranchNotFoundException;
import com.reece.platform.products.branches.exception.WorkdayException;
import com.reece.platform.products.branches.model.DTO.*;
import com.reece.platform.products.branches.model.entity.Branch;
import com.reece.platform.products.branches.model.enums.MileRadiusEnum;
import com.reece.platform.products.branches.model.repository.BranchesDAO;
import com.reece.platform.products.branches.utilities.BrandDomainMapper;
import com.reece.platform.products.model.DTO.GetTerritoryResponseDTO;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import com.reece.platform.products.service.ErpService;
import java.io.IOException;
import java.util.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

public class BranchesServiceTest {

    TestLogger testLogger = TestLoggerFactory.getTestLogger(BranchesService.class);

    @Captor
    private ArgumentCaptor<Collection<Branch>> listBranchesArgumentCaptor;

    @Captor
    private ArgumentCaptor<Branch> branchArgumentCaptor;

    @Mock
    private BranchesDAO branchesDAO;

    @Mock
    private DataWarehouseRepository dataWarehouseRepository;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private BrandDomainMapper brandDomainMapper;

    @Mock
    private ErpService erpService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private BranchesService branchesService;
    private List<Branch> branches;
    private UUID uuid;
    private Branch newBranch;
    private Branch deletedBranch;
    private Point mockLocation;

    private static final String TEST_LOCATION_CODE = "123";
    private static final String WORKDAY_FLAG_TRUE = "X";
    private static final String WORKDAY_FLAG_FALSE = "";
    private static final String TEST_MANAGER = "Manager";
    private static final String TEST_MANAGER_EMAIL = "Manager@test.com";
    private static final String TEST_MANAGER_PHONE = "222-222-2222";
    private static final String TEST_WORKDAY_BRAND = "Fortiline Waterworks";
    private static final String TEST_WORKDAY_ADDRESS_1 = "123 Street";
    private static final String TEST_WORKDAY_ADDRESS_2 = "APT 1";
    private static final String TEST_WORKDAY_CITY = "APT 1";
    private static final String TEST_WORKDAY_CITY_ALT = "APT 2";
    private static final String TEST_WORKDAY_ZIPCODE = "APT 1";
    private static final String TEST_WORKDAY_STATE = "APT 1";
    private static final String TEST_WORKDAY_PHONE = "APT 1";
    private static final String TEST_LOCATION_ATTRIBUTES = "APT 1";

    @SneakyThrows
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        branchesService =
            new BranchesService(
                dataWarehouseRepository,
                branchesDAO,
                geocodingService,
                brandDomainMapper,
                erpService,
                restTemplateBuilder
            );
        ReflectionTestUtils.setField(branchesService, "workdayEndpoint", "http://workday.com");

        testLogger.clearAll();

        var geometryFactory = new GeometryFactory();
        mockLocation = geometryFactory.createPoint(new Coordinate(10, 10));

        when(geocodingService.getLocationFromAddress(anyString())).thenReturn(mockLocation);
        when(branchesDAO.findAll()).thenReturn(CreateBranches(mockLocation));
        when(dataWarehouseRepository.getAllBranches()).thenReturn(CreatePDWBranches());

        Branch branch = new Branch();
        branch.setBranchId("123123");
        branch.setDistance(20f);
        branch.setName("Fortiline Waterworks");
        branch.setLocation(mockLocation);
        branch.setIsActive(true);
        Branch branch2 = new Branch();
        branch2.setBranchId("456456");
        branch2.setDistance(15f);
        branch2.setLocation(mockLocation);
        branch2.setName("Todd Pipe - TEST");
        branch2.setIsActive(true);
        Branch branch3 = new Branch();
        branch3.setBranchId("585858");
        branch3.setDistance(30f);
        branch3.setName("Morrison Supply");
        branch3.setLocation(mockLocation);
        branch3.setIsActive(true);

        deletedBranch = new Branch();
        deletedBranch.setBranchId("148284");
        deletedBranch.setDistance(25f);
        deletedBranch.setName("Deleted");
        deletedBranch.setLocation(mockLocation);
        deletedBranch.setIsActive(false);

        branches = List.of(branch, branch2, branch3, deletedBranch);

        uuid = UUID.randomUUID();
        newBranch = new Branch();
        newBranch.setBranchId(BRANCH_ID_ONE);
        newBranch.setEntityId("6");
        newBranch.setName(BRANCH_NAME_ONE);
        newBranch.setAddress1(BRANCH_ADDRESS_ONE);
        newBranch.setAddress2(BRANCH_ADDRESS_TWO);
        newBranch.setCity(BRANCH_CITY_ONE);
        newBranch.setState(BRANCH_STATE_ONE);
        newBranch.setZip(BRANCH_ZIP_ONE);
        newBranch.setPhone("(817) 336-0451");
        newBranch.setErpSystemName(ErpEnum.valueOf("ECLIPSE"));
        newBranch.setWebsite("");
        newBranch.setWorkdayId("");
        newBranch.setWorkdayLocationName("");
        newBranch.setRvp("James Healy (05864)");
        newBranch.setIsPlumbing(true);
        newBranch.setIsWaterworks(false);
        newBranch.setIsHvac(false);
        newBranch.setIsBandK(false);
        newBranch.setActingBranchManager("Michael Gilder");
        newBranch.setActingBranchManagerPhone("+1 (817) 9054323 x1043500");
        newBranch.setActingBranchManagerEmail(BRANCH_MANAGER_EMAIL_ONE);
        newBranch.setBusinessHours(BRANCH_BUSINESS_HOURS_ONE);
        newBranch.setIsActive(true);
        newBranch.setIsAvailableInStoreFinder(true);
        newBranch.setIsShoppable(true);
        newBranch.setIsPricingOnly(false);
        newBranch.setId(uuid);
    }

    @Test
    void getBranch_ToddPipeBrandMapsCorrectly() {
        Branch branch = new Branch();
        branch.setBranchId("123123");
        branch.setName("TODD PIPE - ANAHEIM CA");
        branch.setLocation(mockLocation);
        when(branchesDAO.findAllByBranchId(anyString())).thenReturn(Collections.singletonList(branch));

        BranchResponseDTO result = branchesService.getBranch("123123");
        assertEquals(result.getBrand(), "Todd Pipe");
    }

    @Test
    void getBranch_success() {
        Branch branch = new Branch();
        branch.setBranchId("123123");
        branch.setName("Fortiline Waterworks");
        branch.setLocation(mockLocation);
        when(branchesDAO.findAllByBranchId(anyString())).thenReturn(Collections.singletonList(branch));

        BranchResponseDTO result = branchesService.getBranch("123123");
        assertEquals(result.getBranchId(), "123123");
        assertEquals(result.getLatitude(), 10.0);
        assertEquals(result.getLongitude(), 10.0);
    }

    @Test
    void getBranch_willReturnDeletedBranch() {
        when(branchesDAO.findAllByBranchId(anyString())).thenReturn(Collections.singletonList(deletedBranch));

        BranchResponseDTO result = branchesService.getBranch(deletedBranch.getBranchId());
        assertEquals(result.getBranchId(), "148284");
        assertEquals(result.getLatitude(), 10.0);
        assertEquals(result.getLongitude(), 10.0);
    }

    @Test
    void getBranch_notFound() {
        when(branchesDAO.findAllByBranchId(anyString())).thenReturn(new ArrayList<>());

        assertThrows(BranchNotFoundException.class, () -> branchesService.getBranch("123123"));
    }

    @Test
    void getBranches_nullRadiusDefaultsTo200Miles() throws Exception {
        String branchIdInRange = "123321";
        Branch branchInRange = new Branch();
        branchInRange.setBranchId(branchIdInRange);
        branchInRange.setDistance(199f);
        branchInRange.setName("Fortiline Waterworks");
        branchInRange.setLocation(mockLocation);
        branchInRange.setIsActive(true);

        String branchIdOutOfRange = "563828";
        Branch branchOutOfRange = new Branch();
        branchOutOfRange.setBranchId(branchIdOutOfRange);
        branchOutOfRange.setDistance(201f);
        branchOutOfRange.setName("Fortiline Waterworks");
        branchOutOfRange.setLocation(mockLocation);
        branchOutOfRange.setIsActive(true);
        when(branchesDAO.findByLocation(any(), any(), any()))
            .thenReturn(Arrays.asList(branchInRange, branchOutOfRange));

        var result = branchesService.getBranches(new BranchesRequestDTO());
        assertEquals(result.getBranches().get(0).getBranchId(), branchIdInRange);
        assertEquals(result.getBranches().size(), 1);
    }

    @Test
    void getBranches_validMileRadius() throws Exception {
        BranchesRequestDTO branchGeolocationRequestDTO = new BranchesRequestDTO();
        branchGeolocationRequestDTO.setBranchSearchRadius(MileRadiusEnum.MILES_50);
        String branchIdInRange = "123321";
        Branch branchInRange = new Branch();
        branchInRange.setBranchId(branchIdInRange);
        branchInRange.setDistance(49f);
        branchInRange.setName("Fortiline Waterworks");
        branchInRange.setLocation(mockLocation);
        branchInRange.setIsActive(true);

        String branchIdOutOfRange = "563828";
        Branch branchOutOfRange = new Branch();
        branchOutOfRange.setBranchId(branchIdOutOfRange);
        branchOutOfRange.setDistance(51f);
        branchOutOfRange.setName("Fortiline Waterworks");
        branchOutOfRange.setLocation(mockLocation);
        branchOutOfRange.setIsActive(true);
        when(branchesDAO.findByLocation(any(), any(), any()))
            .thenReturn(Arrays.asList(branchInRange, branchOutOfRange));

        var result = branchesService.getBranches(branchGeolocationRequestDTO);
        assertEquals(result.getBranches().get(0).getBranchId(), branchIdInRange);
        assertEquals(result.getBranches().size(), 1);
    }

    @Test
    void getBranches_withTerritory_success() throws Exception {
        var request = new BranchesRequestDTO();
        var territoryResponseDTO = new GetTerritoryResponseDTO();
        territoryResponseDTO.setBranchList(List.of("123123"));
        request.setTerritory("TEST_TERRITORY");
        when(branchesDAO.findByLocation(any(), any(), any())).thenReturn(branches);
        when(erpService.getTerritoryById(any())).thenReturn(territoryResponseDTO);

        var result = branchesService.getBranches(request);
    }

    @Test
    void getBranches_withoutTerritory_success() throws Exception {
        var request = new BranchesRequestDTO();
        request.setTerritory("");
        when(branchesDAO.findByLocation(any(), any(), any())).thenReturn(branches);

        var result = branchesService.getBranches(request);
        assertEquals(result.getBranches().get(0).getBranchId(), "123123");
        assertEquals(3, result.getBranches().size());
    }

    @Test
    void getBranches_withTerritoryException_success() throws Exception {
        var request = new BranchesRequestDTO();
        request.setTerritory("TEST_TERRITORY");
        when(branchesDAO.findByLocation(any(), any(), any())).thenReturn(branches);
        when(erpService.getTerritoryById(any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        var result = branchesService.getBranches(request);
        assertEquals(result.getBranches().get(0).getBranchId(), "123123");
        assertEquals(result.getBranches().get(1).getBranchId(), "456456");
        assertEquals(result.getBranches().size(), 3);
    }

    @Test
    void syncWithSnowflake_successWithCreateBranch() {
        when(branchesDAO.findByBranchId("1004")).thenReturn(branches.get(0));

        when(
            branchesDAO.createBranch(
                BRANCH_ID_ONE,
                "6",
                BRANCH_NAME_ONE,
                BRANCH_ADDRESS_ONE,
                BRANCH_ADDRESS_TWO,
                BRANCH_CITY_ONE,
                BRANCH_STATE_ONE,
                BRANCH_ZIP_ONE,
                "(817) 336-0451",
                new GeometryFactory().createPoint(new Coordinate(10, 10)),
                "ECLIPSE",
                "",
                "",
                "",
                "James Healy (05864)",
                true,
                false,
                false,
                false,
                "Michael Gilder",
                "+1 (817) 9054323 x1043500",
                BRANCH_MANAGER_EMAIL_ONE,
                BRANCH_BUSINESS_HOURS_ONE,
                true,
                true,
                true,
                false
            )
        )
            .thenReturn(newBranch);

        //calling the main method
        branchesService.syncWithSnowflake();

        assertSame(
            branchesDAO.createBranch(
                BRANCH_ID_ONE,
                "6",
                BRANCH_NAME_ONE,
                BRANCH_ADDRESS_ONE,
                BRANCH_ADDRESS_TWO,
                BRANCH_CITY_ONE,
                BRANCH_STATE_ONE,
                BRANCH_ZIP_ONE,
                "(817) 336-0451",
                new GeometryFactory().createPoint(new Coordinate(10, 10)),
                "ECLIPSE",
                "",
                "",
                "",
                "James Healy (05864)",
                true,
                false,
                false,
                false,
                "Michael Gilder",
                "+1 (817) 9054323 x1043500",
                BRANCH_MANAGER_EMAIL_ONE,
                BRANCH_BUSINESS_HOURS_ONE,
                true,
                true,
                true,
                false
            ),
            newBranch
        );

        verify(branchesDAO, times(1)).findByBranchId("1004");
        assertNotNull(branchesDAO.findByBranchId("1004"));
        assertSame(branchesDAO.findByBranchId("1004"), branches.get(0));
    }

    @Test
    void syncWithSnowflake_successWithUpdateBranch() {
        when(branchesDAO.findByBranchId("1004")).thenReturn(branches.get(0));

        when(
            branchesDAO.updateBranch(
                BRANCH_ID_ONE,
                "6",
                BRANCH_NAME_ONE,
                BRANCH_ADDRESS_ONE,
                BRANCH_ADDRESS_TWO,
                BRANCH_CITY_ONE,
                BRANCH_STATE_ONE,
                BRANCH_ZIP_ONE,
                "(817) 336-0451",
                new GeometryFactory().createPoint(new Coordinate(10, 10)),
                "ECLIPSE",
                "",
                "",
                "",
                "James Healy (05864)",
                true,
                false,
                false,
                false,
                "Michael Gilder",
                "+1 (817) 9054323 x1043500",
                BRANCH_MANAGER_EMAIL_ONE,
                BRANCH_BUSINESS_HOURS_ONE,
                true,
                true,
                true,
                false,
                uuid
            )
        )
            .thenReturn(newBranch);

        //calling the main method
        branchesService.syncWithSnowflake();

        assertNotNull(newBranch);

        assertSame(
            branchesDAO.updateBranch(
                BRANCH_ID_ONE,
                "6",
                BRANCH_NAME_ONE,
                BRANCH_ADDRESS_ONE,
                BRANCH_ADDRESS_TWO,
                BRANCH_CITY_ONE,
                BRANCH_STATE_ONE,
                BRANCH_ZIP_ONE,
                "(817) 336-0451",
                new GeometryFactory().createPoint(new Coordinate(10, 10)),
                "ECLIPSE",
                "",
                "",
                "",
                "James Healy (05864)",
                true,
                false,
                false,
                false,
                "Michael Gilder",
                "+1 (817) 9054323 x1043500",
                BRANCH_MANAGER_EMAIL_ONE,
                BRANCH_BUSINESS_HOURS_ONE,
                true,
                true,
                true,
                false,
                uuid
            ),
            newBranch
        );

        verify(branchesDAO, times(1)).findByBranchId("1004");
        assertNotNull(branchesDAO.findByBranchId("1004"));
        assertSame(branchesDAO.findByBranchId("1004"), branches.get(0));
    }

    @Test
    void syncWithSnowflake_shouldFlagBranchesAsDeleted() {
        var branchToDelete = new Branch(
            "999",
            "Deleted Branch",
            "123 Deleted St",
            "Dallas",
            "TX",
            "75001",
            "M-F: 8-5pm",
            "tim@dialexa.com",
            mockLocation
        );

        when(branchesDAO.findAllBranches()).thenReturn(List.of(branchToDelete));

        branchesService.syncWithSnowflake();

        verify(branchesDAO).updateBranchStatusByBranchId(false, "999");

        assertEquals("999", branchToDelete.getBranchId());
    }

    @Test
    void syncWithSnowflake_willCallGoogleIfAddressHasChangedOrBranchIsNew()
        throws IOException, InterruptedException, ApiException {
        var existingBranches = CreateBranches(mockLocation).subList(0, 2);
        existingBranches.get(1).setAddress1("123 New Address St.");
        when(branchesDAO.findAllBranches()).thenReturn(existingBranches);

        branchesService.syncWithSnowflake();

        verify(geocodingService, times(2)).getLocationFromAddress(anyString());
    }

    @Test
    void syncWithSnowflake_doesNotRunIfNoBranchesAreFoundInSnowflake() {
        when(dataWarehouseRepository.getAllBranches()).thenReturn(Collections.EMPTY_LIST);
        branchesService.syncWithSnowflake();
        verify(branchesDAO, times(0)).saveAll(any());

        testLogger
            .getLoggingEvents()
            .stream()
            .filter(ev -> ev.getLevel().equals(Level.WARN))
            .findFirst()
            .ifPresentOrElse(
                ev -> {
                    assertEquals(LoggingEvent.warn("No branches found in the Snowflake view"), ev);
                },
                Assertions::fail
            );
    }

    @Test
    void syncWithSnowflake_handlesGoogleApiException() throws IOException, InterruptedException, ApiException {
        var exception = ApiException.from("INVALID_REQUEST", "failed the test");
        when(branchesDAO.findAll()).thenReturn(Collections.EMPTY_LIST);
        when(geocodingService.getLocationFromAddress(any())).thenThrow(exception);
        assertDoesNotThrow(() -> branchesService.syncWithSnowflake());
        testLogger
            .getLoggingEvents()
            .stream()
            .filter(ev -> ev.getLevel().equals(Level.WARN))
            .findFirst()
            .ifPresentOrElse(
                ev -> {
                    assertEquals(exception, ev.getThrowable().get());
                    assertEquals("Unable to fetch coordinates for branch {} with address {}", ev.getMessage());
                    assertEquals(List.of("1006", "123 Test Ln, Addison, TX, 75001"), ev.getArguments());
                },
                Assertions::fail
            );
    }

    @Test
    void syncWithSnowflake_handlesGoogleIoException() throws IOException, InterruptedException, ApiException {
        var exception = new InterruptedException();
        when(branchesDAO.findAll()).thenReturn(Collections.EMPTY_LIST);
        when(geocodingService.getLocationFromAddress(any())).thenThrow(exception);
        assertDoesNotThrow(() -> branchesService.syncWithSnowflake());
        testLogger
            .getLoggingEvents()
            .stream()
            .filter(ev -> ev.getLevel().equals(Level.WARN))
            .findFirst()
            .ifPresentOrElse(
                ev -> {
                    assertEquals(exception, ev.getThrowable().get());
                    assertEquals("Unable to fetch coordinates for branch {} with address {}", ev.getMessage());
                    assertEquals(List.of("1006", "123 Test Ln, Addison, TX, 75001"), ev.getArguments());
                },
                Assertions::fail
            );
    }

    @Test
    void syncWithSnowflake_handlesGoogleInterruptedException() throws IOException, InterruptedException, ApiException {
        var exception = new IOException();
        when(branchesDAO.findAll()).thenReturn(Collections.EMPTY_LIST);
        when(geocodingService.getLocationFromAddress(any())).thenThrow(exception);
        assertDoesNotThrow(() -> branchesService.syncWithSnowflake());
        testLogger
            .getLoggingEvents()
            .stream()
            .filter(ev -> ev.getLevel().equals(Level.WARN))
            .findAny()
            .ifPresentOrElse(
                ev -> {
                    assertEquals(exception, ev.getThrowable().get());
                    assertEquals("Unable to fetch coordinates for branch {} with address {}", ev.getMessage());
                    assertEquals(List.of("1006", "123 Test Ln, Addison, TX, 75001"), ev.getArguments());
                },
                Assertions::fail
            );
    }

    @Test
    void refreshWorkdayBranches_success_overrideEmail() throws Exception {
        String testEmail = "test@test.com";
        ReflectionTestUtils.setField(branchesService, "overrideEmail", testEmail);
        WorkdayBranchResponseDTO workdayBranchResponse = new WorkdayBranchResponseDTO();
        List<WorkdayBranchDTO> mockWorkdayBranchDTOS = new ArrayList<>();
        WorkdayBranchDTO workdayBranchDTO = new WorkdayBranchDTO();
        workdayBranchDTO.setLocation_Code(TEST_LOCATION_CODE);
        workdayBranchDTO.setB_K(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setWaterworks(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setHVAC(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setPlumbing(WORKDAY_FLAG_FALSE);
        workdayBranchDTO.setManager(TEST_MANAGER);
        workdayBranchDTO.setBranch_Manager_Email(TEST_MANAGER_EMAIL);
        workdayBranchDTO.setBranch_Manager_Phone_Number(TEST_MANAGER_PHONE);
        workdayBranchDTO.setBrand(TEST_WORKDAY_BRAND);
        workdayBranchDTO.setAddress_Line_1(TEST_WORKDAY_ADDRESS_1);
        workdayBranchDTO.setAddress_Line_2(TEST_WORKDAY_ADDRESS_2);
        workdayBranchDTO.setCity(TEST_WORKDAY_CITY);
        workdayBranchDTO.setZip_Code(TEST_WORKDAY_ZIPCODE);
        workdayBranchDTO.setState(TEST_WORKDAY_STATE);
        workdayBranchDTO.setPhone(TEST_WORKDAY_PHONE);
        workdayBranchDTO.setLocation_Attributes(TEST_LOCATION_ATTRIBUTES);
        mockWorkdayBranchDTOS.add(workdayBranchDTO);

        workdayBranchResponse.setBranches(mockWorkdayBranchDTOS.toArray(new WorkdayBranchDTO[0]));
        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = new ResponseEntity<>(
            workdayBranchResponse,
            HttpStatus.OK
        );
        when(restTemplate.exchange(any(), any(), any(), eq(WorkdayBranchResponseDTO.class), anyInt()))
            .thenReturn(workDayDataResponse);
        when(branchesDAO.findAllBranches()).thenReturn(Collections.EMPTY_LIST);
        List<WorkdayBranchDTO> workdayBranchDTOS = branchesService.refreshWorkdayBranches();
        assertFalse(workdayBranchDTOS.isEmpty());
        WorkdayBranchDTO actualWorkdayBranchDto = workdayBranchDTOS.get(0);
        assertEquals(actualWorkdayBranchDto, workdayBranchDTO);

        verify(branchesDAO, times(1)).findAllBranches();
        verify(branchesDAO, times(1)).saveAll(anyList());
    }

    @Test
    void refreshWorkdayBranches_success_noOverrideEmail() throws Exception {
        ReflectionTestUtils.setField(branchesService, "overrideEmail", null);
        WorkdayBranchResponseDTO workdayBranchResponse = new WorkdayBranchResponseDTO();
        List<WorkdayBranchDTO> mockWorkdayBranchDTOS = new ArrayList<>();
        WorkdayBranchDTO workdayBranchDTO = new WorkdayBranchDTO();
        workdayBranchDTO.setLocation_Code(TEST_LOCATION_CODE);
        workdayBranchDTO.setB_K(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setWaterworks(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setHVAC(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setPlumbing(WORKDAY_FLAG_FALSE);
        workdayBranchDTO.setManager(TEST_MANAGER);
        workdayBranchDTO.setBranch_Manager_Email(TEST_MANAGER_EMAIL);
        workdayBranchDTO.setBranch_Manager_Phone_Number(TEST_MANAGER_PHONE);
        workdayBranchDTO.setBrand(TEST_WORKDAY_BRAND);
        workdayBranchDTO.setAddress_Line_1(TEST_WORKDAY_ADDRESS_1);
        workdayBranchDTO.setAddress_Line_2(TEST_WORKDAY_ADDRESS_2);
        workdayBranchDTO.setCity(TEST_WORKDAY_CITY);
        workdayBranchDTO.setZip_Code(TEST_WORKDAY_ZIPCODE);
        workdayBranchDTO.setState(TEST_WORKDAY_STATE);
        workdayBranchDTO.setPhone(TEST_WORKDAY_PHONE);
        workdayBranchDTO.setLocation_Attributes(TEST_LOCATION_ATTRIBUTES);
        mockWorkdayBranchDTOS.add(workdayBranchDTO);

        workdayBranchResponse.setBranches(mockWorkdayBranchDTOS.toArray(new WorkdayBranchDTO[0]));
        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = new ResponseEntity<>(
            workdayBranchResponse,
            HttpStatus.OK
        );
        when(restTemplate.exchange(any(), any(), any(), eq(WorkdayBranchResponseDTO.class), anyInt()))
            .thenReturn(workDayDataResponse);
        when(branchesDAO.findAllBranches()).thenReturn(Collections.EMPTY_LIST);
        List<WorkdayBranchDTO> workdayBranchDTOS = branchesService.refreshWorkdayBranches();
        assertFalse(workdayBranchDTOS.isEmpty());
        WorkdayBranchDTO actualWorkdayBranchDto = workdayBranchDTOS.get(0);
        assertEquals(actualWorkdayBranchDto, workdayBranchDTO);

        verify(branchesDAO, times(1)).findAllBranches();
        verify(branchesDAO, times(1)).saveAll(anyList());
    }

    //Update flow
    @Test
    void refreshWorkdayBranches_successInsert() throws WorkdayException {
        WorkdayBranchResponseDTO workdayBranchResponse = new WorkdayBranchResponseDTO();
        List<WorkdayBranchDTO> mockWorkdayBranchDTOS = new ArrayList<>();
        WorkdayBranchDTO workdayBranchDTO = new WorkdayBranchDTO();
        workdayBranchDTO.setLocation_Code(TEST_LOCATION_CODE);
        workdayBranchDTO.setB_K(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setWaterworks(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setHVAC(WORKDAY_FLAG_TRUE);
        workdayBranchDTO.setPlumbing(WORKDAY_FLAG_FALSE);
        workdayBranchDTO.setManager(TEST_MANAGER);
        workdayBranchDTO.setBranch_Manager_Email(TEST_MANAGER_EMAIL);
        workdayBranchDTO.setBranch_Manager_Phone_Number(TEST_MANAGER_PHONE);
        workdayBranchDTO.setBrand(TEST_WORKDAY_BRAND);
        workdayBranchDTO.setAddress_Line_1(TEST_WORKDAY_ADDRESS_1);
        workdayBranchDTO.setAddress_Line_2(TEST_WORKDAY_ADDRESS_2);
        workdayBranchDTO.setCity(TEST_WORKDAY_CITY_ALT);
        workdayBranchDTO.setZip_Code(TEST_WORKDAY_ZIPCODE);
        workdayBranchDTO.setState(TEST_WORKDAY_STATE);
        workdayBranchDTO.setPhone(TEST_WORKDAY_PHONE);
        workdayBranchDTO.setLocation_Attributes(TEST_LOCATION_ATTRIBUTES);
        mockWorkdayBranchDTOS.add(workdayBranchDTO);

        workdayBranchResponse.setBranches(mockWorkdayBranchDTOS.toArray(new WorkdayBranchDTO[0]));
        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = new ResponseEntity<>(
            workdayBranchResponse,
            HttpStatus.OK
        );
        when(restTemplate.exchange(any(), any(), any(), eq(WorkdayBranchResponseDTO.class), anyInt()))
            .thenReturn(workDayDataResponse);

        List<WorkdayBranchDTO> workdayBranchDTOS = branchesService.refreshWorkdayBranches();
        assertFalse(workdayBranchDTOS.isEmpty());
        WorkdayBranchDTO actualWorkdayBranchDto = workdayBranchDTOS.get(0);
        assertEquals(actualWorkdayBranchDto, workdayBranchDTO);

        verify(branchesDAO, times(1)).findAllBranches();
        verify(branchesDAO, times(1)).saveAll(anyList());
    }

    @Test
    void refreshWorkdayBranches_emptyResponseWorkday() {
        WorkdayBranchResponseDTO workdayBranchResponse = new WorkdayBranchResponseDTO();
        workdayBranchResponse.setBranches(new WorkdayBranchDTO[0]);

        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = new ResponseEntity<>(
            workdayBranchResponse,
            HttpStatus.OK
        );
        when(restTemplate.exchange(any(), any(), any(), eq(WorkdayBranchResponseDTO.class), anyInt()))
            .thenReturn(workDayDataResponse);
        assertThrows(WorkdayException.class, () -> branchesService.refreshWorkdayBranches());

        verify(branchesDAO, times(0)).deleteAll();
        verify(branchesDAO, times(0)).save(any());
    }

    @Test
    void refreshWorkdayBranches_nullResponseWorkday() {
        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = new ResponseEntity<>(
            null,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
        when(restTemplate.exchange(any(), any(), any(), eq(WorkdayBranchResponseDTO.class), anyInt()))
            .thenReturn(workDayDataResponse);
        assertThrows(WorkdayException.class, () -> branchesService.refreshWorkdayBranches());

        verify(branchesDAO, times(0)).deleteAll();
        verify(branchesDAO, times(0)).save(any());
    }

    @Test
    void getAllBranches_success() {
        var existingBranches = CreateBranches(mockLocation).subList(0, 2);
        existingBranches.get(1).setAddress1("123 New Address St.");
        when(branchesDAO.findAllBranches()).thenReturn(existingBranches);

        var result = branchesService.getAllBranches();

        assertEquals(result.get(1).getAddress1(), existingBranches.get(1).getAddress1());
    }

    @Test
    void updateBranches_success_updates_all() {
        var id = UUID.randomUUID();
        Branch branch = new Branch();
        branch.setName("Test Branch");
        branch.setIsActive(false);
        branch.setIsAvailableInStoreFinder(false);
        branch.setIsShoppable(false);
        branch.setIsPricingOnly(false);
        branch.setLocation(mockLocation);

        UpdateBranchDTO updateBranchDTO = new UpdateBranchDTO();
        updateBranchDTO.setIsActive(true);
        updateBranchDTO.setIsAvailableInStoreFinder(true);
        updateBranchDTO.setIsShoppable(true);
        updateBranchDTO.setIsPricingOnly(true);

        when(branchesDAO.findById(id)).thenReturn(Optional.of(branch));
        when(
            branchesDAO.updateBranch(
                updateBranchDTO.getIsActive(),
                updateBranchDTO.getIsAvailableInStoreFinder(),
                updateBranchDTO.getIsShoppable(),
                updateBranchDTO.getIsPricingOnly(),
                id
            )
        )
            .thenReturn(branch);

        var response = branchesService.updateBranch(id, updateBranchDTO);

        assertEquals(response.getIsActive(), true);
        assertEquals(response.getIsAvailableInStoreFinder(), true);
        assertEquals(response.getIsShoppable(), true);
        assertEquals(response.getIsPricingOnly(), true);
        verify(branchesDAO, times(1)).findById(id);
        verify(branchesDAO, times(1))
            .updateBranch(
                updateBranchDTO.getIsActive(),
                updateBranchDTO.getIsAvailableInStoreFinder(),
                updateBranchDTO.getIsShoppable(),
                updateBranchDTO.getIsPricingOnly(),
                id
            );
    }

    @Test
    void updateBranches_success_updates_with_defaults() {
        var id = UUID.randomUUID();
        Branch branch = new Branch();
        branch.setName("Test Branch");
        branch.setIsActive(false);
        branch.setIsAvailableInStoreFinder(false);
        branch.setIsShoppable(false);
        branch.setIsPricingOnly(false);
        branch.setLocation(mockLocation);

        UpdateBranchDTO updateBranchDTO = new UpdateBranchDTO();

        when(branchesDAO.findById(id)).thenReturn(Optional.of(branch));
        when(
            branchesDAO.updateBranch(
                branch.getIsActive(),
                branch.getIsAvailableInStoreFinder(),
                branch.getIsShoppable(),
                branch.getIsPricingOnly(),
                id
            )
        )
            .thenReturn(branch);

        var response = branchesService.updateBranch(id, updateBranchDTO);

        assertEquals(response.getIsActive(), false);
        assertEquals(response.getIsAvailableInStoreFinder(), false);
        assertEquals(response.getIsShoppable(), false);
        assertEquals(response.getIsPricingOnly(), false);
        verify(branchesDAO, times(1)).findById(id);
        verify(branchesDAO, times(1))
            .updateBranch(
                branch.getIsActive(),
                branch.getIsAvailableInStoreFinder(),
                branch.getIsShoppable(),
                branch.getIsPricingOnly(),
                id
            );
    }

    @Test
    void updateBranches_branchNotFound() {
        var id = UUID.randomUUID();
        when(branchesDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(
            BranchNotFoundException.class,
            () -> {
                branchesService.updateBranch(id, new UpdateBranchDTO());
            }
        );

        verify(branchesDAO, times(1)).findById(id);
        verify(branchesDAO, times(0)).updateBranch(any(), any(), any(), any(), any());
    }
}
