package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.ProductPriceResponseDTO;
import com.reece.platform.eclipse.exceptions.InvalidBranchException;
import com.reece.platform.eclipse.exceptions.InvalidProductException;
import com.reece.platform.eclipse.service.KourierService;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
@AllArgsConstructor
@Slf4j
public class PricingController {

    @Autowired
    private KourierService kourierService;

    @GetMapping("/productPrice")
    public @ResponseBody ProductPriceResponseDTO getProductPrice(
        @RequestParam String productId,
        @RequestParam(value = "branch") String branch,
        @RequestParam(value = "customerId", required = false) String customerId,
        @RequestParam(value = "userId", required = false) String userId,
        @RequestParam(value = "effectiveDate", required = false) @DateTimeFormat(
            pattern = "yyyy/mm/dd"
        ) Date effectiveDate,
        @RequestParam(value = "correlationId", required = false) String correlationId
    ) throws InvalidBranchException, InvalidProductException {
        if (productId == null || productId.isEmpty()) {
            throw new InvalidProductException();
        }
        if (branch == null || branch.isEmpty()) {
            throw new InvalidBranchException();
        }
        return kourierService.getProductPrice(productId, branch, customerId, userId, effectiveDate, correlationId);
    }
}
