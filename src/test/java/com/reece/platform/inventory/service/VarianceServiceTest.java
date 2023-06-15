package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.dto.variance.VarianceLocationsDTO;
import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.exception.NextLocationNotFoundException;
import com.reece.platform.inventory.exception.VarianceNotFoundException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.external.mincron.MincronService;
import com.reece.platform.inventory.model.CountItem;
import com.reece.platform.inventory.model.variance.VarianceDetails;
import com.reece.platform.inventory.repository.CountItemQuantityRepository;
import com.reece.platform.inventory.repository.CountLocationItemRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.VarianceCountItemQuantityRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VarianceServiceTest {

    @Mock
    private EclipseService mockEclipseService;

    @Mock
    private MincronService mockMincronService;

    @Mock
    private CountLocationItemRepository mockCountLocationItemRepository;

    @Mock
    private LocationCountRepository mockLocationCountRepository;

    @Mock
    private CountItemQuantityRepository mockCountItemQuantityRepository;

    @Mock
    private VarianceCountItemQuantityRepository mockVarianceCountItemQuantityRepository;

    @Mock
    private ERPSystemFactory mockErpSystemFactory;

    @InjectMocks
    private VarianceService varianceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        varianceService =
            new VarianceService(
                mockEclipseService,
                mockMincronService,
                mockCountLocationItemRepository,
                mockLocationCountRepository,
                mockVarianceCountItemQuantityRepository,
                mockErpSystemFactory
            );

        var detailsList = new ArrayList<VarianceDetails>();
        mockVarianceDetails.setLocation("Testing");
        mockVarianceDetails.setErpProductID("Testing");
        mockVarianceDetails.setOnHandCost(21.00);
        mockVarianceDetails.setCountedCost(21.00);
        detailsList.add(mockVarianceDetails);
        detailsList.add(mockVarianceDetails);
        mockVarianceDetailsDTO.setCounts(detailsList);

        mockVarianceLocationSummaryList.add(mockVarianceLocationSummary);
        mockVarianceLocationSummaryList.add(mockVarianceLocationSummary);
        mockVarianceLocationSummaryList.add(mockVarianceLocationSummary);

        mockCountItem.setMostRecentQuantity(3);
        mockCountItem.setTagNum("Testing");
    }

    @Test
    void getVarianceSummary_shouldReturnVarianceSummaryDTOBasedOnCountID() {
        when(mockEclipseService.getVarianceSummary(anyString())).thenReturn(mockVarianceSummaryDTO);

        varianceService.getVarianceSummary("Testing", ECLIPSE_BRANCH_ID);

        verify(mockEclipseService, times(1)).getVarianceSummary(anyString());
    }

    @Test
    void getVarianceSummary_shouldReturnMincronVarianceSummaryDTOBasedOnCountID() {
        when(mockEclipseService.getVarianceSummary(anyString())).thenReturn(mockVarianceSummaryDTO);

        varianceService.getVarianceSummary("Testing", MINCRON_BRANCH_ID);

        verify(mockMincronService, times(1)).getMincronVarianceSummary(anyString(), anyString());
    }

    @Test
    void loadVarianceDetails_shouldLoadVarianceCountsInToCountLocationItemRepository() {
        when(mockEclipseService.getVarianceDetails(anyString())).thenReturn(mockVarianceDetailsDTO);
        when(mockCountLocationItemRepository.findCountItem(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(new CountItem()));
        when(mockCountLocationItemRepository.save(any(CountItem.class))).thenReturn(new CountItem());

        varianceService.loadVarianceDetails("Testing", "Testing");

        verify(mockEclipseService, times(1)).getVarianceDetails(anyString());
        verify(mockCountLocationItemRepository, times(2))
            .findCountItem(anyString(), anyString(), anyString(), anyString());
        verify(mockCountLocationItemRepository, times(2)).save(any(CountItem.class));
    }

    @Test
    void getVarianceLocations_shouldReturnVarianceLocationsDTOBasedOnBranchIDAndCountID() {
        var expectedResult = new VarianceLocationsDTO();
        expectedResult.setLocations(mockVarianceLocationSummaryList);
        expectedResult.setTotalLocations((long) mockVarianceLocationSummaryList.size());

        when(mockLocationCountRepository.findVarianceLocationSummaries(anyString(), anyString()))
            .thenReturn(mockVarianceLocationSummaryList);

        var result = varianceService.getVarianceLocations("Testing", "Testing");

        assertEquals(expectedResult, result);
        verify(mockLocationCountRepository, times(1)).findVarianceLocationSummaries(anyString(), anyString());
    }

    @Test
    void getVarianceLocation_shouldReturnVarianceLocationDTOBasedOnBranchIDCountIDAndLocationID()
        throws VarianceNotFoundException {
        when(mockLocationCountRepository.findVarianceLocation(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockVarianceLocationDTO));

        varianceService.getVarianceLocation("Testing", "Testing", "Testing");

        verify(mockLocationCountRepository, times(1)).findVarianceLocation(anyString(), anyString(), anyString());
    }

    @Test
    void getVarianceNextLocation_shouldGetNextLocationDTO() {
        when(mockLocationCountRepository.getVarianceNextLocationId(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of("Testing"));

        varianceService.getVarianceNextLocation("Testing", "Testing", "Testing");

        verify(mockLocationCountRepository, times(1)).getVarianceNextLocationId(anyString(), anyString(), anyString());
    }

    @Test
    void getVarianceNextLocation_shouldThrowNextLocationNotFoundException() {
        when(mockLocationCountRepository.getVarianceNextLocationId(anyString(), anyString(), anyString()))
            .thenThrow(NextLocationNotFoundException.class);

        assertThrows(
            NextLocationNotFoundException.class,
            () -> mockLocationCountRepository.getVarianceNextLocationId(anyString(), anyString(), anyString())
        );
    }

    @Test
    void stageVarianceCount_shouldStageVarianceCount() {
        when(mockCountLocationItemRepository.findVarianceCountItem(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockCountItem));

        varianceService.stageVarianceCount("Testing", "Testing", "Testing", "Testing", 2);

        verify(mockCountLocationItemRepository, times(1))
            .findVarianceCountItem(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void commitVarianceCount_shouldCommitVarianceCount() {
        var countItemList = new ArrayList<CountItem>();
        countItemList.add(mockCountItem);
        countItemList.add(mockCountItem);
        countItemList.add(mockCountItem);

        when(mockCountLocationItemRepository.findVarianceStagedCountItems(anyString(), anyString(), anyString()))
            .thenReturn(countItemList);
        when(mockErpSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockCountLocationItemRepository.saveAll(anyList()))
            .thenAnswer(saveAllFunc -> saveAllFunc.getArguments()[0]);
        when(mockVarianceCountItemQuantityRepository.saveAll(anyList()))
            .thenAnswer(saveAllFunc -> saveAllFunc.getArguments()[0]);

        varianceService.commitVarianceCount("Testing", "Testing", "Testing");

        verify(mockCountLocationItemRepository, times(1))
            .findVarianceStagedCountItems(anyString(), anyString(), anyString());
        verify(mockErpSystemFactory, times(1)).getErpSystem(anyString());
        verify(mockCountLocationItemRepository, times(1)).saveAll(anyList());
        verify(mockVarianceCountItemQuantityRepository, times(1)).saveAll(anyIterable());
    }
}
