package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.querydsl.core.types.Predicate;
import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.exception.*;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.*;
import java.util.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CountsServiceTest {

    @Mock
    private CountRepository mockCountRepository;

    @Mock
    private CountLocationItemRepository mockCountLocationItemRepository;

    @Mock
    private LocationCountRepository mockLocationCountRepository;

    @Mock
    private CountItemQuantityRepository mockCountItemQuantityRepository;

    @Mock
    private ERPSystemFactory mockErpSystemFactory;

    @InjectMocks
    private CountsService countsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        countsService =
            new CountsService(
                mockCountRepository,
                mockCountLocationItemRepository,
                mockLocationCountRepository,
                mockCountItemQuantityRepository,
                mockErpSystemFactory
            );

        mockBranch.setName("testing");
        mockCount.setBranch(new Branch());
    }

    @Test
    void getCount_shouldReturnCountDTO_emptyCount() {
        when(mockCountRepository.findCount(anyString(), anyString())).thenReturn(Optional.empty());

        countsService.getCount("1234", "Testing");

        verify(mockCountRepository, times(1)).findCount(anyString(), anyString());
    }

    @Test
    void getCount_shouldReturnCountDTO_presentCount() {
        when(mockCountRepository.findCount(anyString(), anyString())).thenReturn(Optional.of(mockCount));

        countsService.getCount("1234", "Testing");

        verify(mockCountRepository, times(1)).findCount(anyString(), anyString());
    }

    @Test
    void getLocations_shouldReturnLocationsDTO() {
        when(mockCountRepository.findCount(any(), any())).thenReturn(Optional.of(mockCount));
        when(mockLocationCountRepository.findLocationSummaries(anyString(), any()))
            .thenReturn(mockLocationSummaryDTOList);

        var expectedResult = new LocationsDTO(mockLocationSummaryDTOList.size(), 0, mockLocationSummaryDTOList);

        var result = countsService.getLocations("1234", "1234");

        assertEquals(expectedResult, result);
        verify(mockCountRepository, times(1)).findCount(any(), any());
        verify(mockLocationCountRepository, times(1)).findLocationSummaries(anyString(), any());
    }

    @Test
    void getLocations_shouldThrowCountNotFoundException() {
        when(mockCountRepository.findCount(any(), any())).thenReturn(Optional.empty());
        assertThrows(CountNotFoundException.class, () -> countsService.getLocations("1234", "1234"));
    }

    @Test
    void getLocation_shouldReturnOptionalLocationDTO_nonEmptyResult() {
        when(mockLocationCountRepository.findLocation(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockLocationDTO));
        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);

        countsService.getLocation("Testing", "Testing", "Testing");

        verify(mockLocationCountRepository, times(1)).findLocation(anyString(), anyString(), anyString());
        verify(mockErpSystemFactory, times(0)).getErpSystem(anyString());
        verify(mockCountLocationItemRepository, times(0)).saveAll(any());
    }

    @Test
    void getLocation_shouldReturnOptionalLocationDTO_EmptyResult() {
        when(mockLocationCountRepository.findLocation(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockLocationDTO_EmptyArray));

        var result = countsService.getLocation("1234", "Testing", "Testing");

        assertEquals(mockLocationDTO_EmptyArray, result);
        verify(mockLocationCountRepository, times(1)).findLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getNextLocation_shouldReturnNextLocationDTO() {
        when(mockLocationCountRepository.getNextLocationId(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of("Testing"));

        countsService.getNextLocation("Testing", "Testing", "Testing");

        verify(mockLocationCountRepository, times(1)).getNextLocationId(anyString(), anyString(), anyString());
    }

    @Test
    void getNextLocation_throwNextLocationNotFoundException() {
        when(mockLocationCountRepository.getNextLocationId(anyString(), anyString(), anyString()))
            .thenThrow(NextLocationNotFoundException.class);

        assertThrows(
            NextLocationNotFoundException.class,
            () -> countsService.getNextLocation("Testing", "Testing", "Testing")
        );
    }

    @Test
    void stageCount_shouldSaveCountItem() {
        when(mockCountLocationItemRepository.findCountItem(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockCountItem));

        countsService.stageCount("Testing", "Testing", "Testing", "Testing", 21);

        verify(mockCountLocationItemRepository, times(1))
            .findCountItem(anyString(), anyString(), anyString(), anyString());

        verify(mockCountLocationItemRepository, times(1)).save(any(CountItem.class));
    }

    @Test
    void stageCount_shouldRollBackStageCount() {
        mockCountItem.setStatus(CountLocationItemStatus.STAGED);
        when(mockCountLocationItemRepository.findCountItem(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockCountItem));
        when(mockCountItemQuantityRepository.findMostRecentQuantity(any(CountItem.class)))
            .thenReturn(Optional.of(new CountItemQuantity()));

        countsService.stageCount("Testing", "Testing", "Testing", "Testing", null);

        verify(mockCountLocationItemRepository, times(1))
            .findCountItem(anyString(), anyString(), anyString(), anyString());
        verify(mockCountItemQuantityRepository, times(1)).findMostRecentQuantity(any(CountItem.class));
        verify(mockCountLocationItemRepository, times(1)).save(any(CountItem.class));
    }

    @Test
    void stageCount_shouldThrowGenericError() {
        assertThrows(
            NoSuchElementException.class,
            () -> countsService.stageCount("Testing", "Testing", "Testing", "Testing", 21)
        );
    }

    @Test
    void commitCount_shouldSaveCountToRelevantRepository() {
        mockCountItem.setMostRecentQuantity(21);

        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(new LocationCount()));
        when(mockCountLocationItemRepository.findStagedCountItems(anyString(), anyString(), anyString()))
            .thenReturn(List.of(mockCountItem, mockCountItem));

        countsService.commitCount("Testing", "Testing", "Testing");

        verify(mockErpSystemFactory, times(1)).getErpSystem(anyString());
        verify(mockLocationCountRepository, times(1)).findLocationCount(anyString(), anyString(), anyString());
        verify(mockCountLocationItemRepository, times(1)).findStagedCountItems(anyString(), anyString(), anyString());

        verify(mockCountLocationItemRepository, times(1)).saveAll(anyIterable());
        verify(mockCountItemQuantityRepository, times(1)).saveAll(anyIterable());
        verify(mockLocationCountRepository, times(1)).save(any(LocationCount.class));
    }

    @Test
    void commitCount_shouldThrowGenericError() {
        mockCountItem.setMostRecentQuantity(21);

        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenThrow(NoSuchElementException.class);
        when(mockCountLocationItemRepository.findStagedCountItems(anyString(), anyString(), anyString()))
            .thenReturn(List.of(mockCountItem, mockCountItem));

        assertThrows(NoSuchElementException.class, () -> countsService.commitCount("Testing", "Testing", "Testing"));
    }

    @Test
    void addToCount() {
        var expectedResult = new LocationProductDTO(
            "Testing",
            "Testing",
            "Testing",
            "Testing",
            "Testing",
            "Testing",
            "Testing",
            21,
            CountLocationItemStatus.COMMITTED,
            21,
            "1"
        );
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(new LocationCount()));
        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockCountLocationItemRepository.count((Predicate) any())).thenReturn(21L);

        var result = countsService.addToCount("Testing", "Testing", "Testing", "Testing", 21);

        assertEquals(expectedResult, result);
        verify(mockLocationCountRepository, times(1)).findLocationCount(anyString(), anyString(), anyString());
        verify(mockErpSystemFactory, times(1)).getErpSystem(anyString());
        verify(mockCountLocationItemRepository, times(1)).count((Predicate) any());
        verify(mockCountLocationItemRepository, times(1)).save(any(CountItem.class));
        verify(mockCountItemQuantityRepository, times(1)).save(any(CountItemQuantity.class));
    }

    @Test
    void addToCount_shouldThrowGenericError() {
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenThrow(NoSuchElementException.class);
        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockCountLocationItemRepository.count((Predicate) any())).thenReturn(21L);

        assertThrows(
            NoSuchElementException.class,
            () -> countsService.addToCount("Testing", "Testing", "Testing", "Testing", 21)
        );
    }

    @Test
    void getCount_remove_leading0() {
        Assert.assertEquals(countsService.stripLeadingZero("054729"), "54729");
    }

    @Test
    void getCount_noLeadingZero_shouldremainUnchanged() {
        Assert.assertEquals(countsService.stripLeadingZero("54729"), "54729");
    }
}
