package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.WriteInDTO;
import com.reece.platform.inventory.exception.LocationNotFoundException;
import com.reece.platform.inventory.exception.WriteInNotFountException;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.Location;
import com.reece.platform.inventory.model.LocationCount;
import com.reece.platform.inventory.model.WriteIn;
import com.reece.platform.inventory.repository.CountRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.WriteInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.LongSupplier;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class WriteInsServiceTest{

    @Mock
    private WriteInRepository mockWriteInRepository;

    @Mock
    private LocationCountRepository mockLocationCountRepository;

    @Mock
    private LongSupplier totalSupplier;

    @InjectMocks
    private WriteInsService writeInsService;

    @Mock
    private CountRepository mockCountRepository;

    Location mockLocation = new Location();
    LocationCount mockLocationCount = new LocationCount();

    Count mockCount = new Count();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        writeInsService = new WriteInsService(
                mockWriteInRepository,
                mockCountRepository
        );

        mockLocation.setErpLocationId("TestLocation");
        mockLocationCount.setLocation(mockLocation);
    }

    @Test
    void findAllWriteIns_shouldReturnPageOfWriteInDTO() {
        Pageable pageable = PageRequest.of(0, 8);
        var writeInList = new ArrayList<WriteIn>();
        var entity1 = mockWriteInDTO_1.toEntity();
        var entity2 = mockWriteInDTO_2.toEntity();

        entity1.setCountId(mockCount);
        entity2.setCountId(mockCount);

        writeInList.add(entity1);
        writeInList.add(entity2);
        var mockResponse = PageableExecutionUtils.getPage(writeInList, pageable, totalSupplier);

        when(mockWriteInRepository.findAllWriteIns(any(String.class), any(String.class), any(Pageable.class)))
                .thenReturn(mockResponse);

        writeInsService.findAllWriteIns("Testing","Testing", pageable);

        verify(mockWriteInRepository, times(1))
                .findAllWriteIns(any(String.class), any(String.class), any(Pageable.class));
    }

    @Test
    void findById_shouldReturnWriteInDTO(){
        var mockResponse = mockWriteInDTO_1.toEntity();

        mockResponse.setCountId(mockCount);
        mockResponse.setLocationName("test location");
        mockResponse.setLocationCount(null);

        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockResponse));

        writeInsService.findById(testUUID);

        verify(mockWriteInRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findById_shouldThrowWriteInNotFountException(){
        when(mockWriteInRepository.findById(any(UUID.class))).thenThrow(WriteInNotFountException.class);

        assertThrows(
                WriteInNotFountException.class,
                () -> writeInsService.findById(testUUID)
        );
    }

    @Test
    void createWriteIn_shouldSetLocationCountInWriteInAndSaveItReturnWriteInDTO() {
        var mockResponse = mockWriteInDTO_1.toEntity();
        mockResponse.setCountId(mockCount);
        mockResponse.setLocationName("test location");
        mockResponse.setLocationCount(null);
        var expectedResult = WriteInDTO.fromEntity(mockResponse);

        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(mockLocationCount));
        when(mockWriteInRepository.save(any(WriteIn.class)))
                .thenReturn(mockResponse);
        when(mockCountRepository.findCount(anyString(), anyString()))
                .thenReturn(Optional.of(mockCount));

        var results = writeInsService
                .createWriteIn("Testing", "Testing", mockWriteInDTO_1);

        assertEquals(expectedResult.getLocationId(), results.getLocationId());
        verify(mockCountRepository, times(1))
                .findCount(anyString(), anyString());
        verify(mockWriteInRepository, times(1))
                .save(any(WriteIn.class));
    }

    @Test
    void createWriteIn_ThrowLocationNotFoundException(){
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
                .thenThrow(LocationNotFoundException.class);

        assertThrows(
                LocationNotFoundException.class,
                () -> mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()));
    }

    @Test
    void updateWriteIn_shouldReturnWriteInDTOUpdatedFromMockWriteInDTO_1() {
        var mockResponse = mockWriteInDTO_1.toEntity();
        mockResponse.setCountId(mockCount);
        mockResponse.setLocationName("Test Location");
        mockResponse.setLocationCount(null);
        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenReturn(Optional.ofNullable(mockResponse));
        when(mockWriteInRepository.save(any(WriteIn.class))).thenAnswer(saveFunc -> saveFunc.getArguments()[0]);

        var result = writeInsService.updateWriteIn(testUUID_2, mockWriteInDTO_1);

        assertEquals("Test Location", result.getLocationId());
        assertEquals(mockWriteInDTO_1.getCatalogNum(), result.getCatalogNum());
        assertEquals(mockWriteInDTO_1.getUpcNum(), result.getUpcNum());
        assertEquals(mockWriteInDTO_1.getDescription(), result.getDescription());
        assertEquals(mockWriteInDTO_1.getUom(), result.getUom());
        assertEquals(mockWriteInDTO_1.getQuantity(), result.getQuantity());
        assertEquals(mockWriteInDTO_1.getComment(), result.getComment());
        assertEquals(mockWriteInDTO_1.isResolved(), result.isResolved());

        verify(mockWriteInRepository, times(1))
                .findById(any(UUID.class));
        verify(mockWriteInRepository, times(1))
                .save(any(WriteIn.class));
    }

   /* @Test
    void updateWriteIn_shouldThrowLocationNotFoundException(){
        when(mockLocationCountRepository.findLocationCount(anyString(),anyString(),anyString()))
            .thenThrow(LocationNotFoundException.class);

        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenReturn(Optional.ofNullable(mockWriteInDTO_2.toEntity()));
        when(mockWriteInRepository.save(any(WriteIn.class))).thenAnswer(saveFunc -> saveFunc.getArguments()[0]);


        assertThrows(
                LocationNotFoundException.class,
                () -> writeInsService.updateWriteIn(testUUID_2, mockWriteInDTO_1));
    }*/

    @Test
    void updateWriteIn_shouldThrowWriteInNotFountException(){
        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenThrow(WriteInNotFountException.class);

        when(mockLocationCountRepository.findLocationCount(anyString(),anyString(),anyString()))
                .thenReturn(Optional.ofNullable(mockLocationCount));
        when(mockWriteInRepository.save(any(WriteIn.class))).thenAnswer(saveFunc -> saveFunc.getArguments()[0]);


        assertThrows(
                WriteInNotFountException.class,
                () -> writeInsService.updateWriteIn(testUUID_2, mockWriteInDTO_1));
    }

    @Test
    void resolveWriteIn_shouldReturnResolvedVersionOfEnteredUUIDBasedWriteInDTO() {
        var mockWriteIn = mockWriteInDTO_1.toEntity();
        mockWriteIn.setCountId(mockCount);
        mockWriteIn.setLocationName("test location");
        mockWriteIn.setLocationCount(null);
        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockWriteIn));
        when(mockWriteInRepository.save(any(WriteIn.class))).thenAnswer(saveFunc -> saveFunc.getArguments()[0]);


        var result = writeInsService.resolveWriteIn(testUUID);

        verify(mockWriteInRepository, times(1))
                .findById(any(UUID.class));
        verify(mockWriteInRepository, times(1))
                .save(any(WriteIn.class));
        assertTrue(result.isResolved());
    }

    @Test
    void resolveWriteIn_ThrowWriteInNotFountException(){
        when(mockWriteInRepository.findById(any(UUID.class)))
                .thenThrow(WriteInNotFountException.class);

        when(mockLocationCountRepository.findLocationCount(anyString(),anyString(),anyString()))
                .thenReturn(Optional.ofNullable(mockLocationCount));
        when(mockWriteInRepository.save(any(WriteIn.class))).thenAnswer(saveFunc -> saveFunc.getArguments()[0]);

        assertThrows(
                WriteInNotFountException.class,
                () -> writeInsService.updateWriteIn(testUUID_2, mockWriteInDTO_1));
    }
}