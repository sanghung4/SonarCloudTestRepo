package com.reece.platform.products.controller;

import com.reece.platform.products.exceptions.EclipseException;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.PriceAndAvailability;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.CustomerProductInfoService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("customer-product-info")
@Validated
@RequiredArgsConstructor
public class CustomerProductInfoController {

    private final CustomerProductInfoService customerProductInfoService;
    private final AuthorizationService authorizationService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PriceAndAvailability> getPricingAndAvailability(
        @RequestParam(defaultValue = "") @NotEmpty List<String> partNumbers,
        @RequestParam(defaultValue = "") @NotNull UUID shipToId,
        @RequestHeader String authorization,
        @RequestHeader("x-erp-user-id") String erpUserId,
        @RequestHeader("x-erp-password") String erpPassword,
        @RequestHeader("x-erp-account-id") String erpAccountId,
        @RequestHeader("x-erp-user-name") String erpUserName,
        @RequestHeader("X-erp-system-name") String erpSystemName
    ) throws ExecutionException, InterruptedException {
        val userId = authorizationService.getUserIdFromToken(authorization);
        val employee = authorizationService.userIsEmployee(authorization);

        val erpUserInformation = new ErpUserInformation(
            erpUserId,
            erpPassword,
            erpAccountId,
            erpUserName,
            erpSystemName,
                null
        );

        try {
            return customerProductInfoService.getPriceAndAvailability(
                partNumbers,
                erpUserInformation,
                employee,
                shipToId,
                userId
            );
        } catch (EclipseException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            throw e;
        }
    }
}
