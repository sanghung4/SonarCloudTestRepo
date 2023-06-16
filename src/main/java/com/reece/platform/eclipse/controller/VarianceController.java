package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.dto.VarianceDetailsDTO;
import com.reece.platform.eclipse.dto.VarianceSummaryDTO;
import com.reece.platform.eclipse.service.KourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/variance")
@Slf4j
public class VarianceController {

    private final KourierService kourierService;

    @GetMapping("/summary")
    public @ResponseBody VarianceSummaryDTO getVarianceSummary(@RequestParam String countId) {
        return kourierService.getVarianceSummary(countId);
    }

    @GetMapping("/details")
    public @ResponseBody VarianceDetailsDTO getVarianceDetails(@RequestParam String countId) {
        return kourierService.getVarianceDetails(countId);
    }
}
