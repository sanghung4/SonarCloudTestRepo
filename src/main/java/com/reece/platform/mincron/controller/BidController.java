package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.model.BidDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bidList")
public class BidController {

    private BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping
    public @ResponseBody PageDTO<BidDTO> getBidList(
            @RequestParam String accountId,
            @RequestParam(defaultValue = "1") Integer startRow,
            @RequestParam(defaultValue = "10") Integer maxRows,
            @RequestParam(defaultValue = "") String searchFilter,
            @RequestParam(defaultValue = "") String fromDate,
            @RequestParam(defaultValue = "") String toDate,
            @RequestParam(defaultValue = "") String sortOrder,
            @RequestParam(defaultValue = "") String sortDirection
    ) throws Exception {
        PageDTO<BidDTO> bidPage = bidService.getBids(
                accountId,
                startRow,
                maxRows,
                searchFilter,
                fromDate,
                toDate,
                sortOrder,
                sortDirection
        );

        return bidPage;
    }
}
