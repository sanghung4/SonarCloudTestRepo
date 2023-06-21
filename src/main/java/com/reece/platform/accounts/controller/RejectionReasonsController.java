package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rejection-reasons")
public class RejectionReasonsController {

    /**
     * Get the rejection reasons
     * @return a list of rejection reasons
     */
    @GetMapping
    public @ResponseBody List<RejectionReasonEnum> getRejectionReasons() {
        return List.of(RejectionReasonEnum.values());
    }
}
