package com.reece.platform.eclipse.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class EclipseCountSummaryDTO {
    private String branchNum;
    private String countId;
    private Integer numItems;
    private LocalDate countDate;
}
