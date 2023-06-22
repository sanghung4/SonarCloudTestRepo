package com.reece.platform.mincron.model;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Value
public class MincronCountSummaryDTO {
    private static final DateTimeFormatter MDY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMddyy");
    String branchNum;
    String countId;
    int numItems;
    LocalDate countDate;
    String status;

    public MincronCountSummaryDTO(String branchNum, String countId, int numItems, String countDate, String status) {
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
        this.status = status;
    }
}
