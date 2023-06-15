package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.internal.MincronAllCountsDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.external.mincron.MincronLocationDTO;
import com.reece.platform.inventory.external.mincron.MincronService;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.reece.platform.inventory.util.TestCommon.mockMincronAddToCountDTO;
import static com.reece.platform.inventory.util.TestCommon.mockMincronPostCountDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MincronServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MincronService mincronService = new MincronService(restTemplate);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(mincronService, "mincronServiceUrl", "test.com");
        ReflectionTestUtils.setField(mincronService, "restTemplate", restTemplate);
    }

    @Test
    public void getCounts_Success() {

        when(restTemplate.getForEntity(anyString(), eq(MincronAllCountsDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mincronAllCountsDTO(), HttpStatus.OK));

        MincronAllCountsDTO result = mincronService.getCounts();
        assertNotNull(result.getCounts());
        assertEquals(result.getCounts().get(0).getCountId(), "7352");
        assertFalse(result.getMoreThan100Counts());
        assertEquals(result.getCounts().get(0).getCountDate(), "2023-04-29");

    }

    @Test
    public void getCount_Success() {

        when(restTemplate.getForEntity(anyString(), eq(com.reece.platform.inventory.external.mincron.MincronCountDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mincronCountDTO(), HttpStatus.OK));

        com.reece.platform.inventory.external.mincron.MincronCountDTO result = mincronService.getCount("8355", "7352");
        assertEquals(result.getCountId(), "7352");
        assertEquals(result.getBranchName(), "test");
        assertEquals(result.getBranchNumber(), "8355");

    }

    @Test
    public void getAllLocations_Success() {

        String[] locations = new String[]{"7358", "9452", "1256"};
        when(restTemplate.getForEntity(anyString(), eq(String[].class)))
                .thenReturn(new ResponseEntity<>(locations, HttpStatus.OK));

        List<String> result = mincronService.getAllLocations("8355", "7352");
        assertNotNull(result);
        assertEquals(result.get(0), "7358");
        assertEquals(result.get(1), "9452");
        assertEquals(result.get(2), "1256");

    }

    @Test
    public void getLocation_Success() {

        when(restTemplate.getForEntity(anyString(), eq(MincronLocationDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.mincronLocationDTO(), HttpStatus.OK));

        MincronLocationDTO result = mincronService.getLocation("8355", "7352", "8662");
        assertNotNull(result.getItems());
        assertEquals(result.getLocationId(), "8662");
        assertEquals(result.getItems().get(0).getItemNum(), "6724");
        assertEquals(result.getItems().get(0).getCatalogNum(), "9622");
        assertEquals(result.getItems().get(0).getProdNum(), "5673");

    }

    @Test
    public void updateCount_Success() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mincronService.updateCount("7352", "8355", Collections.singletonList(mockMincronPostCountDTO));

    }

    @Test
    public void addToCount_Success() {

        when(restTemplate.postForEntity(anyString(), eq(mockMincronAddToCountDTO), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mincronService.addToCount("7352", "8355", "8662", "5242", 465);

    }

    @Test
    public void getMincronVarianceSummary_Success() {

        when(restTemplate.getForEntity(anyString(), eq(VarianceSummary.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.createSampleVariancesummary(), HttpStatus.OK));

        VarianceSummaryDTO result = mincronService.getMincronVarianceSummary("7352", "8355");
        assertEquals(result.getDifferenceQuantity(), 100);
        assertEquals(result.getNetTotalCost(), "9999.9");
        assertEquals(result.getLocationQuantity(), 99);

    }

    @Test
    public void getMincronVarianceDetails_Success() {

        when(restTemplate.getForEntity(anyString(), eq(VarianceDetailsDTO.class)))
                .thenReturn(new ResponseEntity<>(TestCommon.varianceDetailsDTO(), HttpStatus.OK));

        VarianceDetailsDTO result = mincronService.getMincronVarianceDetails("7352", "8355");
        assertNotNull(result.getCounts());
        assertEquals(result.getCounts().get(0).getErpProductID(), "testProduct");
        assertFalse(result.getCounts().get(0).getNotCountedFlag());
        assertEquals(result.getCounts().get(0).getOnHandCost(), 55.0);
        assertEquals(result.getCounts().get(0).getQtyDeviance(), 41);

    }

}
