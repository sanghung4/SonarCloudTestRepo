package com.reece.platform.mincron.service;

import com.reece.platform.mincron.dto.*;
import com.reece.platform.mincron.dto.kerridge.*;
import com.reece.platform.mincron.dto.variance.VarianceDetails;
import com.reece.platform.mincron.dto.variance.VarianceDetailsResponseDTO;
import com.reece.platform.mincron.dto.variance.VarianceSummaryDTO;
import com.reece.platform.mincron.exception.MincronException;
import com.reece.platform.mincron.exception.NotFoundException;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MincronServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    MincronService mincronService = new MincronService(restTemplate);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(mincronService, "mincronBaseUrl", "http://test.com");
        ReflectionTestUtils.setField(mincronService, "restTemplate", restTemplate);
    }

    @Test
    public void getAllCounts_Success() throws IOException {

        CountInfoDTO countInfoDTO = new CountInfoDTO("1003", "54367", 1, "031623");
        AvailCountsResponseDTO availCountsResponseDTO = new AvailCountsResponseDTO(1, Collections.singletonList(countInfoDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AvailCountsResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(availCountsResponseDTO, HttpStatus.OK));

        MincronCountsDTO resp = mincronService.getAllCounts();
        assertNotNull(resp.getCounts());
        assertEquals(resp.getCounts().get(0).getBranchNum(), "1003");
        assertEquals(resp.getCounts().get(0).getCountId(), "54367");
        assertEquals(resp.getCounts().get(0).getNumItems(), 1);
        assertEquals(resp.getCounts().get(0).getCountDate().toString(), "2023-03-16");

    }

    @Test
    public void getAllCounts_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AvailCountsResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.getAllCounts());

    }

    @Test
    public void validateCount_Success() throws IOException {

        ValidateCountResponseDTO validateCountResponseDTO = new ValidateCountResponseDTO();
        validateCountResponseDTO.setBranch("1003");
        validateCountResponseDTO.setBrname("test");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ValidateCountResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(validateCountResponseDTO, HttpStatus.OK));

        CountDTO resp = mincronService.validateCount("1003", "54367");
        assertNotNull(resp.getCountId());
        assertEquals(resp.getBranchNumber(), "1003");
        assertEquals(resp.getBranchName(), "test");
        assertEquals(resp.getCountId(), "54367");

    }

    @Test
    public void validateCount_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ValidateCountResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.validateCount("1003", "54367"));

    }

    @Test
    public void getLocations_Success() throws IOException {

        LocationCodeDTO locationCodeDTO = new LocationCodeDTO();
        locationCodeDTO.setLocation("Mexico");
        CountLocationsResponseDTO countLocationsResponseDTO = new CountLocationsResponseDTO();
        countLocationsResponseDTO.setCountLocations(23);
        countLocationsResponseDTO.setLocations(Collections.singletonList(locationCodeDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CountLocationsResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(countLocationsResponseDTO, HttpStatus.OK));

        List<String> resp = mincronService.getLocations("1003", "54367");
        assertNotNull(resp);
        assertEquals(resp.get(0), "Mexico");

    }

    @Test
    public void getLocations_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CountLocationsResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.getLocations("1003", "54367"));

    }

    @Test
    public void getLocation_Success() throws IOException {

        CountLocationItemDTO countLocationItemDTO = new CountLocationItemDTO("35", "52", "test", "642", "754", "I", "", "");
        CountLocationResponseDTO countLocationResponseDTO = new CountLocationResponseDTO(43, 65, Collections.singletonList(countLocationItemDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CountLocationResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(countLocationResponseDTO, HttpStatus.OK));

        LocationDTO resp = mincronService.getLocation("1003", "54367", "Mexico");
        assertNotNull(resp.getItems());
        assertEquals(resp.getLocationId(), "Mexico");
        assertEquals(resp.getItemCount(), 65);
        assertEquals(resp.getTotalQuantity(), 43);

    }

    @Test
    public void getLocation_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CountLocationResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.getLocation("1003", "54367", "Mexico"));

    }

    @Test
    public void updateCount_Success() throws IOException {

        PostCountResponseDTO postCountResponseDTO = new PostCountResponseDTO();
        postCountResponseDTO.setIsSuccess(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(PostCountResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(postCountResponseDTO, HttpStatus.OK));

        MincronCountDTO mincronCountDTO = new MincronCountDTO("Mexico", "test", 32);
        MincronUpdateCountRequestDTO mincronUpdateCountRequestDTO = new MincronUpdateCountRequestDTO();
        mincronUpdateCountRequestDTO.setUpdates(Collections.singletonList(mincronCountDTO));
        mincronService.updateCount("1003", "54367", mincronUpdateCountRequestDTO);

    }

    @Test
    public void updateCount_MincronException() throws IOException {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(PostCountResponseDTO.class)))
                .thenThrow(MincronException.class);

        MincronCountDTO mincronCountDTO = new MincronCountDTO("Mexico", "test", 32);
        MincronUpdateCountRequestDTO mincronUpdateCountRequestDTO = new MincronUpdateCountRequestDTO();
        mincronUpdateCountRequestDTO.setUpdates(Collections.singletonList(mincronCountDTO));
        assertThrows(MincronException.class, () -> mincronService.updateCount("1003", "54367", mincronUpdateCountRequestDTO));

    }

    @Test
    public void getNextLocation_Success() throws IOException {

        NextLocationResponseDTO nextLocationResponseDTO = new NextLocationResponseDTO();
        nextLocationResponseDTO.setNextloc("Mexico");
        nextLocationResponseDTO.setIsSuccess(true);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(NextLocationResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(nextLocationResponseDTO, HttpStatus.OK));

        NextLocationDTO resp = mincronService.getNextLocation("1003", "54367", "Mexico");
        assertNotNull(resp.getLocationId());
        assertEquals(resp.getLocationId(), "Mexico");

    }

    @Test
    public void getNextLocation_NotFoundException() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(NextLocationResponseDTO.class)))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> mincronService.getNextLocation("1003", "54367", "Mexico"));

    }

    @Test
    public void searchProducts_Success() throws IOException {

        ProductSearchItemDTO productSearchItemDTO = new ProductSearchItemDTO("4325", "test", "I", "", "");
        ProductSearchResponseDTO productSearchResponseDTO = new ProductSearchResponseDTO();
        productSearchResponseDTO.setLastItem("test");
        productSearchResponseDTO.setResults(Collections.singletonList(productSearchItemDTO));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductSearchResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(productSearchResponseDTO, HttpStatus.OK));

        ProductSearchResultDTO resp = mincronService.searchProducts("1003", "test", "Mexico");
        assertNotNull(resp.getItems());
        assertEquals(resp.getLastItem(), "test");
        assertEquals(resp.getItems().get(0).getProdDesc(), "test");
        assertEquals(resp.getItems().get(0).getUom(), "I");

    }

    @Test
    public void searchProducts_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ProductSearchResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.searchProducts("1003", "test", "Mexico"));

    }

    @Test
    public void fetchVarianceSummary_Success() throws IOException {

        VarianceSummaryDTO varianceSummaryDTO = new VarianceSummaryDTO(23, 412, 34, 598.00, 754.54);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceSummaryDTO.class)))
                .thenReturn(new ResponseEntity<>(varianceSummaryDTO, HttpStatus.OK));

        VarianceSummaryDTO resp = mincronService.fetchVarianceSummary("1003", "54367");
        assertNotNull(resp.getTotalQuantity());
        assertEquals(resp.getTotalQuantity(), 412);
        assertEquals(resp.getTotalNumberOfLocations(), 34);
        assertEquals(resp.getGrossTotalVarianceCost(), 598.00);

    }

    @Test
    public void fetchVarianceSummary_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceSummaryDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.fetchVarianceSummary("1003", "54367"));

    }

    @Test
    public void fetchVarianceDetails_Success() throws IOException {

        VarianceDetails varianceDetails = new VarianceDetails("Mexico", "4245", "test", 42, 43, 56, 53.49, 67.00, 976.21, true, 42, 75547, 632);
        VarianceDetailsResponseDTO varianceDetailsResponseDTO = new VarianceDetailsResponseDTO();
        varianceDetailsResponseDTO.setCounts(Collections.singletonList(varianceDetails));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceDetailsResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(varianceDetailsResponseDTO, HttpStatus.OK));

        VarianceDetailsResponseDTO resp = mincronService.fetchVarianceDetails("1003", "54367");
        assertNotNull(resp.getCounts());
        assertEquals(resp.getCounts().get(0).getCountedCost(), 976.21);
        assertEquals(resp.getCounts().get(0).getErpProductID(), "4245");
        assertEquals(resp.getCounts().get(0).getQtyDeviance(), 56);
        assertEquals(resp.getCounts().get(0).getNotCountedFlag(), true);
        assertEquals(resp.getCounts().get(0).getPercentDeviance(), 67.00);;

    }

    @Test
    public void fetchVarianceDetails_Exception() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(VarianceDetailsResponseDTO.class)))
                .thenThrow(HttpClientErrorException.class);
        assertThrows(Exception.class, () -> mincronService.fetchVarianceDetails("1003", "54367"));

    }
}
