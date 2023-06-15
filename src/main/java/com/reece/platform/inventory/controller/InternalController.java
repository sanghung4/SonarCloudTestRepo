package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.CountStatusDTO;
import com.reece.platform.inventory.dto.ErrorDTO;
import com.reece.platform.inventory.exception.EclipseLoadCountsException;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.service.CountsService;
import com.reece.platform.inventory.service.InternalService;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final InternalService internalService;

    /**
     * Load Mincron Counts
     * Counts are queried for then loaded one at a time in the pi-batch job using
     * this endpoint
     * @param branchId
     * @param countId
     * @return
     */
    @PutMapping("/branches/{branchId}/counts/{countId}/_load")
    @ResponseBody
    CountDTO loadMincronCount(@PathVariable String branchId, @PathVariable String countId) {
        return internalService.loadMincronCount(branchId, countId);
    }

    @PutMapping("/eclipse/_load")
    @ResponseBody
    List<CountStatusDTO> loadEclipseAPICount(
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
        ) Date startDate,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
        ) Date endDate
    ) throws EclipseLoadCountsException {
        return internalService.loadEclipseAPICount(startDate, endDate);
    }

    @PutMapping("/counts/_delete")
    public @ResponseBody String deleteErpCounts(
        @RequestParam ERPSystemName erpSystem,
        @RequestParam(defaultValue = "7") int endDateOffset
    ) throws ParseException {
        log.info("Entering deleteErpCounts({}, {})", erpSystem, endDateOffset);
        var countDeleted = internalService.deleteCounts(erpSystem, endDateOffset);
        String successMessage = String.format(
            "Deleted (%s) counts for dates before current date - (%s) days.",
            countDeleted,
            endDateOffset
        );
        log.info(successMessage);
        return successMessage;
    }

    @ExceptionHandler(EclipseLoadCountsException.class)
    public ResponseEntity<ErrorDTO> handEclipseLoadCountsException(EclipseLoadCountsException exception) {
        return new ResponseEntity<>(
            new ErrorDTO("ECLIPSE_LOAD_COUNT_EXCEPTION", exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
