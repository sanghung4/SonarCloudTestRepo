package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.ErrorDTO;
import com.reece.platform.inventory.dto.MetricsDTO;
import com.reece.platform.inventory.dto.metrics.MetricsOverviewDTO;
import com.reece.platform.inventory.dto.metrics.MetricsPercentageChangeDTO;
import com.reece.platform.inventory.dto.metrics.MetricsPercentageTotalDTO;
import com.reece.platform.inventory.dto.metrics.MetricsUsageDTO;
import com.reece.platform.inventory.exception.MetricException;
import com.reece.platform.inventory.service.MetricsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    @PostMapping("_login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerLogin(JwtAuthenticationToken authentication, @RequestHeader("X-Branch-Id") String branchId) {
        metricsService.registerLogin(branchId, authentication.getTokenAttributes().get("sub").toString());
    }

    @PostMapping("/completion")
    public @ResponseBody MetricsDTO updateMetrics(
        @RequestHeader("X-Branch-Id") String branchId,
        @RequestHeader("X-Count-Id") String countId,
        @RequestBody MetricsDTO metricsDto
    ) {
        return metricsService.addCountMetric(metricsDto, branchId, countId);
    }

    @GetMapping(value = "totalOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody MetricsOverviewDTO getTotalOverview(
        @RequestParam("startDateWeekOld") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateWeekOld,
        @RequestParam("endDateWeekOld") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateWeekOld,
        @RequestParam("startDateWeekNew") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateWeekNew,
        @RequestParam("endDateWeekNew") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateWeekNew
    ) {
        return metricsService.getTotalOverview(startDateWeekOld, endDateWeekOld, startDateWeekNew, endDateWeekNew);
    }

    @GetMapping(value = "totalUsage", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody MetricsUsageDTO getTotalUsage(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return metricsService.getTotalUsage(startDate, endDate);
    }

    @GetMapping(value = "percentageChange", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody MetricsPercentageChangeDTO getPercentageChange(
        @RequestParam("startDateWeekOld") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateWeekOld,
        @RequestParam("endDateWeekOld") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateWeekOld,
        @RequestParam("startDateWeekNew") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateWeekNew,
        @RequestParam("endDateWeekNew") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateWeekNew
    ) {
        return metricsService.getPercentageChange(startDateWeekOld, endDateWeekOld, startDateWeekNew, endDateWeekNew);
    }

    @GetMapping(value = "percentageTotal", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody MetricsPercentageTotalDTO getPercentageTotal(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return metricsService.getPercentageTotal(startDate, endDate);
    }

    @ExceptionHandler(MetricException.class)
    public ResponseEntity<ErrorDTO> handleMetricException(MetricException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("UNABLE_TO_CREATE_METRIC", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
