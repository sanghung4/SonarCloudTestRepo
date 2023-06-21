package com.reece.platform.products.controller;

import static com.reece.platform.products.search.SearchService.MAX_PAGES;
import static java.util.stream.Collectors.toList;

import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.service.ProductService;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.zalando.problem.Status;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

@RestController
@Validated
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductsController {

    private static final String FILTER_REGEXP = "^(.*)\\|(.*)$";
    private static final java.util.regex.Pattern FILTER_PATTERN = java.util.regex.Pattern.compile(FILTER_REGEXP);

    private final SearchService searchService;
    private final ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductSearchResponseDTO searchProducts(
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "1") @Min(1) @Max(MAX_PAGES) int page,
        @RequestParam(defaultValue = "12") @Min(0) int size,
        @RequestParam(defaultValue = "0") @Min(0) @Max(3) int categoryLevel,
        @RequestParam(required = false) List<@Pattern(regexp = FILTER_REGEXP) String> categories,
        @RequestParam(required = false) List<@Pattern(regexp = FILTER_REGEXP) String> filters,
        @RequestParam(required = false) UUID userId,
        @RequestParam(required = false) UUID shipToId,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String engine,
        @RequestParam(required = false) String customerNumber
    ) {
        val request = new ProductSearchRequestDTO(
            searchTerm,
            page,
            size,
            null,
            parseFilters(filters),
            parseFilters(categories),
            categoryLevel,
            shipToId,
            null,
            null,
            state,
            engine
        );
        var productSearchResponseDTO = searchService.getProductsByQuery(request, userId);
        if (customerNumber != null && !customerNumber.isEmpty()) {
            try {
                productSearchResponseDTO
                    .getProducts()
                    .forEach(p -> p.setCustomerPartNumbers(p.getCustomerPartNumbers(), customerNumber));
            } catch (Exception e) {}
        }
        return productSearchResponseDTO;
    }

    /**
     * Get Pricing and Availability for products
     * Optionally include list ids in response
     * @param requestDTO
     * @return
     */
    @GetMapping(value = "pricing", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductPricingResponseDTO getPricingAndAvailability(
        @Valid ProductPricingRequestDTO requestDTO
    ) {
        return productService.getPricing(requestDTO);
    }

    /**
     * Get Product Inventory
     * @param productId
     * @return
     */
    @GetMapping(value = "{productId}/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductInventoryResponseDTO getInventory(@PathVariable String productId) {
        return productService.getProductInventory(productId);
    }

    private List<ProductSearchFilterDTO> parseFilters(List<String> filters) {
        if (filters == null) {
            return Collections.emptyList();
        }

        return filters
            .stream()
            .map(FILTER_PATTERN::matcher)
            .map(m -> {
                if (m.matches()) {
                    return new ProductSearchFilterDTO(m.group(1), m.group(2));
                }

                return null;
            })
            .filter(Objects::nonNull)
            .collect(toList());
    }
}
