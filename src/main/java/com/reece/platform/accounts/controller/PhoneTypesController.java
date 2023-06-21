package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/phone-types")
public class PhoneTypesController {

    @GetMapping()
    public @ResponseBody
    List<PhoneTypeEnum> getPhoneTypes() {
        return Arrays.asList(PhoneTypeEnum.values());
    }
}
