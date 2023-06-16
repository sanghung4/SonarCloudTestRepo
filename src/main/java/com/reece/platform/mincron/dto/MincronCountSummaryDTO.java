package com.reece.platform.mincron.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MincronCountSummaryDTO {

    private static final DateTimeFormatter MDY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMddyy");

    private String branchNum;
    private String countId;
    private Integer numItems;
    private LocalDate countDate;

    public MincronCountSummaryDTO(String branchNum, String countId, int numItems, String countDate) {
        this.branchNum = branchNum;
        this.countId = countId;
        this.numItems = numItems;
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(String.format("%06d", Integer.parseInt(countDate)), MDY_DATE_FORMAT);
        } catch (DateTimeParseException | NumberFormatException e) {
            log.error("Exception caught parsing Mincron date:" + countDate, e);
        }
        this.countDate = parsedDate;
    }
}
