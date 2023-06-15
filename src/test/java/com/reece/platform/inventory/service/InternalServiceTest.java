package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.DeleteCountsDTO;
import com.reece.platform.inventory.dto.EclipseLoadCountDto;
import com.reece.platform.inventory.exception.EclipseLoadCountsException;
import com.reece.platform.inventory.model.CountStatus;
import com.reece.platform.inventory.model.ERPSystemName;
import java.text.ParseException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InternalServiceTest {

    @Mock
    private LoadCountService mockLoadCountService;

    @Mock
    private FileTransferService mockFileTransferService;

    @Mock
    private CountAdminService mockCountAdminService;

    @InjectMocks
    private InternalService internalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        internalService =
            new InternalService(mockLoadCountService, mockFileTransferService, mockCountAdminService, null, null);
    }

    @Test
    void loadMincronCount_success() {
        var count = mockCount;
        count.setStatus(CountStatus.NOT_LOADED);
        count.setBranch(mockBranch);
        when(mockLoadCountService.findOrCreateCount(any(), anyString(), anyBoolean())).thenReturn(count);
        doNothing().when(mockLoadCountService).doLoad(any());

        var result = internalService.loadMincronCount("test", "test");

        assertEquals(CountDTO.fromEntity(count), result);
        verify(mockLoadCountService, times(1)).doLoad(any());
    }

    @Test
    void loadMincronCount_success_skip_load() {
        var inProgressCount = mockCount;
        inProgressCount.setBranch(mockBranch);
        inProgressCount.setStatus(CountStatus.IN_PROGRESS);
        when(mockLoadCountService.findOrCreateCount(any(), anyString(), anyBoolean())).thenReturn(inProgressCount);

        var result = internalService.loadMincronCount("test", "test");

        assertEquals(CountDTO.fromEntity(inProgressCount), result);
        verify(mockLoadCountService, times(0)).doLoad(any());
    }

    @Test
    void batchLoadEclipseCounts_success() throws EclipseLoadCountsException {
        var mockEclipseLoadCountDto = new EclipseLoadCountDto();
        mockEclipseLoadCountDto.setBatchNumber("Testing");
        mockEclipseLoadCountDto.setBranch("Testing");
        var mockEclipseLoadCountDto_2 = new EclipseLoadCountDto();
        mockEclipseLoadCountDto_2.setBatchNumber("Testing2");
        mockEclipseLoadCountDto_2.setBranch("Testing");
        when(mockFileTransferService.downloadLatestEclipseCountsFile())
            .thenReturn(List.of(mockEclipseLoadCountDto, mockEclipseLoadCountDto_2));
        doNothing().when(mockLoadCountService).loadCountFromProductList(anyString(), any());

        assertDoesNotThrow(() -> internalService.batchLoadEclipseCounts());
    }

    @Test
    void loadEclipseCounts_shouldThrowEclipseLoadCountsException() throws EclipseLoadCountsException {
        when(mockFileTransferService.downloadLatestEclipseCountsFile()).thenThrow(EclipseLoadCountsException.class);

        assertThrows(EclipseLoadCountsException.class, () -> internalService.batchLoadEclipseCounts());
    }

    @Test
    void deleteCounts_success() throws ParseException {
        Integer val = Integer.valueOf("0");
        when(mockCountAdminService.deleteCounts(mockDeleteCountsDTO)).thenReturn(val);

        var result = internalService.deleteCounts(ERPSystemName.MINCRON, 7);

        assertEquals(val, result);
    }
}
