package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.ErrorDTO;
import com.reece.platform.mincron.model.ProductSearchResultDTO;
import com.reece.platform.mincron.service.MincronService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductsController {
    private final MincronService mincronService;

    @RequestMapping("/search")
    public @ResponseBody
    ProductSearchResultDTO search(@RequestParam String branchNum, @RequestParam String query, @RequestParam(defaultValue = "") String lastItem) {
        return mincronService.searchProducts(branchNum, query, lastItem);
    }

}
