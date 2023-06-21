package com.reece.platform.products.branches.controller;

import com.reece.platform.products.branches.exception.*;
import com.reece.platform.products.branches.model.DTO.*;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.service.AccountService;
import com.reece.platform.products.utilities.FeaturesUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/branches")
@AllArgsConstructor
public class BranchesController {

    private final BranchesService branchesService;
    private final AccountService accountService;

    @GetMapping
    public @ResponseBody ResponseEntity<GeolistResponseDTO> getBranches(BranchesRequestDTO request) {
        return ResponseEntity.ok(branchesService.getBranches(request));
    }

    @GetMapping("{branchId}")
    public @ResponseBody ResponseEntity<BranchResponseDTO> getBranch(@PathVariable String branchId)
        throws BranchNotFoundException {
        return ResponseEntity.ok(branchesService.getBranch(branchId));
    }

    @GetMapping("/getAll")
    public @ResponseBody ResponseEntity<List<BranchResponseDTO>> getBranchesByIds()
        throws BranchNotFoundException {
        return ResponseEntity.ok(branchesService.getAllBranches());
    }

    @GetMapping("refreshBranchesWorkday")
    public @ResponseBody ResponseEntity<List<WorkdayBranchDTO>> refreshWorkdayBranches() throws WorkdayException {
        if (!FeaturesUtil.isWorkdayEnabled(accountService.getFeatures())) {
            throw new WorkdayException("Workday branch feature is not enabled.");
        }
        return ResponseEntity.ok(branchesService.refreshWorkdayBranches());
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkdayException.class)
    public ResponseEntity<String> handleWorkdayException(WorkdayException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
