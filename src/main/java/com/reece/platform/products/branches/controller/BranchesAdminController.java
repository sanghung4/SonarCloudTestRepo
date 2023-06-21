package com.reece.platform.products.branches.controller;

import com.reece.platform.products.branches.exception.BranchNotFoundException;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.model.DTO.UpdateBranchDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.service.AuthorizationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class BranchesAdminController {

    private final BranchesService branchesService;
    private final AuthorizationService authorizationService;

    /**
     * Get All Branches for admin screen
     * @param authorization
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<BranchResponseDTO>> getAllBranches(
        @RequestHeader(name = "authorization") String authorization
    ) {
        if (!authorizationService.userCanManageBranches(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(branchesService.getAllBranches());
    }

    /**
     * Update Branch information from admins
     * @param authorization
     * @param branchId
     * @param updateBranchDTO
     * @return
     */
    @PutMapping("{branchId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<BranchResponseDTO> updateBranch(
        @RequestHeader(name = "authorization") String authorization,
        @PathVariable UUID branchId,
        @RequestBody UpdateBranchDTO updateBranchDTO
    ) throws BranchNotFoundException {
        if (!authorizationService.userCanManageBranches(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(branchesService.updateBranch(branchId, updateBranchDTO));
    }

    @ExceptionHandler({ BranchNotFoundException.class })
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
