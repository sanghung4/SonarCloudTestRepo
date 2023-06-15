package com.reece.platform.inventory.service;

import static com.reece.platform.inventory.util.DateUtil.extractDate;

import com.reece.platform.inventory.dto.MetricsDTO;
import com.reece.platform.inventory.dto.metrics.*;
import com.reece.platform.inventory.exception.MetricException;
import com.reece.platform.inventory.model.LocationCount;
import com.reece.platform.inventory.model.LocationCountMetric;
import com.reece.platform.inventory.model.MetricsCompletionStatus;
import com.reece.platform.inventory.model.MetricsLogin;
import com.reece.platform.inventory.repository.LocationCountMetricRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.MetricsRepository;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetricsService {

    private final MetricsRepository metricsRepository;
    private final LocationCountRepository locationCountRepository;
    private final LocationCountMetricRepository locationCountMetricRepository;

    private static final DecimalFormat df = new DecimalFormat("0.0");

    public void registerLogin(String branchId, String username) {
        MetricsLogin metricsLogin = new MetricsLogin();
        metricsLogin.setBranchId(branchId);
        metricsLogin.setUserEmail(username);
        metricsLogin.setLoginAt(new Date());
        metricsRepository.save(metricsLogin);
    }

    public MetricsDTO addCountMetric(MetricsDTO metricsDto, String branchId, String countId) {
        try {
            LocationCount locationCount = locationCountRepository
                .findLocationCount(branchId, countId, metricsDto.getLocation())
                .orElseGet(() -> null);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (locationCount != null) {
                LocationCountMetric locationCountMetric = new LocationCountMetric();
                locationCountMetric.setLocationCount(locationCount);
                locationCountMetric.setCompletionStatus(MetricsCompletionStatus.valueOf(metricsDto.getStatus()));
                locationCountMetric.setTimeStarted(sdf.parse(metricsDto.getTimeStarted()));
                locationCountMetric.setTimeEnded(sdf.parse(metricsDto.getTimeEnded()));
                locationCountMetric.setCounted(metricsDto.getCounted());
                locationCountMetric.setNeededToBeCounted(metricsDto.getNeedToBeCounted());
                locationCountMetric.setTotal(metricsDto.getTotal());

                locationCountMetricRepository.save(locationCountMetric);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MetricException();
        }

        return metricsDto;
    }

    public MetricsOverviewDTO getTotalOverview(
        LocalDate startDateWeekOld,
        LocalDate endDateWeekOld,
        LocalDate startDateWeekNew,
        LocalDate endDateWeekNew
    ) {
        var sdo = extractDate(startDateWeekOld, true);
        var edo = extractDate(endDateWeekOld, false);
        var sdn = extractDate(startDateWeekNew, true);
        var edn = extractDate(endDateWeekNew, false);
        var res = new MetricsOverviewDTO();

        res.setType("totalOverview");
        var metricsList = new ArrayList<MetricsChangeDTO>();
        var userMetrics = new MetricsChangeDTO();

        userMetrics.setMetric("users");
        var userCountOld = metricsRepository.getUserCountByTimeRange(sdo, edo);
        var userCountNew = metricsRepository.getUserCountByTimeRange(sdn, edn);
        userMetrics.setQuantity(userCountNew);
        userMetrics.setPercentageChange(df.format(calculatePercentageDifference(userCountOld, userCountNew)));
        var loginMetrics = new MetricsChangeDTO();

        loginMetrics.setMetric("logins");
        var loginCountOld = metricsRepository.getLoginCountByTimeRange(sdo, edo);
        var loginCountNew = metricsRepository.getLoginCountByTimeRange(sdn, edn);
        loginMetrics.setQuantity(loginCountNew);
        loginMetrics.setPercentageChange(df.format(calculatePercentageDifference(loginCountOld, loginCountNew)));
        var branchMetrics = new MetricsChangeDTO();

        branchMetrics.setMetric("branches");
        var branchCountOld = metricsRepository.getBranchCountByTimeRange(sdo, edo);
        var branchCountNew = metricsRepository.getBranchCountByTimeRange(sdn, edn);
        branchMetrics.setQuantity(branchCountNew);
        branchMetrics.setPercentageChange(df.format(calculatePercentageDifference(branchCountOld, branchCountNew)));
        metricsList.add(userMetrics);

        metricsList.add(loginMetrics);
        metricsList.add(branchMetrics);
        res.setResponse(metricsList);
        return res;
    }

    public MetricsUsageDTO getTotalUsage(LocalDate startDate, LocalDate endDate) {
        var sd = extractDate(startDate, true);
        var ed = extractDate(endDate, false);
        var res = new MetricsUsageDTO();
        res.setType("totalUsage");
        List<MetricsDivisionDTO> divisions = metricsRepository.getAllCountsByDivisionAndTimeRange(sd, ed);
        res.setResponse(divisions);
        var branchDivisionMap = metricsRepository.getBranchesByTimeRange(sd, ed);
        for (MetricsDivisionDTO division : divisions) {
            division.setBranches(branchDivisionMap.get(division.getDivision()));
        }
        var totalDivision = new MetricsDivisionDTO();
        totalDivision.setDivision("Grand Total");
        totalDivision.setUserCount(metricsRepository.getUserCountByTimeRange(sd, ed));
        totalDivision.setLoginCount(metricsRepository.getLoginCountByTimeRange(sd, ed));
        totalDivision.setBranchCount(metricsRepository.getBranchCountByTimeRange(sd, ed));
        divisions.add(totalDivision);
        return res;
    }

    public MetricsPercentageChangeDTO getPercentageChange(
        LocalDate startDateWeekOld,
        LocalDate endDateWeekOld,
        LocalDate startDateWeekNew,
        LocalDate endDateWeekNew
    ) {
        var sdo = extractDate(startDateWeekOld, true);
        var edo = extractDate(endDateWeekOld, false);
        var sdn = extractDate(startDateWeekNew, true);
        var edn = extractDate(endDateWeekNew, false);

        var res = new MetricsPercentageChangeDTO();
        res.setType("percentageChange");
        List<MetricsPercentageChangeDivisionDTO> response = new ArrayList<>();
        res.setResponse(response);

        var oldDivisions = metricsRepository.getAllCountsByDivisionAndTimeRange(sdo, edo);
        var newDivisions = metricsRepository.getAllCountsByDivisionAndTimeRange(sdn, edn);

        var divisionKeys = oldDivisions.stream().map(div -> div.getDivision()).collect(Collectors.toSet());
        divisionKeys.addAll(newDivisions.stream().map(div -> div.getDivision()).collect(Collectors.toSet()));
        var divisionKeyList = divisionKeys.stream().sorted().collect(Collectors.toList());

        for (String divisionKey : divisionKeyList) {
            var divisionMetrics = new MetricsPercentageChangeDivisionDTO();
            var oldDivision = oldDivisions
                .stream()
                .filter(e -> e.getDivision().equals(divisionKey))
                .findFirst()
                .orElse(emptyCountDivision());
            var newDivision = newDivisions
                .stream()
                .filter(e -> e.getDivision().equals(divisionKey))
                .findFirst()
                .orElse(emptyCountDivision());
            divisionMetrics.setDivision(divisionKey);
            divisionMetrics.setUserChange(
                df.format(calculatePercentageDifference(oldDivision.getUserCount(), newDivision.getUserCount()))
            );
            divisionMetrics.setLoginChange(
                df.format(calculatePercentageDifference(oldDivision.getLoginCount(), newDivision.getLoginCount()))
            );
            divisionMetrics.setBranchChange(
                df.format(calculatePercentageDifference(oldDivision.getBranchCount(), newDivision.getBranchCount()))
            );
            response.add(divisionMetrics);
        }
        var totalChange = new MetricsPercentageChangeDivisionDTO();
        totalChange.setDivision("Grand Total");
        var oldUserCount = oldDivisions.stream().mapToInt(div -> div.getUserCount()).sum();
        var newUserCount = newDivisions.stream().mapToInt(div -> div.getUserCount()).sum();
        var oldLoginCount = oldDivisions.stream().mapToInt(div -> div.getLoginCount()).sum();
        var newLoginCount = newDivisions.stream().mapToInt(div -> div.getLoginCount()).sum();
        var oldBranchCount = oldDivisions.stream().mapToInt(div -> div.getBranchCount()).sum();
        var newBranchCount = newDivisions.stream().mapToInt(div -> div.getBranchCount()).sum();
        totalChange.setUserChange(df.format(calculatePercentageDifference(oldUserCount, newUserCount)));
        totalChange.setLoginChange(df.format(calculatePercentageDifference(oldLoginCount, newLoginCount)));
        totalChange.setBranchChange(df.format(calculatePercentageDifference(oldBranchCount, newBranchCount)));
        response.add(totalChange);
        return res;
    }

    public MetricsPercentageTotalDTO getPercentageTotal(LocalDate startDate, LocalDate endDate) {
        var sd = extractDate(startDate, true);
        var ed = extractDate(endDate, false);

        var res = new MetricsPercentageTotalDTO();
        res.setType("percentageTotal");
        List<MetricsPercentageTotalDivisionDTO> response = new ArrayList<>();
        res.setResponse(response);

        var divisions = metricsRepository.getAllCountsByDivisionAndTimeRange(sd, ed);
        var totalUserCount = divisions.stream().mapToInt(MetricsDivisionDTO::getUserCount).sum();
        var totalLoginCount = divisions.stream().mapToInt(MetricsDivisionDTO::getLoginCount).sum();
        var totalBranchCount = divisions.stream().mapToInt(MetricsDivisionDTO::getBranchCount).sum();

        for (MetricsDivisionDTO division : divisions) {
            MetricsPercentageTotalDivisionDTO divisionPercentage = new MetricsPercentageTotalDivisionDTO();
            divisionPercentage.setDivision(division.getDivision());
            divisionPercentage.setUserCount(division.getUserCount());
            divisionPercentage.setLoginCount(division.getLoginCount());
            divisionPercentage.setBranchCount(division.getBranchCount());
            divisionPercentage.setUserPercentage(division.getUserCount() / Double.valueOf(totalUserCount));
            divisionPercentage.setLoginPercentage(division.getLoginCount() / Double.valueOf(totalLoginCount));
            divisionPercentage.setBranchPercentage(division.getBranchCount() / Double.valueOf(totalBranchCount));
            response.add(divisionPercentage);
        }
        return res;
    }

    private Double calculatePercentageDifference(Integer oldValue, Integer newValue) {
        try {
            return (newValue - oldValue) / Double.valueOf(oldValue) * 100;
        } catch (Exception e) {
            return null;
        }
    }

    private MetricsDivisionDTO emptyCountDivision() {
        MetricsDivisionDTO res = new MetricsDivisionDTO();
        res.setUserCount(0);
        res.setBranchCount(0);
        res.setLoginCount(0);
        return res;
    }
}
