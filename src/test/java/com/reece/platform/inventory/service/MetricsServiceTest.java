package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.TestCommon.*;
import static com.reece.platform.inventory.util.TestCommon.date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reece.platform.inventory.dto.MetricsDTO;
import com.reece.platform.inventory.dto.metrics.*;
import com.reece.platform.inventory.exception.MetricException;
import com.reece.platform.inventory.model.LocationCountMetric;
import com.reece.platform.inventory.model.MetricsLogin;
import com.reece.platform.inventory.repository.LocationCountMetricRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.MetricsRepository;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MetricsServiceTest {

    @Mock
    private MetricsRepository mockMetricsRepository;

    @Mock
    private LocationCountRepository mockLocationCountRepository;

    @Mock
    private LocationCountMetricRepository mockLocationCountMetricRepository;

    @InjectMocks
    private MetricsService metricsService;

    private static final DecimalFormat df = new DecimalFormat("0.0");

    private LocalDate startTestingDate;
    private LocalDate endTestingDate;
    private LocalDate startTestingDate_2;
    private LocalDate endTestingDate_2;
    private int count_1;
    private int count_2;
    private MetricsDivisionDTO metricsDiv_1;
    private MetricsDivisionDTO metricsDiv_2;

    private ArrayList<MetricsDivisionDTO> metricsDivDTOList;
    private ArrayList<MetricsDivisionDTO> metricsDivDTOList_2;
    private double percentUsedForTesting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        metricsService =
            new MetricsService(mockMetricsRepository, mockLocationCountRepository, mockLocationCountMetricRepository);

        // Tests assume count_1 is half as large as count_2
        percentUsedForTesting = 100.0;
        count_1 = 1;
        count_2 = 2;
        startTestingDate = LocalDate.of(2022, 10, 1);
        endTestingDate = LocalDate.of(2022, 11, 1);
        startTestingDate_2 = LocalDate.of(2022, 8, 30);
        endTestingDate_2 = LocalDate.of(2022, 9, 30);

        metricsDiv_1 = new MetricsDivisionDTO();
        metricsDiv_1.setDivision("Testing1");
        metricsDiv_1.setUserCount(count_1);
        metricsDiv_1.setLoginCount(count_1);
        metricsDiv_1.setBranchCount(count_1);
        metricsDiv_2 = new MetricsDivisionDTO();
        metricsDiv_2.setDivision("Testing1");
        metricsDiv_2.setUserCount(count_2);
        metricsDiv_2.setLoginCount(count_2);
        metricsDiv_2.setBranchCount(count_2);

        metricsDivDTOList = new ArrayList<>();
        metricsDivDTOList.add(metricsDiv_1);
        metricsDivDTOList.add(metricsDiv_1);

        metricsDivDTOList_2 = new ArrayList<>();
        metricsDivDTOList_2.add(metricsDiv_2);
        metricsDivDTOList_2.add(metricsDiv_2);
    }

    @Test
    void registerLogin() {
        when(mockMetricsRepository.save(any(MetricsLogin.class))).thenReturn(new MetricsLogin());

        metricsService.registerLogin("Testing", "Testing");

        verify(mockMetricsRepository, times(1)).save(any(MetricsLogin.class));
    }

    @Test
    void getTotalOverview() {
        var expectedResult = new MetricsOverviewDTO();
        expectedResult.setType("totalOverview");
        var metricsList = new ArrayList<MetricsChangeDTO>();

        var userMetrics = new MetricsChangeDTO();
        userMetrics.setMetric("users");
        userMetrics.setQuantity(count_2);
        userMetrics.setPercentageChange(df.format(percentUsedForTesting));

        var loginMetrics = new MetricsChangeDTO();
        loginMetrics.setMetric("logins");
        loginMetrics.setQuantity(count_2);
        loginMetrics.setPercentageChange(df.format(percentUsedForTesting));

        var branchMetrics = new MetricsChangeDTO();
        branchMetrics.setMetric("branches");
        branchMetrics.setQuantity(count_2);
        branchMetrics.setPercentageChange(df.format(percentUsedForTesting));

        metricsList.add(userMetrics);
        metricsList.add(loginMetrics);
        metricsList.add(branchMetrics);
        expectedResult.setResponse(metricsList);

        when(mockMetricsRepository.getUserCountByTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(count_1)
            .thenReturn(count_2);
        when(mockMetricsRepository.getLoginCountByTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(count_1)
            .thenReturn(count_2);
        when(mockMetricsRepository.getBranchCountByTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(count_1)
            .thenReturn(count_2);

        var result = metricsService.getTotalOverview(
            startTestingDate,
            endTestingDate,
            startTestingDate_2,
            endTestingDate_2
        );

        assertEquals(expectedResult, result);
        verify(mockMetricsRepository, times(2)).getUserCountByTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(2)).getLoginCountByTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(2)).getBranchCountByTimeRange(any(Date.class), any(Date.class));
    }

    @Test
    void getTotalUsage() {
        var branches = List.of(new MetricsBranchDTO());
        Map<String, List<MetricsBranchDTO>> branchesByDivisions = new HashMap<>();
        branchesByDivisions.put(metricsDiv_1.getDivision(), branches);
        branchesByDivisions.put(metricsDiv_2.getDivision(), branches);

        for (MetricsDivisionDTO division : metricsDivDTOList) {
            division.setBranches(branchesByDivisions.get(division.getDivision()));
        }

        var totalDivision = new MetricsDivisionDTO();
        totalDivision.setDivision("Grand Total");
        totalDivision.setUserCount(count_1);
        totalDivision.setLoginCount(count_1);
        totalDivision.setBranchCount(count_1);
        metricsDivDTOList.add(totalDivision);

        var expectedResult = new MetricsUsageDTO();
        expectedResult.setType("totalUsage");
        expectedResult.setResponse(metricsDivDTOList);

        when(mockMetricsRepository.getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(metricsDivDTOList);
        when(mockMetricsRepository.getBranchesByTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(branchesByDivisions);
        when(mockMetricsRepository.getUserCountByTimeRange(any(Date.class), any(Date.class))).thenReturn(count_1);
        when(mockMetricsRepository.getLoginCountByTimeRange(any(Date.class), any(Date.class))).thenReturn(count_1);
        when(mockMetricsRepository.getBranchCountByTimeRange(any(Date.class), any(Date.class))).thenReturn(count_1);

        var result = metricsService.getTotalUsage(startTestingDate, endTestingDate);

        assertEquals(expectedResult, result);
        verify(mockMetricsRepository, times(1)).getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(1)).getBranchesByTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(1)).getUserCountByTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(1)).getLoginCountByTimeRange(any(Date.class), any(Date.class));
        verify(mockMetricsRepository, times(1)).getBranchCountByTimeRange(any(Date.class), any(Date.class));
    }

    @Test
    void getPercentageChange() {
        var expectedResult = new MetricsPercentageChangeDTO();
        expectedResult.setType("percentageChange");
        List<MetricsPercentageChangeDivisionDTO> response = new ArrayList<>();
        expectedResult.setResponse(response);

        var oldDivisions = metricsDivDTOList;
        var newDivisions = metricsDivDTOList;

        var divisionKeys = oldDivisions.stream().map(MetricsDivisionDTO::getDivision).collect(Collectors.toSet());
        divisionKeys.addAll(newDivisions.stream().map(MetricsDivisionDTO::getDivision).collect(Collectors.toSet()));
        var divisionKeyList = divisionKeys.stream().sorted().collect(Collectors.toList());

        for (String divisionKey : divisionKeyList) {
            var divisionMetrics = new MetricsPercentageChangeDivisionDTO();
            divisionMetrics.setDivision(divisionKey);
            divisionMetrics.setUserChange(df.format(percentUsedForTesting));
            divisionMetrics.setLoginChange(df.format(percentUsedForTesting));
            divisionMetrics.setBranchChange(df.format(percentUsedForTesting));
            response.add(divisionMetrics);
        }
        var totalChange = new MetricsPercentageChangeDivisionDTO();
        totalChange.setDivision("Grand Total");
        totalChange.setUserChange(df.format(percentUsedForTesting));
        totalChange.setLoginChange(df.format(percentUsedForTesting));
        totalChange.setBranchChange(df.format(percentUsedForTesting));
        response.add(totalChange);

        when(mockMetricsRepository.getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(metricsDivDTOList)
            .thenReturn(metricsDivDTOList_2);

        var result = metricsService.getPercentageChange(
            startTestingDate,
            endTestingDate,
            startTestingDate_2,
            endTestingDate_2
        );

        assertEquals(expectedResult, result);
        verify(mockMetricsRepository, times(2)).getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class));
    }

    @Test
    void getPercentageTotal() {
        var expectedResult = new MetricsPercentageTotalDTO();
        expectedResult.setType("percentageTotal");
        List<MetricsPercentageTotalDivisionDTO> response = new ArrayList<>();
        expectedResult.setResponse(response);

        var divisions = metricsDivDTOList;
        var totalUserCount = divisions.stream().mapToInt(MetricsDivisionDTO::getUserCount).sum();
        var totalLoginCount = divisions.stream().mapToInt(MetricsDivisionDTO::getLoginCount).sum();
        var totalBranchCount = divisions.stream().mapToInt(MetricsDivisionDTO::getBranchCount).sum();

        for (MetricsDivisionDTO division : divisions) {
            MetricsPercentageTotalDivisionDTO divisionPercentage = new MetricsPercentageTotalDivisionDTO();
            divisionPercentage.setDivision(division.getDivision());
            divisionPercentage.setUserCount(division.getUserCount());
            divisionPercentage.setLoginCount(division.getLoginCount());
            divisionPercentage.setBranchCount(division.getBranchCount());
            divisionPercentage.setUserPercentage(division.getUserCount() / (double) totalUserCount);
            divisionPercentage.setLoginPercentage(division.getLoginCount() / (double) totalLoginCount);
            divisionPercentage.setBranchPercentage(division.getBranchCount() / (double) totalBranchCount);
            response.add(divisionPercentage);
        }

        when(mockMetricsRepository.getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class)))
            .thenReturn(metricsDivDTOList);

        var result = metricsService.getPercentageTotal(startTestingDate, endTestingDate);

        assertEquals(expectedResult, result);
        verify(mockMetricsRepository, times(1)).getAllCountsByDivisionAndTimeRange(any(Date.class), any(Date.class));
    }

    @Test
    void addCountMetric_shouldSaveLocationCountMetricToRepository() {
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockLocationCount));

        metricsService.addCountMetric(mockMetricsDTO, "Testing", "Testing");

        verify(mockLocationCountRepository, times(1)).findLocationCount(anyString(), anyString(), anyString());
        verify(mockLocationCountMetricRepository, times(1)).save(any(LocationCountMetric.class));
    }

    @Test
    void addCountMetric_shouldThrowMetricException() {
        var mockMetricsDTOErrorTesting = new MetricsDTO(
            "COMPLETED",
            21,
            21,
            21,
            "Testing", //will cause exception
            "2020-12-05 6:58:02",
            "Testing"
        );
        when(mockLocationCountRepository.findLocationCount(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(mockLocationCount));

        //Logs Parse Exception before throwing MetricsException
        assertThrows(
            MetricException.class,
            () -> metricsService.addCountMetric(mockMetricsDTOErrorTesting, "Testing", "Testing")
        );
    }
}
