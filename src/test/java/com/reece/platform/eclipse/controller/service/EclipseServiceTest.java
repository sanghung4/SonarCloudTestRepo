package com.reece.platform.eclipse.controller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import com.reece.platform.eclipse.dto.EclipseRestSessionDTO;
import com.reece.platform.eclipse.dto.WarehousePickComplete;
import com.reece.platform.eclipse.dto.WarehouseTaskUserPicksDTO;
import com.reece.platform.eclipse.exceptions.EclipseException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.service.BaseEclipseService;
import com.reece.platform.eclipse.service.EclipseService;
import com.reece.platform.eclipse.service.EclipseSessionService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class EclipseServiceTest {

    @Mock
    private EclipseSessionService eclipseSessionService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EclipseService eclipseService = new EclipseService(eclipseSessionService);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(eclipseService, "eclipseApiEndpoint", "https://test.com");
        ReflectionTestUtils.setField(eclipseService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(eclipseService, "eclipseSessionService", eclipseSessionService);
    }

    @Test
    public void getUserPicks_200() throws EclipseException, EclipseTokenException {
        EclipseRestSessionDTO sampleEclipseRestSessionDTOSample = new EclipseRestSessionDTO();
        sampleEclipseRestSessionDTOSample.setSessionToken("Test");

        WarehouseTaskUserPicksDTO warehouseTaskUserPicksDTO = new WarehouseTaskUserPicksDTO();
        WarehousePickComplete warehousePickComplete = new WarehousePickComplete();
        warehouseTaskUserPicksDTO.setResults(Collections.singletonList(warehousePickComplete));
        warehousePickComplete.setWarehouseID("TestWareHouse");
        warehousePickComplete.setBranchId("TestBranch");

        Mockito
            .when(eclipseSessionService.getSessionToken())
            .thenReturn(Optional.ofNullable(sampleEclipseRestSessionDTOSample));
        Mockito
            .when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(WarehouseTaskUserPicksDTO.class)))
            .thenReturn(new ResponseEntity<>(warehouseTaskUserPicksDTO, HttpStatus.OK));

        WarehouseTaskUserPicksDTO result = eclipseService.getUserPicks("testBranch", "abc");

        assertEquals(result.getResults().size(), 1);
        assertEquals(result.getResults().get(0).getBranchId(), "TestBranch");
        assertEquals(result.getResults().get(0).getWarehouseID(), "TestWareHouse");
    }
}
