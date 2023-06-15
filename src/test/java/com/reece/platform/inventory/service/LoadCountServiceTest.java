package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.model.Branch;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.Location;
import com.reece.platform.inventory.model.LocationCount;
import com.reece.platform.inventory.repository.*;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LoadCountServiceTest {

    @Mock
    private BranchRepository mockBranchRepository;

    @Mock
    private CountRepository mockCountRepository;

    @Mock
    private CountLocationItemRepository mockCountLocationItemRepository;

    @Mock
    private LocationCountRepository mockLocationCountRepository;

    @Mock
    private LocationRepository mockLocationRepository;

    @Mock
    private ERPSystemFactory mockERPSystemFactory;

    @InjectMocks
    private LoadCountService loadCountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loadCountService =
            new LoadCountService(
                mockBranchRepository,
                mockCountRepository,
                mockCountLocationItemRepository,
                mockLocationCountRepository,
                mockLocationRepository,
                mockERPSystemFactory
            );
    }

    @Test
    void doLoad_success() {
        mockCount.setId(UUID.randomUUID());
        mockBranch.setErpBranchNum("Testing");
        mockCount.setBranch(mockBranch);

        when(mockCountRepository.save(any(Count.class))).thenReturn(mockCount);
        when(mockERPSystemFactory.getErpSystem(anyString())).thenReturn(mockERPSystem);
        when(mockLocationRepository.save(any(Location.class))).thenReturn(new Location());

        loadCountService.doLoad(mockCount);

        verify(mockCountRepository, times(2)).save(any(Count.class));
        verify(mockERPSystemFactory, times(2)).getErpSystem(anyString());
        verify(mockBranchRepository, times(1)).save(any(Branch.class));
        verify(mockLocationRepository, times(2)).findByBranchAndErpLocationId(any(Branch.class), anyString());
        verify(mockLocationRepository, times(2)).save(any(Location.class));
        verify(mockLocationCountRepository, times(2)).findByLocationAndCount(any(Location.class), any(Count.class));
        verify(mockLocationCountRepository, times(2)).save(any(LocationCount.class));
        verify(mockCountLocationItemRepository, times(2)).saveAll(anyIterable());
    }

    @Test
    void doLoad_Exception() {
        mockCount.setId(UUID.randomUUID());
        mockBranch.setErpBranchNum("Testing");
        mockCount.setBranch(mockBranch);

        when(mockCountRepository.save(any(Count.class))).thenReturn(mockCount);

        loadCountService.doLoad(mockCount);

        verify(mockCountRepository, times(2)).save(any(Count.class));
        verify(mockERPSystemFactory, times(1)).getErpSystem(anyString());
        verify(mockBranchRepository, times(0)).save(any(Branch.class));
        verify(mockLocationRepository, times(0)).findByBranchAndErpLocationId(any(Branch.class), anyString());
        verify(mockLocationRepository, times(0)).save(any(Location.class));
        verify(mockLocationCountRepository, times(0)).findByLocationAndCount(any(Location.class), any(Count.class));
        verify(mockLocationCountRepository, times(0)).save(any(LocationCount.class));
        verify(mockCountLocationItemRepository, times(0)).saveAll(anyIterable());
    }
}
