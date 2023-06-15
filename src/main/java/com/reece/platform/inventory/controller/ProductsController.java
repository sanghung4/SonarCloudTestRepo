package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.dto.ProductSearchRequestDTO;
import com.reece.platform.inventory.dto.ProductSearchResultDTO;
import com.reece.platform.inventory.dto.kourier.ProductSearchResponseDTO;
import com.reece.platform.inventory.exception.InvalidProductSearchException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.util.StringUtil;
import com.reece.platform.inventory.util.Util;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {

    private final EclipseService eclipseService;

    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSearchResultDTO searchProducts(@RequestBody ProductSearchRequestDTO request) {
        return eclipseService.getProductSearch(request);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSearchResponseDTO getProductsBySearch(
        @RequestParam(value = "keywords") String keywords,
        @RequestParam(value = "displayName", required = false) String displayName,
        @RequestParam(required = false) String searchInputType
    ) throws InvalidProductSearchException {
        if (StringUtil.hasNullorEmptyValue(keywords)) {
            throw new InvalidProductSearchException();
        }
        if (Objects.nonNull(searchInputType) && !Util.isValidSearchInputType(searchInputType)) {
            throw new InvalidProductSearchException(Util.INVALID_SEARCH_INPUT_TYPE);
        }
        return eclipseService.getKourierProductSearch(keywords, displayName, searchInputType);
    }
}
