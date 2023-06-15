package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.dto.metrics.MetricsBranchDTO;
import com.reece.platform.inventory.dto.metrics.MetricsDivisionDTO;
import com.reece.platform.inventory.model.MetricsLogin;
import com.reece.platform.inventory.model.QMetricsBranchDivision;
import com.reece.platform.inventory.model.QMetricsLogin;
import com.reece.platform.inventory.repository.MetricsCustomRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MetricsCustomRepositoryImpl extends QuerydslRepositorySupport implements MetricsCustomRepository {
    public MetricsCustomRepositoryImpl() {
        super(MetricsLogin.class);
    }

    QMetricsLogin metricsLogin = QMetricsLogin.metricsLogin;
    QMetricsBranchDivision metricsBranchDivision = QMetricsBranchDivision.metricsBranchDivision;

    @Override
    public Integer getLoginCountByTimeRange(Date startDate, Date endDate) {
        return Math.toIntExact(
            from(metricsLogin)
                    .join(metricsBranchDivision)
                    .on(metricsLogin.branchId.eq(metricsBranchDivision.branchId))
                    .select(metricsLogin.userEmail, metricsBranchDivision.branchId)
                    .distinct()
            .where(metricsLogin.loginAt.goe(startDate)
                .and(metricsLogin.loginAt.lt(endDate)))
            .fetchCount());
    }

    @Override
    public Integer getUserCountByTimeRange(Date startDate, Date endDate) {
        // This query explicitly joins against metrics_branch_division to count logins to different divisions
        // as separate users
        val q = from(metricsLogin)
                .join(metricsBranchDivision)
                .on(metricsLogin.branchId.eq(metricsBranchDivision.branchId))
                .where(metricsLogin.loginAt.goe(startDate)
                        .and(metricsLogin.loginAt.lt(endDate)))
                .groupBy(metricsBranchDivision.divisionId)
                .select(
                        metricsLogin.userEmail.countDistinct()
                );
        return Math.toIntExact(q.fetch().stream().mapToLong(e -> e.longValue()).sum());
    }

    @Override
    public Integer getBranchCountByTimeRange(Date startDate, Date endDate) {
        return Math.toIntExact(
            from(metricsLogin)
                    .join(metricsBranchDivision)
                    .on(metricsLogin.branchId.eq(metricsBranchDivision.branchId))
                .select(metricsLogin.branchId)
                .distinct()
                .where(metricsLogin.loginAt.goe(startDate)
                        .and(metricsLogin.loginAt.lt(endDate)))
                .fetchCount());
    }

    public List<MetricsDivisionDTO> getAllCountsByDivisionAndTimeRange(Date startDate, Date endDate) {
        val q = from(metricsLogin)
                .join(metricsBranchDivision)
                .on(metricsLogin.branchId.eq(metricsBranchDivision.branchId))
                .where(metricsLogin.loginAt.goe(startDate)
                        .and(metricsLogin.loginAt.lt(endDate)))
                .groupBy(metricsBranchDivision.divisionId)
                .select(
                        metricsBranchDivision.divisionId,
                        metricsLogin.count(),
                        metricsLogin.userEmail.countDistinct(),
                        metricsLogin.branchId.countDistinct()
                );

        return q.fetch().stream().map(res -> {
            String divisionId = res.get(metricsBranchDivision.divisionId);
            Long loginCount = res.get(1, Long.class);
            Long userCount = res.get(2, Long.class);
            Long branchCount = res.get(3, Long.class);
            val result = new MetricsDivisionDTO();
            result.setDivision(divisionId);
            result.setLoginCount(Math.toIntExact(loginCount));
            result.setUserCount(Math.toIntExact(userCount));
            result.setBranchCount(Math.toIntExact(branchCount));
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<MetricsBranchDTO>> getBranchesByTimeRange(Date startDate, Date endDate) {
        val q = from(metricsLogin)
            .join(metricsBranchDivision)
            .on(metricsLogin.branchId.eq(metricsBranchDivision.branchId))
        .where(metricsLogin.loginAt.goe(startDate)
            .and(metricsLogin.loginAt.lt(endDate)))
        .groupBy(
            metricsBranchDivision.branchId,
            metricsBranchDivision.branchName,
            metricsBranchDivision.branchState,
            metricsBranchDivision.divisionId
        )
        .select(
            metricsLogin.count(),
            metricsLogin.userEmail.countDistinct(),
            metricsBranchDivision.branchId,
            metricsBranchDivision.branchName,
            metricsBranchDivision.branchState,
            metricsBranchDivision.divisionId
        );

        return q.fetch().stream().map(res -> {
            val result = new MetricsBranchDTO();

            Long loginCount = res.get(0, Long.class);
            Long userCount = res.get(1, Long.class);
            String branchId = res.get(metricsBranchDivision.branchId);
            String branchName = res.get(metricsBranchDivision.branchName);
            String branchState = res.get(metricsBranchDivision.branchState);
            String divisionId = res.get(metricsBranchDivision.divisionId);
            result.setLoginCount(Math.toIntExact(loginCount));
            result.setUserCount(Math.toIntExact(userCount));
            result.setId(branchId);
            result.setCity(branchName);
            result.setState(branchState);
            result.setDivisionId(divisionId);
            return result;
        }).collect(Collectors.groupingBy(e -> e.getDivisionId(), Collectors.toList()));
    }
}
