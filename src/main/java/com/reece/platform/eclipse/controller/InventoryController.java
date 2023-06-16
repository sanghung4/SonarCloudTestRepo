package com.reece.platform.eclipse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reece.platform.eclipse.dto.FullCountDTO;
import com.reece.platform.eclipse.dto.UpdateCountDTO;
import com.reece.platform.eclipse.dto.inventory.CountInfoResponseDTO;
import com.reece.platform.eclipse.dto.inventory.EclipseBatchDTO;
import com.reece.platform.eclipse.dto.inventory.EclipseLocationItemDTO;
import com.reece.platform.eclipse.dto.inventory.EclipseNextLocationDTO;
import com.reece.platform.eclipse.dto.inventory.UpdateCountRequestDTO;
import com.reece.platform.eclipse.exceptions.KourierException;
import com.reece.platform.eclipse.service.KourierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {


    private final KourierService kourierService;

    @GetMapping("/counts")
    public @ResponseBody CountInfoResponseDTO getAvailableCounts(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String branchId
    ) {
        return kourierService.getAvailableCounts(startDate, endDate, branchId);
    }

    @GetMapping("/counts/{countId}")
    public @ResponseBody FullCountDTO getCount(@PathVariable String countId) {
        return kourierService.getCount(countId);
    }

    @GetMapping("/branches/{branchId}/batches/{batchId}")
    public @ResponseBody EclipseBatchDTO validateCount(@PathVariable String branchId, @PathVariable String batchId) {
        return kourierService.validateCount(branchId, batchId);
    }

    @GetMapping("/branches/{branchId}/batches/{batchId}/locations")
    public @ResponseBody List<String> getAllLocations(@PathVariable String branchId, @PathVariable String batchId) {
        return kourierService.getAllLocations(branchId, batchId);
    }

    @GetMapping("/batches/{batchId}/locations/{locationId}")
    public @ResponseBody List<EclipseLocationItemDTO> getLocation(
        @PathVariable String batchId,
        @PathVariable String locationId
    ) {
        return kourierService.getLocationItems(batchId, locationId);
    }

    @GetMapping("/batches/{batchId}/locations/{locationId}/_next")
    public @ResponseBody EclipseNextLocationDTO getNextLocation(
        @PathVariable String batchId,
        @PathVariable String locationId
    ) {
        return kourierService.getNextLocation(batchId, locationId);
    }

    @PostMapping("/batches/{batchId}/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCount(
        @PathVariable String batchId,
        @PathVariable String locationId,
        @RequestBody UpdateCountRequestDTO updateCountDTO
    ) {
        kourierService.updateCount(batchId, locationId, updateCountDTO);
    }

    @PostMapping("/batches/{batchId}/locations/{locationId}/_new")
    @ResponseStatus(HttpStatus.CREATED)
    public void  addToCount(
        @PathVariable String batchId,
        @PathVariable String locationId,
        @RequestBody UpdateCountDTO updateCountDTO
    ) throws KourierException {
        // Not built out yet. Missing in Kourier
        kourierService.addToCount(batchId, locationId, updateCountDTO);
    }
}
