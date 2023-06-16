package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.dto.ProductSearchResultDTO;
import com.reece.platform.mincron.service.MincronService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Validated
public class ProductsController {

    private final MincronService mincronService;

    @RequestMapping("/search")
    public @ResponseBody ProductSearchResultDTO search(
        @RequestParam @NotBlank(message = "Invalid Parameter: 'branchNum' is blank, which is not valid") String branchNum,
        @RequestParam @NotBlank(message = "Invalid Parameter: 'query' is blank, which is not valid") String query,
        @RequestParam(defaultValue = "") String lastItem
    ) throws IOException {
        return mincronService.searchProducts(branchNum, query, lastItem);
    }
}
