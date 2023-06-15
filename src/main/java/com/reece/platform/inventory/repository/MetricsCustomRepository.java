package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.dto.metrics.MetricsBranchDTO;
import com.reece.platform.inventory.dto.metrics.MetricsDivisionDTO;
import com.reece.platform.inventory.model.MetricsBranchDivision;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MetricsCustomRepository {
    Integer getLoginCountByTimeRange(Date startDate, Date endDate);
    Integer getUserCountByTimeRange(Date startDate, Date endDate);
    Integer getBranchCountByTimeRange(Date startDate, Date endDate);
    List<MetricsDivisionDTO> getAllCountsByDivisionAndTimeRange(Date startDate, Date endDate);
    Map<String, List<MetricsBranchDTO>> getBranchesByTimeRange(Date startDate, Date endDate);
}
