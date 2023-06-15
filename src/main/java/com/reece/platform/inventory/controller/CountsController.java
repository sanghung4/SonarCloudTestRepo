package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.exception.CountNotFoundException;
import com.reece.platform.inventory.service.CountsService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Validated
public class CountsController {

    private final CountsService countsService;

    /**
     * Get Count - verify that count has been loaded into system. If not return not found
     * @param branchId
     * @param countId
     * @return
     */
    @GetMapping("/branches/{branchId}/counts/{countId}")
    public @ResponseBody CountDTO getCount(
        @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
        @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) {
        return countsService.getCount(branchId, countId).orElseThrow(CountNotFoundException::new);
    }

    /**
     * Return all locations for a count
     * @param branchId
     * @param countId
     * @return
     */
    @GetMapping("/branches/{branchId}/counts/{countId}/locations")
    public @ResponseBody LocationsDTO getLocations(
        @PathVariable @NotBlank(message = "Invalid Parameter: 'branchId' is blank, which is not valid") String branchId,
        @PathVariable @NotBlank(message = "Invalid Parameter: 'countId' is blank, which is not valid") String countId
    ) {
        return countsService.getLocations(branchId, countId);
    }

    /**
     * Return location details (all products in location)
     * @param branchId
     * @param countId
     * @param locationId
     * @return
     */
    @GetMapping("/locations/{locationId}")
    public @ResponseBody LocationDTO getLocation(
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
        return countsService.getLocation(branchId, countId, locationId);
    }

    /**
     * Get Next Location to count
     * @param branchId
     * @param countId
     * @param locationId
     * @return
     */
    @GetMapping("/locations/{locationId}/_next")
    public @ResponseBody NextLocationDTO getNextLocation(
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
        return countsService.getNextLocation(branchId, countId, locationId);
    }

    /**
     * Stage an updated count at location
     * @param branchId
     * @param countId
     * @param locationId
     * @param updateCountDTO
     */
    @PostMapping("/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCount(
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
        countsService.stageCount(
            branchId,
            countId,
            locationId,
            updateCountDTO.getProductId(),
            updateCountDTO.getQuantity()
        );
    }

    /**
     * Commit Count to ERP
     * @param branchId
     * @param countId
     * @param locationId
     */
    @PostMapping("/locations/{locationId}/_commit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commitLocation(
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
        countsService.commitCount(branchId, countId, locationId);
    }

    /**
     * Add Product at Location - Eclipse Only
     * @param branchId
     * @param countId
     * @param locationId
     * @param updateCountDTO
     * @return
     */
    @PostMapping("/locations/{locationId}/_add")
    public @ResponseBody LocationProductDTO addToCount(
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
        return countsService.addToCount(
            branchId,
            countId,
            locationId,
            updateCountDTO.getProductId(),
            updateCountDTO.getQuantity()
        );
    }
}
