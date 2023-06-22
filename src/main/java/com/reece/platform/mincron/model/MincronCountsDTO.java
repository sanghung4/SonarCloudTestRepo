package com.reece.platform.mincron.model;

import lombok.Value;

import java.util.List;

@Value
public class MincronCountsDTO {
    boolean moreThan100Counts;
    List<MincronCountSummaryDTO> counts;
}
