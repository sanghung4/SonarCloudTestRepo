package com.reece.platform.eclipse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.exceptions.EclipseLoadCountsException;
import com.reece.platform.eclipse.external.ec.*;
import com.reece.platform.eclipse.model.DTO.EclipseLoadCountDto;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EclipseServiceApplication.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class InventoryControllerTest {

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private FileTransferService fileTransferService;

    @MockBean
    private KourierService kourierService;

    @MockBean
    private EclipseCredentialsStore eclipseCredentialsStore;

    @MockBean
    private EclipseConnectService eclipseConnectService;

    private InventoryController inventoryController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static final EclipseCredentials MOCK_CREDENTIALS = new EclipseCredentials(
            "abc",
            "abc",
            "abc"
    );
    private static final EclipseBatchDTO MOCK_BATCH = new EclipseBatchDTO(
            "testBatchNumber",
            "testBranchNumber",
            "testBranchName",
            "testBatchDescription",
            "testBatchUserId",
            "testBatchUsername",
            null
    );

    private static final List<EclipseLoadCountDto> MOCK_ECLIPSE_COUNTS = List.of(
            new EclipseLoadCountDto()
    );

    private static final String MOCK_CREDENTIAL_HEADER = "dGVzdDp0ZXN0";

    private static final List<String> MOCK_LOCATIONS = List.of(
            "A10B10",
            "A10B15"
    );

    private static final EclipseLocationItemDTO MOCK_ECLIPSE_LOCATION_ITEM = new EclipseLocationItemDTO("test","test","test","test","test");

    private static final List<EclipseLocationDTO> MOCK_ECLIPSE_LOCATIONS = List.of(
            new EclipseLocationDTO("test", List.of(MOCK_ECLIPSE_LOCATION_ITEM))
    );

    private static final EclipseNextLocationDTO MOCK_NEXT_LOCATION = new EclipseNextLocationDTO();

    private static final String MOCK_UPDATE_REQUEST = "{\"productId\": \"test\",\"quantity\": 5}";

    @BeforeEach
    public void setup() throws Exception {
        inventoryController = new InventoryController(eclipseCredentialsStore, eclipseConnectService, fileTransferService);
        objectMapper = new ObjectMapper();
    }

    @Test
    void validateBatch_success() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString()))
                .thenReturn(MOCK_CREDENTIALS);
        when(eclipseConnectService.validateBatch(any(EclipseCredentials.class), anyString(), anyString()))
                .thenReturn(MOCK_BATCH);
        this.mockMvc.perform(get("/inventory/branches/testBranch/batches/testBatchId")
                        .header("X-Eclipse-Credentials", MOCK_CREDENTIAL_HEADER)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    @Test
    void getInventoryCounts_success() throws Exception {
        when(fileTransferService.downloadLatestEclipseCountsFile()).thenReturn(MOCK_ECLIPSE_COUNTS);
        var result = this.mockMvc.perform(put("/inventory/count/_load"))
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() -> objectMapper.readValue(result.getResponse().getContentAsString(), List.class));
    }

    @Test
    void getInventoryCounts_failureEclipse() throws Exception {
        when(fileTransferService.downloadLatestEclipseCountsFile()).thenThrow(new EclipseLoadCountsException());
        this.mockMvc.perform(put("/inventory/count/_load"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllLocations_succcess() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString())).thenReturn(MOCK_CREDENTIALS);
        when(eclipseConnectService.getAllLocations(any(EclipseCredentials.class), anyString(), anyString()))
                .thenReturn(MOCK_LOCATIONS);
        this.mockMvc.perform(get("/inventory/branches/testBranch/batches/testBatch/locations")
                        .header("Authorization", "test"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getAllProducts_success() throws Exception {
        when(eclipseConnectService.getAllProducts(any(EclipseCredentials.class), anyString(), anyString()))
                .thenReturn(MOCK_ECLIPSE_LOCATIONS);
        this.mockMvc.perform(get("/inventory/branches/testBranch/batches/testBatch/locations/_fullLoad")
                        .header("Authorization", "test"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getLocation_success() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString())).thenReturn(MOCK_CREDENTIALS);
        when(eclipseConnectService.getLocationItems(any(EclipseCredentials.class), anyString(), anyString()))
                .thenReturn(List.of(MOCK_ECLIPSE_LOCATION_ITEM));
        this.mockMvc.perform(get("/inventory/batches/testBatch/locations/testLocation")
                .header("Authorization", "test"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getNextLocation_success() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString())).thenReturn(MOCK_CREDENTIALS);
        when(eclipseConnectService.getNextLocation(any(EclipseCredentials.class), anyString(), anyString()))
                .thenReturn(MOCK_NEXT_LOCATION);
        this.mockMvc.perform(get("/inventory/batches/testBatch/locations/testLocation/_next")
                        .header("Authorization", "test"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateCount_success() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString())).thenReturn(MOCK_CREDENTIALS);
        this.mockMvc.perform(post("/inventory/batches/testBatch/locations/testLocation")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_UPDATE_REQUEST))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void addToCount_success() throws Exception {
        when(eclipseCredentialsStore.getCredentials(anyString(), anyString())).thenReturn(MOCK_CREDENTIALS);
        this.mockMvc.perform(post("/inventory/batches/testBatch/locations/testLocation/_new")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MOCK_UPDATE_REQUEST))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
