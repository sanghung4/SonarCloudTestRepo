package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.SpecialPrice;
import org.springframework.stereotype.Service;

@Service
public interface SpecialPriceService {

    void saveSpecialPrice(SpecialPrice specialPrices);
}
