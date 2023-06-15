package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.date;
import static com.reece.platform.inventory.util.TestCommon.mockCountStatusDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.dto.DeleteCountsDTO;
import com.reece.platform.inventory.dto.DeletedCountDTO;
import com.reece.platform.inventory.dto.DeletedMultipleCountResponse;
import com.reece.platform.inventory.dto.internal.MincronAllCountsDTO;
import com.reece.platform.inventory.exception.InValidCountDeletionException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.external.mincron.MincronService;
import com.reece.platform.inventory.repository.CountRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CountAdminServiceTest {

    @Mock
    private CountRepository mockCountRepository;

    @Mock
    private ArchiveCountService mockArchiveCountService;

    @Mock
    private LoadCountService mockLoadCountService;

    @Mock
    private MincronService mockMincronService;

    @Mock
    private EclipseService mockEclipseService;

    @InjectMocks
    private CountAdminService countAdminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        countAdminService =
            new CountAdminService(
                mockCountRepository,
                mockLoadCountService,
                mockMincronService,
                mockEclipseService,
                mockArchiveCountService
            );
    }

    @Test
    void getCountsByTimeRange() {
        var mincronCounts = new MincronAllCountsDTO();
        mincronCounts.setCounts(Collections.emptyList());
        when(mockCountRepository.findCountsByTimeRange(any(), any())).thenReturn(List.of(mockCountStatusDTO));
        when(mockCountRepository.findCountsByIds(any())).thenReturn(Collections.emptyList());
        when(mockMincronService.getCounts()).thenReturn(mincronCounts);
        when(mockEclipseService.getCounts(any(), any())).thenReturn(Collections.emptyList());

        countAdminService.getCountsByTimeRange(date, date);

        verify(mockCountRepository, times(1)).findCountsByTimeRange(any(), any());
    }

    @Test
    void deleteCountsForBranchTest_invalidCountException() {
        DeleteCountsDTO deleteMincronCountsDTO = new DeleteCountsDTO(null, null);
        deleteMincronCountsDTO.setBranchId("020");
        deleteMincronCountsDTO.setCountId("150");

        List<UUID> sampleUUids = Collections.emptyList();

        when(mockCountRepository.findCountsUUID(any(), any(), any())).thenReturn(sampleUUids);

        InValidCountDeletionException exception = assertThrows(
            InValidCountDeletionException.class,
            () -> countAdminService.deleteCountsForBranch(deleteMincronCountsDTO),
            "Invalid countIds"
        );

        assertEquals("No counts found to be deleted for countid (150) and branchId (020)", exception.getErrorMessage());
    }

    @Test
    void deleteCountsForBranchTest() {
        DeleteCountsDTO deleteMincronCountsDTO = new DeleteCountsDTO(null, null);
        deleteMincronCountsDTO.setBranchId("020");
        deleteMincronCountsDTO.setCountId("150");

        DeletedCountDTO sampleDeletedCountDTO = new DeletedCountDTO(1, 1, 1, 1, 1);

        List<UUID> sampleUUids = Arrays.asList(UUID.randomUUID());

        when(mockCountRepository.findCountsUUID(any(), any(), any())).thenReturn(sampleUUids);
        when(mockArchiveCountService.deleteCount(any())).thenReturn(new DeletedCountDTO(2, 1, 3, 1, 4));

        DeletedMultipleCountResponse deletedMultipleCountResponse = countAdminService.deleteCountsForBranch(
            deleteMincronCountsDTO
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(deletedMultipleCountResponse.getDeletedCounts()).isNotEmpty();
        softAssertions.assertThat(deletedMultipleCountResponse.getDeletedCounts().size()).isEqualTo(1);
        softAssertions
            .assertThat(deletedMultipleCountResponse.getDeletedCounts().get(0).getCountLocationItemQuantities())
            .isEqualTo(3);
        softAssertions
            .assertThat(deletedMultipleCountResponse.getDeletedCounts().get(0).getCountLocations())
            .isEqualTo(2);
        softAssertions.assertThat(deletedMultipleCountResponse.getDeletedCounts().get(0).getWriteIns()).isEqualTo(4);
        softAssertions
            .assertThat(deletedMultipleCountResponse.getDeletedCounts().get(0).getCountLocationItems())
            .isEqualTo(1);
        softAssertions.assertAll();
    }
}
