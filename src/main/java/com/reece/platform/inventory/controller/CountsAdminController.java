package com.reece.platform.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.exception.InvalidDateException;
import com.reece.platform.inventory.service.CountAdminService;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counts")
public class CountsAdminController {

    private final CountAdminService countAdminService;

    @GetMapping("")
    public @ResponseBody List<CountStatusDTO> getCountsStatus(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate
    ) {
        return countAdminService.getCountsByTimeRange(startDate, endDate);
    }

    @GetMapping("{countId}")
    public @ResponseBody CountStatusDTO getCountStatus(@PathVariable UUID countId) {
        return countAdminService.getCountStatus(countId);
    }

    @PutMapping("/_load")
    @ResponseBody
    CountStatusDTO loadCount(@RequestParam String branchId, @RequestParam String countId) {
        return countAdminService.loadCount(branchId, countId);
    }

    @PutMapping("/_delete")
    public @ResponseBody String deleteCounts(@RequestBody DeleteCountsDTO deleteCountsDto) throws ParseException {
        var countDeleted = countAdminService.deleteCounts(deleteCountsDto);
        return String.format("Deleted (%s) counts for the specified dates.", countDeleted);
    }

    @PutMapping("/{countId}/_delete")
    public @ResponseBody DeletedCountDTO deleteCount(@PathVariable UUID countId) {
        return countAdminService.deleteCount(countId);
    }

    @PutMapping(value = "/branch/_delete")
    public DeletedMultipleCountResponse deleteMultipleCount(@Valid @RequestBody DeleteCountsDTO deleteCountsDto) {
        return countAdminService.deleteCountsForBranch(deleteCountsDto);
    }

    @PutMapping("/remove")
    public ResponseEntity<String> purgeMincronCounts(@RequestBody SoftDeleteCountsDTO softDeleteCountsDTO)
        throws InvalidDateException, ParseException {
        Integer countDeleted = countAdminService.softDeleteCounts(softDeleteCountsDTO);

        String body = null;
        if (countDeleted == 0) {
            body = "No counts found in the specified date range";
        } else {
            body = String.format("Successfully deleted (%s) Mincron counts for the specified dates.", countDeleted);
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @ExceptionHandler({ ParseException.class })
    public ResponseEntity<String> handleInvalidDateExceptiontException(ParseException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ JsonProcessingException.class })
    public ResponseEntity<String> handleInvalidDateExceptiontException(JsonProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
