package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.model.pojo.*;
import com.reece.specialpricing.postgres.SpecialPrice;
import com.reece.specialpricing.service.EclipseService;
import com.reece.specialpricing.service.SpecialPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/product/specialPrice")
public class SpecialPriceController {

    @Autowired
    private SpecialPricingService specialPricingService;

    @Autowired
    private EclipseService eclipseService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity suggestChanges(
        @Valid @RequestBody SpecialPriceChangeRequest changeRequest
    ) throws CSVUploadException {
        var errors = changeRequest.validate().stream()
                .map(error -> new ErrorDetails(error.getErrorField(), error.getMessage(), error.getExceptionType()))
                .collect(Collectors.toList());
        if(!errors.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorResponse(errors.size(), errors));
        }

        changeRequest.cleanUserInputData();

        var result = this.specialPricingService.createAndUpdatePrices(changeRequest);

        return ResponseEntity.ok(new SpecialPriceChangeResponse(result));
    }

    @ExceptionHandler(CSVUploadException.class)
    public ResponseEntity<ErrorResponse> handleEclipseTokenException(CSVUploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(new ErrorDetails(e.getErrorField(), e.getMessage(), e.getExceptionType())));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity getSpecialPrices(
            @Valid PaginationContext paginationContext,
            @Valid SpecialPriceSearchRequest searchRequest
            ){
        var errors = paginationContext.validate(true);
        errors.addAll(searchRequest.validate());
        var responseErrors = errors.stream().map(error -> new ErrorDetails(error.getErrorField(), error.getMessage(), error.getExceptionType()))
            .collect(Collectors.toList());
        if(!responseErrors.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorResponse(responseErrors.size(), responseErrors));
        }

        searchRequest.cleanUserInputData();

        var result = this.specialPricingService.getPrices(searchRequest.getCustomerId(), searchRequest.getProductId(), searchRequest.getPriceLine(), paginationContext);

        var metadata = new HashMap<String, Object>();
        metadata.put("page", paginationContext.getPage());
        metadata.put("nextPage", result.getNextPage());
        metadata.put("pageCount", result.getPageCount());
        metadata.put("pageSize", paginationContext.getPageSize());
        metadata.put("resultCount", result.getResultCount());
        metadata.put("orderBy", paginationContext.getOrderBy());
        metadata.put("orderDirection", paginationContext.getOrderDirection());
        metadata.put("customerId", searchRequest.getCustomerId());
        metadata.put("productId", searchRequest.getProductId());
        metadata.put("priceLine", searchRequest.getPriceLine());

        return ResponseEntity.ok(
                new SearchResult(
                        metadata,
                        result.getResults()
                                .stream()
                                .map(p -> new StructuredSpecialPrice((SpecialPrice)p))
                                .collect(Collectors.toList())
                )
        );
    }

    @GetMapping(value = "priceLines", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity getPriceLines(
            @Valid SpecialPriceSearchRequest searchRequest
    ) {
        var errors = searchRequest.validate();
        var responseErrors = errors.stream().map(error -> new ErrorDetails(error.getErrorField(), error.getMessage(), error.getExceptionType()))
                .collect(Collectors.toList());
        if(!responseErrors.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorResponse(responseErrors.size(), responseErrors));
        }

        searchRequest.cleanUserInputData();

        var result = this.specialPricingService.getPriceLines(searchRequest.getCustomerId(), searchRequest.getProductId());

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "productPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ProductPriceResponse getProductPrice(
            @RequestParam String productId,
            @RequestParam(value = "branch",required=false) String branch,
            @RequestParam(value = "customerId",required=false) String customerId,
            @RequestParam(value = "userId",required =false) String userId,
            @RequestParam(value = "effectiveDate",required=false) @DateTimeFormat(pattern="yyyy/mm/dd") Date effectiveDate,
            @RequestParam(value = "correlationId",required=false) String correlationId
    ) throws Exception {
        try {
            var response = this.eclipseService.getProductPrice(productId, branch,customerId,userId,effectiveDate,correlationId);
            return response;
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
