package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.WriteInDTO;
import com.reece.platform.inventory.service.WriteInsService;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/write-ins")
@RequiredArgsConstructor
@Validated
public class WriteInsController {

    private final WriteInsService writeInsService;

    @GetMapping
    public Page<WriteInDTO> index(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        Pageable pageRequest
    ) {
        return writeInsService.findAllWriteIns(branchId, countId, pageRequest);
    }

    @GetMapping("{writeInId}")
    public WriteInDTO show(
        @PathVariable @NotBlank(message = "Invalid Parameter: 'writeInId' is blank, which is not valid") UUID writeInId
    ) {
        return writeInsService.findById(writeInId);
    }

    @PostMapping
    public WriteInDTO create(
        @RequestHeader("X-Branch-Id") @NotBlank(
            message = "Invalid Header: 'branchId' is blank, which is not valid"
        ) String branchId,
        @RequestHeader("X-Count-Id") @NotBlank(
            message = "Invalid Header: 'countId' is blank, which is not valid"
        ) String countId,
        @Valid @RequestBody WriteInDTO writeInDTO
    ) {
        return writeInsService.createWriteIn(branchId, countId, writeInDTO);
    }

    @PutMapping("{writeInId}")
    public WriteInDTO update(@PathVariable UUID writeInId, @RequestBody WriteInDTO writeInDTO) {
        return writeInsService.updateWriteIn(writeInId, writeInDTO);
    }

    @PatchMapping("{writeInId}/_resolve")
    public WriteInDTO markResolved(@PathVariable UUID writeInId) {
        return writeInsService.resolveWriteIn(writeInId);
    }
}
