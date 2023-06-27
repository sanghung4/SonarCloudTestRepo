package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.model.pojo.ErrorDetails;
import com.reece.specialpricing.model.pojo.ErrorResponse;
import com.reece.specialpricing.model.pojo.SearchResult;
import com.reece.specialpricing.model.pojo.TypeaheadSearchRequest;
import com.reece.specialpricing.service.SnowflakeCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/snowflakeCache")
public class SnowflakeCachingController {
    @Autowired
    private SnowflakeCachingService snowflakeCachingService;

    @RequestMapping("/refreshCustomers")
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Boolean> refreshCustomers(){
        return ResponseEntity.ok(snowflakeCachingService.refreshCustomerCache());
    }

    @RequestMapping("/refreshProducts")
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Boolean> refreshProducts(){
        return ResponseEntity.ok(snowflakeCachingService.refreshProductCache());
    }

    @RequestMapping("/refreshSpecialPrices")
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Boolean> refreshSpecialPrices(){
        return ResponseEntity.ok(snowflakeCachingService.refreshSpecialPriceCache());
    }
}
