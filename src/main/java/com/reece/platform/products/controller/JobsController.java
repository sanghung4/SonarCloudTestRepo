package com.reece.platform.products.controller;

import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.EmptyProductIndexJobException;
import com.reece.platform.products.exceptions.ProductNotFoundException;
import com.reece.platform.products.search.CreateIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobsController {

    private final BranchesService branchesService;
    private final CreateIndexService createIndexService;

    @PostMapping("search-engine/{engineName}/_full")
    @ResponseStatus(HttpStatus.CREATED)
    public void buildIndex(@PathVariable String engineName) throws EmptyProductIndexJobException {
        createIndexService.buildAndPopulateNewEngine(engineName);
    }

    @PutMapping("search-engine/{engineName}/_update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIndex(@PathVariable String engineName) {
        createIndexService.updateCurrentEngine(engineName);
    }

    @PostMapping("branches/_sync")
    @ResponseStatus(HttpStatus.OK)
    public void syncBranchData() {
        branchesService.syncWithSnowflake();
    }
}
