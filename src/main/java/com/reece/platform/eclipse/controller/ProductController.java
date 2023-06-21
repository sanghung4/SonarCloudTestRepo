package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.model.DTO.kourier.CustomersPriceDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductInventoryDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductInventoryRequestDTO;
import com.reece.platform.eclipse.model.DTO.kourier.ProductPricingAndAvailabilityRequestDTO;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final KourierService kourierService;

    @GetMapping("price")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    CustomersPriceDTO getPricingAndAvailability(@Valid ProductPricingAndAvailabilityRequestDTO request) throws UnsupportedEncodingException {
        return kourierService.getProductPrice(request);
    }

    @GetMapping("inventory")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ProductInventoryDTO getProductInventory(@Valid ProductInventoryRequestDTO request) throws UnsupportedEncodingException {
        return kourierService.getProductInventory(request);
    }

}
