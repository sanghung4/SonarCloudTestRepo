package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.*;
import com.reece.platform.mincron.service.MincronService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final MincronService mincronService;

    @GetMapping("/counts")
    public MincronCountsDTO getAllCounts() {
        return mincronService.getAllCounts();
    }

    @GetMapping("/branches/{branchId}/counts/{countId}")
    public CountDTO getCount(@PathVariable String branchId, @PathVariable String countId) {
        val count = mincronService.validateCount(branchId, countId);
        return count;
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations")
    public List<LocationCodeDTO> getLocations(@PathVariable String branchId, @PathVariable String countId) {
        return mincronService.getLocations(branchId, countId);
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}")
    public LocationDTO getLocation(@PathVariable String branchId, @PathVariable String countId, @PathVariable String locationId) {
        return mincronService.getItemsAtLocation(branchId, countId, locationId);
    }

    @GetMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}/_next")
    public NextLocationDTO getNextLocation(@PathVariable String branchId, @PathVariable String countId, @PathVariable String locationId) {
        return mincronService.getNextLocation(branchId, countId, locationId).orElseThrow(() -> new MincronException("No more locations", HttpStatus.NOT_FOUND, "NEXT_LOCATION_NOT_FOUND"));
    }

    @PutMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}/items/{tagNum}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLocationItem(@PathVariable String branchId, @PathVariable String countId, @PathVariable String locationId, @PathVariable String tagNum, @RequestBody UpdateCountDTO updateCountDTO) {
        mincronService.updateCount(branchId, countId, locationId, tagNum, updateCountDTO.getCountedQty());
    }

    @PostMapping("/branches/{branchId}/counts/{countId}/locations/{locationId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    void createLocationItem(@PathVariable String branchId, @PathVariable String countId, @PathVariable String locationId, @RequestBody AddToCountDTO addToCountDTO) {
        mincronService.addToCount(branchId, countId, locationId, addToCountDTO.getProdNum(), addToCountDTO.getCountedQty());
    }

}
