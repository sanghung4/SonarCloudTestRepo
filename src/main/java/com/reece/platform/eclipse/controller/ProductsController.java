package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.ProductDetailsDTO;
import com.reece.platform.eclipse.dto.productsearch.kourier.ProductKourierSearchResponseDTO;
import com.reece.platform.eclipse.dto.productsearch.request.ProductSearchRequestDTO;
import com.reece.platform.eclipse.dto.productsearch.response.ProductSearchResponseDTO;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.exceptions.InvalidProductSearchException;
import com.reece.platform.eclipse.exceptions.ProductImageUrlNotFoundException;
import com.reece.platform.eclipse.service.EclipseService;
import com.reece.platform.eclipse.service.KourierService;
import com.reece.platform.eclipse.util.StringUtil;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductsController {

    private final EclipseService eclipseService;
    private final KourierService kourierService;

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductDetailsDTO getProduct(@PathVariable String productId) throws EclipseTokenException {
        return eclipseService.getProduct(productId);
    }

    @GetMapping("/{productId}/imageUrl")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getProductImageUrl(@PathVariable String productId)
        throws EclipseTokenException, ProductImageUrlNotFoundException {
        return eclipseService.getProductImageUrl(productId);
    }

    //TODO - Below Api to be removed
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductSearchResponseDTO getProductsBySearch(@RequestBody ProductSearchRequestDTO request)
        throws EclipseTokenException {
        return eclipseService.getProductSearch(request);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ProductKourierSearchResponseDTO getProductsBySearch(
        @RequestParam String keywords,
        @RequestParam(value = "displayName", required = false) String displayName
    ) throws UnsupportedEncodingException, InvalidProductSearchException {
        if (StringUtil.hasNullorEmptyValue(keywords)) {
            throw new InvalidProductSearchException();
        }
        return kourierService.getProductSearch(keywords, displayName);
    }
}
