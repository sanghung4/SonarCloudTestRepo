package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.NextLocationDTO;
import com.reece.platform.inventory.dto.UpdateCountDTO;
import com.reece.platform.inventory.dto.variance.VarianceDetailsDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationsDTO;
import com.reece.platform.inventory.dto.variance.VarianceSummaryDTO;
import com.reece.platform.inventory.exception.VarianceNotFoundException;
import com.reece.platform.inventory.service.VarianceService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/variance")
@Validated
public class VarianceController {

    private final VarianceService varianceService;

    @GetMapping("/summary")
    public @ResponseBody VarianceSummaryDTO getVarianceSummary(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId
    ) {
        return varianceService.getVarianceSummary(countId, branchId);
    }

    @PostMapping("_load")
    public @ResponseBody void loadVarianceDetails(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId
    ) {
        varianceService.loadVarianceDetails(branchId, countId);
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations")
    public @ResponseBody VarianceLocationsDTO getVarianceLocations(
        @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
        @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) {
        return varianceService.getVarianceLocations(branchId, countId);
    }

    @GetMapping("/locations/{locationId}")
    public @ResponseBody VarianceLocationDTO getVarianceLocation(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        @PathVariable @NotBlank(
            message = "Invalid Parameter: 'locationId' is blank, which is not valid"
        ) String locationId
    ) throws VarianceNotFoundException {
        return varianceService.getVarianceLocation(branchId, countId, locationId);
    }

    @GetMapping("/locations/{locationId}/_next")
    public @ResponseBody NextLocationDTO getVarianceNextLocation(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        @PathVariable @NotBlank(
            message = "Invalid Parameter: 'locationId' is blank, which is not valid"
        ) String locationId
    ) {
        return varianceService.getVarianceNextLocation(branchId, countId, locationId);
    }

    @PostMapping("/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVarianceCount(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        @PathVariable @NotBlank(
            message = "Invalid Parameter: 'locationId' is blank, which is not valid"
        ) String locationId,
        @Valid @RequestBody UpdateCountDTO updateCountDTO
    ) {
        varianceService.stageVarianceCount(
            branchId,
            countId,
            locationId,
            updateCountDTO.getProductId(),
            updateCountDTO.getQuantity()
        );
    }

    @PostMapping("/locations/{locationId}/_commit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commitVarianceLocation(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        @PathVariable @NotBlank(
            message = "Invalid Parameter: 'locationId' is blank, which is not valid"
        ) String locationId
    ) {
        varianceService.commitVarianceCount(branchId, countId, locationId);
    }

    @GetMapping("/details")
    public @ResponseBody VarianceDetailsDTO getVarianceDetails(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId
    ) {
        return varianceService.getVarianceDetails(countId, branchId);
    }
}
