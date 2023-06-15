package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.CustomerSearchInputDTO;
import com.reece.platform.inventory.dto.CustomerSearchResponseDTO;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final EclipseService eclipseService;

    @PostMapping(value = "/_search", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CustomerSearchResponseDTO getCustomerSearch(@RequestBody CustomerSearchInputDTO input) {
        return eclipseService.getCustomerSearch(input);
    }

}
