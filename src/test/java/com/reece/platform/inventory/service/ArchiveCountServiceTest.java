package com.reece.platform.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.repository.CountAuditRepository;
import com.reece.platform.inventory.repository.CountRepository;
import com.reece.platform.inventory.util.TestCommon;
import java.util.Collections;
import java.util.Date;
import org.junit.Test;

public class ArchiveCountServiceTest {

    public CountRepository countRepository = mock(CountRepository.class);
    public CountAuditRepository countAuditRepository = mock(CountAuditRepository.class);

    public ArchiveCountService archiveCountService = new ArchiveCountService(
        countRepository,
        null,
        null,
        null,
        null,
        null,
        countAuditRepository
    );

    @Test
    public void deleteCountsTest_NoCounts() {
        Date sampleDate = new Date();
        when(countRepository.findCountsByErpSystemAndEndDate(ERPSystemName.MINCRON, sampleDate))
            .thenReturn(Collections.emptyList());
        Integer result = archiveCountService.deleteCounts(ERPSystemName.MINCRON, sampleDate);
        assertEquals(0, result);
    }

    @Test
    public void deleteCountsTest() {
        Date sampleDate = new Date();
        when(countRepository.findCountsByErpSystemAndEndDate(ERPSystemName.MINCRON, sampleDate))
            .thenReturn(TestCommon.createSampleListOfCounts());
        Integer result = archiveCountService.deleteCounts(ERPSystemName.MINCRON, sampleDate);
        verify(countRepository, times(2)).deleteById(any());
        verify(countAuditRepository, times(2)).save(any());
        assertEquals(2, result);
    }
}
