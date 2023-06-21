package com.reece.platform.products.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.BranchServiceCoreException;
import com.reece.platform.products.exceptions.ElasticsearchException;
import com.reece.platform.products.exceptions.ProductByNumberNotFoundException;
import com.reece.platform.products.exceptions.ProductNotFoundException;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final AuthorizationService authorizationService;
    private final ProductService productService;
    private final SearchService searchService;

    /**
     * Entry point for retrieving an individual product by productId.
     *
     * Product data from Elasticsearch is transformed into the ProductDataDTO and returned as such.
     *
     * @param productId product to search on
     * @return ProductDataDTO object
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ProductDTO getProduct(@RequestParam String productId, @RequestParam(required = false) String customerNumber)
        throws JsonProcessingException, ElasticsearchException, ProductNotFoundException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductById(productId);

        if (productDTO == null) {
            throw new ProductNotFoundException(productId);
        }

        if (customerNumber != null && !customerNumber.isEmpty()) {
            try {
                productDTO.setCustomerPartNumbers(productDTO.getCustomerPartNumbers(), customerNumber);
            } catch (Exception e) {
                log.error("An error occurred while setting customerPart Number", e);
            }
        }

        return productDTO;
    }

    /**
     * Entry point for retrieving an individual product by product number (ERP specific).
     *
     * Product data from Elasticsearch is transformed into the ProductDataDTO and returned as such.
     *
     * @param prodNum product to search on
     * @return ProductDataDTO object
     */
    @PostMapping("/number/{prodNum}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductDTO getProductByNumber(@PathVariable String prodNum)
        throws JsonProcessingException, ElasticsearchException, ProductByNumberNotFoundException, BranchServiceCoreException {
        ProductDTO productDTO = productService.getProductByNumber(prodNum);

        if (productDTO == null) {
            throw new ProductByNumberNotFoundException(prodNum);
        }

        return productDTO;
    }

    /**
     * Entry point for retrieving multiple products from a search.
     *
     * Product data from Elasticsearch is transformed into the ProductDataDTO and returned as such.
     *
     * @param request Request containing product search info and erp user info
     * @return ResponseEntity<ProductSearchResponseDTO> Search results with pagination block and product results
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSearchResponseDTO searchProducts(
        @RequestBody ProductSearchRequestDTO request,
        @RequestHeader(name = "authorization", required = false) String authorization
    ) {
        val userId = authorizationService.getUserIdFromToken(authorization);
        return searchService.getProductsByQuery(request, userId);
    }

    /**
     * Entry point for getting auto-complete search suggestions when typing into the search bar
     *
     * Utilizes the App Search Query Suggestion API
     * https://swiftype.com/documentation/app-search/api/query-suggestion
     * @param request suggested search DTO containing term, size, and optionally erpUserInformation
     *                If erpUserInformation is null, results will not be limited to user's ERP system
     * @return SearchSuggestionResponseDTO
     */
    @PostMapping(value = "search-suggestions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody SearchSuggestionResponseDTO suggestedSearch(@RequestBody SearchSuggestionRequestDTO request) {
        val size = request.getSize() != null ? request.getSize() : 7;

        return searchService.getSuggestedSearch(request.getTerm(), request.getEngine(), size, request.getState());
    }

    /**
     * Retrieve categories for given erp
     *
     * @deprecated
     *   this ETL process has been replaced with a cached data call from elastic
     * @param erp erp to fetch categories for
     * @return category arch
     */
    @Deprecated(since = "0.94", forRemoval = true)
    @GetMapping("category")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CategoriesDTODeprecated getCategories(@RequestParam(required = false) String erp) {
        return productService.getCategories(erp);
    }

    /**
     * New endpoint that fetches categories from elastic
     * @return categories
     */
    @GetMapping("categories")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CategoriesDTO getProductCategories(@RequestParam String engine) {
        return searchService.getCategories(engine);
    }

    /**
     * Entry point for retrieving all products.
     *
     * Products data from Snowflakes is transformed into the List of ProductDataDTO and returned as such.
     *
     * @return List<ProductResponseDTO> object
     */
    @PostMapping("/all-products")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<ProductResponseDTO> getAllProducts() throws ProductNotFoundException {
        List<ProductResponseDTO> productDTOs = productService.getAllProducts();
        if (productDTOs == null) {
            throw new ProductNotFoundException();
        }

        return productDTOs;
    }
}
