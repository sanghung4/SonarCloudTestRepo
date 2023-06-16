package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.dto.CountDTO;
import com.reece.platform.mincron.dto.LocationDTO;
import com.reece.platform.mincron.dto.MincronCountsDTO;
import com.reece.platform.mincron.dto.NextLocationDTO;
import com.reece.platform.mincron.dto.kerridge.MincronUpdateCountRequestDTO;
import com.reece.platform.mincron.dto.variance.VarianceDetailsResponseDTO;
import com.reece.platform.mincron.dto.variance.VarianceSummaryDTO;
import com.reece.platform.mincron.service.MincronService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Validated
public class InventoryController {

    private final MincronService mincronService;

    @GetMapping("/counts")
    public MincronCountsDTO getAllCounts() throws IOException {
        return mincronService.getAllCounts();
    }

    @GetMapping("/branches/{branchId}/counts/{countId}")
    public CountDTO getCount(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) throws IOException {
        return mincronService.validateCount(branchId, countId);
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations")
    public List<String> getLocations(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) throws IOException {
        return mincronService.getLocations(branchId, countId);
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}")
    public LocationDTO getLocation(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'locationId' is blank, which is not valid") String locationId
    ) throws IOException {
        return mincronService.getLocation(branchId, countId, locationId);
    }

    @PutMapping("/branches/{branchId}/counts/{countId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCount(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId,
            @Valid @RequestBody MincronUpdateCountRequestDTO mincronUpdateCountRequestDTO
    ) throws IOException {
        mincronService.updateCount(branchId, countId, mincronUpdateCountRequestDTO);
    }


    @GetMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}/_next")
    public NextLocationDTO getNextLocation(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'locationId' is blank, which is not valid") String locationId
    ) throws IOException {
        return mincronService.getNextLocation(branchId, countId, locationId);
    }

    @GetMapping("/varianceSummary/{branchId}/{countId}")
    public VarianceSummaryDTO getVarianceSummary(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) throws IOException {
        return mincronService.fetchVarianceSummary(branchId, countId);
    }

    @GetMapping("/varianceDetails/{branchId}/{countId}")
    public VarianceDetailsResponseDTO getVarianceDetails(
            @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
            @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) throws IOException {
        return mincronService.fetchVarianceDetails(branchId, countId);
    }
}
