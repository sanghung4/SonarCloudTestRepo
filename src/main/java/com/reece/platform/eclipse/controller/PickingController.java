package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.generated.*;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/picking")
public class PickingController {

    @Autowired
    private EclipseService eclipseService;

    @Autowired
    private KourierService kourierService;

    @Autowired
    public PickingController(EclipseService eclipseService) {
        this.eclipseService = eclipseService;
    }

    /**
     * Get All Picking Tasks for a branch
     * @param branchId
     * @param userId
     * @return
     * @throws EclipseTokenException
     */
    @GetMapping("tasks")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody PickingTasksResponseDTO getPickingTasks(
            @RequestParam String branchId,
            @RequestParam String userId
    ) throws EclipseTokenException {
        return eclipseService.getPickingTasks(branchId, userId, OrderMode.SALEORDER);
    }

    /**
     * Assign a picking task to a user
     * @param list
     * @return
     * @throws EclipseTokenException
     */
    @PutMapping("tasks")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehousePickTaskList assignPickingTasks(
            @RequestBody WarehousePickTaskList list
    ) throws EclipseTokenException {
        return eclipseService.assignPickingTasks(list);
    }

    /**
     * Get all products of a picking task for a user
     * @param branchId
     * @param userId
     * @return
     * @throws EclipseTokenException
     */
    @GetMapping("user")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseTaskUserPicksDTO getUserPicks(
            @RequestParam String branchId,
            @RequestParam String userId
    ) throws EclipseTokenException {
        return eclipseService.getUserPicks(branchId, userId);
    }

    /**
     * Get all Serial numbers for a pick task item with Eclipse API
     * @param warehouseId
     * @return
     * @throws EclipseTokenException
     * @throws InvalidSerializedProductException
     */
    @GetMapping("tasks/{warehouseId}/serialNumbers")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSerialNumbersResponseDTO getSerialNumbers(
            @PathVariable String warehouseId
    ) throws EclipseTokenException, InvalidSerializedProductException {
        return eclipseService.getSerialNumbers(warehouseId);
    }

    /**
     * Update or Create Serial Numbers for a pick item
     * @param warehouseId
     * @param serialNumbers
     * @return
     * @throws EclipseTokenException
     */
    @PutMapping("tasks/{warehouseId}/serialNumbers")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSerialNumbersResponseDTO updateSerialNumbers(
            @PathVariable String warehouseId,
            @RequestBody WarehouseSerialNumbers serialNumbers
    ) throws EclipseTokenException, InvalidSerializedProductException {
        return eclipseService.updateSerialNumbers(warehouseId, serialNumbers);
    }


    /**
     * Complete a user pick and assign it to a tote
     * @param pickId
     * @param userPick
     * @return
     * @throws EclipseTokenException
     * @throws PickNotFoundException
     * @throws InvalidToteException
     */
    @PutMapping("user/pick/{pickId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehousePickComplete completeUserPick(
            @PathVariable String pickId,
            @RequestBody WarehousePickComplete userPick
    ) throws EclipseTokenException, PickNotFoundException, InvalidToteException {
        return eclipseService.completeUserPick(pickId, userPick);
    }

    @PutMapping("tasks/close")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseCloseTask closeTask(
            @RequestBody WarehouseCloseTask request
    ) throws EclipseTokenException {
        return eclipseService.closeTask(request);
    }

    /**
     * Stages a tote and updates the staging location
     * @param request
     * @return
     * @throws EclipseTokenException
     */
    @PutMapping("user/tote")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseToteTask updateToteTask(
            @RequestBody WarehouseToteTask request
    ) throws EclipseTokenException, ToteUnavailableException, InvalidToteException {
        return eclipseService.updateToteTask(request);
    }

    @PutMapping("user/tote/package")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WarehouseTotePackages updateTotePackages(
            @RequestBody WarehouseTotePackages request
    ) throws EclipseTokenException, ToteLockedException, InvalidToteException, InvalidInvoiceException {
        return eclipseService.updateTotePackages(request);
    }


    @PostMapping("/splitQuantity")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody SplitQuantityResponseDTO splitQuantity(
            @RequestBody SplitQuantityRequestDTO request
    ) throws KourierException, SplitQuantityException, EclipseTokenException, InvalidSerializedProductException {
        return kourierService.splitQuantity(request);
    }

    @PostMapping("/closeOrder")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CloseOrderResponseDTO closeOrder(
            @RequestBody CloseOrderRequestDTO request
    ) throws KourierException, CloseOrderException {
        return kourierService.closeOrder(request);
    }

}
