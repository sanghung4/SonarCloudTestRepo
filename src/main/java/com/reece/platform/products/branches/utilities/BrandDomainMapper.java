package com.reece.platform.products.branches.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BrandDomainMapper {

    @Value("${fortiline}")
    private String fortiline;

    @Value("${morrisonSupply}")
    private String morrisonSupply;

    @Value("${morscoHvacSupply}")
    private String morscoHvacSupply;

    @Value("${murraySupply}")
    private String murraySupply;

    @Value("${farnsworthWholesale}")
    private String farnsworthWholesale;

    @Value("${expressPipe}")
    private String expressPipe;

    @Value("${devoreAndJohnson}")
    private String devoreAndJohnson;

    @Value("${wholesaleSpecialities}")
    private String wholesaleSpecialities;

    @Value("${expressionsHomeGallery}")
    private String expressionsHomeGallery;

    @Value("${landBPipe}")
    private String landBPipe;

    @Value("${irvinePipe}")
    private String irvinePipe;

    @Value("${reece}")
    private String reece;

    public String getDomain(String brand) {
        switch (brand) {
            case "Fortiline Waterworks":
                return this.fortiline;
            case "Morrison Supply":
                return this.morrisonSupply;
            case "DeVore & Johnson":
                return this.devoreAndJohnson;
            case "Express Pipe & Supply":
                return this.expressPipe;
            case "Farnsworth Wholesale":
                return this.farnsworthWholesale;
            case "MORSCO HVAC Supply":
                return this.morscoHvacSupply;
            case "Murray Supply":
                return this.murraySupply;
            case "Wholesale Specialties":
                return this.wholesaleSpecialities;
            case "Expressions Home Gallery":
                return this.expressionsHomeGallery;
            case "L&B Pipe":
                return this.landBPipe;
            case "Irvine Pipe & Supply":
                return this.irvinePipe;
            default:
                return this.reece;
        }
    }
}
