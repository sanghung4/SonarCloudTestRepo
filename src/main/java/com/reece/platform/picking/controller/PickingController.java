package com.reece.platform.picking.controller;

import com.reece.platform.picking.dto.*;
import com.reece.platform.picking.dto.kourier.ShippingDetailsResponseDTO;
import com.reece.platform.picking.external.eclipse.EclipseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Controller
@RequiredArgsConstructor
@RequestMapping("/picking")
@Validated
public class PickingController {

    private final EclipseService eclipseService;

    /**
     * Get all active picking tasks for a branch via eclipse-core
     * @param branchId
     * @param userId
     * @return
     */
    @GetMapping(value = "tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<PickingTasksResponseDTO> getPickingTasks(
        @RequestParam @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid") String branchId,
        @RequestParam @NotBlank(message = "Invalid parameter: 'userId' is blank, which is not valid") String userId
    ) {
        var eclipseResponse = eclipseService.getPickingTasks(branchId, userId);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    /**
     * Assign task to User via eclipse-core
     * @param tasks
     * @return
     */
    @PutMapping(value = "tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<PickTasksListDTO> assignPickingTasks(@RequestBody PickTasksListDTO tasks) {
        var eclipseResponse = eclipseService.assignPickingTasks(tasks);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    /**
     * Get all user assigned pick tasks
     * @param branchId
     * @param userId
     * @return
     */
    @GetMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<WarehouseUserPicksDTO> getUserPicks(
        @RequestParam @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid") String branchId,
        @RequestParam @NotBlank(message = "Invalid parameter: 'userId' is blank, which is not valid") String userId,
        @RequestParam @NotBlank(message = "Invalid parameter: 'orderId' is blank, which is not valid") String orderId
    ) {
        var eclipseResponse = eclipseService.getUserPicks(branchId, userId, orderId);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PutMapping("user/pick/{pickId}")
    public @ResponseBody ResponseEntity<WarehousePickCompleteDTO> completeUserPick(
        @PathVariable @NotBlank(message = "Invalid parameter: 'pickId' is blank, which is not valid") String pickId,
        @Valid @RequestBody WarehousePickCompleteDTO userPick
    ) {
        var eclipseResponse = eclipseService.completeUserPick(pickId, userPick);
        return new ResponseEntity<>(eclipseResponse, HttpStatus.OK);
    }

    @PutMapping("close")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseCloseTaskDTO closePickTask(@Valid @RequestBody WarehouseCloseTaskRequestDTO request) {
        return eclipseService.closePickTask(request);
    }

    @PostMapping("/closeOrder")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CloseOrderResponseDTO closeOrder(@Valid @RequestBody CloseOrderRequestDTO request) {
        return eclipseService.closeOrder(request);
    }

    @GetMapping("pick/{warehouseId}/serialNumbers")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSerialNumbersResponseDTO getSerialNumber(
        @PathVariable @NotBlank(
            message = "Invalid parameter: 'warehouseId' is blank, which is not valid"
        ) String warehouseId
    ) {
        return eclipseService.getSerialNumber(warehouseId);
    }

    @PutMapping("pick/{warehouseId}/serialNumbers")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSerialNumbersResponseDTO updateSerialNumbers(
        @PathVariable @NotBlank(
            message = "Invalid parameter: 'warehouseId' is blank, which is not valid"
        ) String warehouseId,
        @Valid @RequestBody ProductSerialNumberRequestDTO serialNumbers
    ) {
        return eclipseService.updateSerialNumbers(warehouseId, serialNumbers);
    }

    @PutMapping(value = "tasks/stageLocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseToteTaskDTO stagePickTask(@Valid @RequestBody WarehouseToteTaskDTO toteTaskDTO) {
        return eclipseService.stagePickTask(toteTaskDTO);
    }

    @PutMapping(value = "tasks/stage/totePackages", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseTotePackagesDTO stagePickTotePackages(
        @Valid @RequestBody WarehouseTotePackagesDTO totePackagesDTO
    ) {
        return eclipseService.stagePickTotePackages(totePackagesDTO);
    }

    @GetMapping("/shipping")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ShippingDetailsResponseDTO getShippingDetails(
        @RequestParam @NotBlank(
            message = "Invalid parameter: 'invoiceNumber' is blank, which is not valid"
        ) String invoiceNumber
    ) {
        return eclipseService.getShippingDetails(invoiceNumber);
    }
}
