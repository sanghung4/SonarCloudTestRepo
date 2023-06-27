package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.SpecialPrice;
import com.reece.specialpricing.postgres.SpecialPricingDataService;
import com.reece.specialpricing.snowflake.SNOWFLAKE_IMPORT_STATUS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialPriceServiceImpl implements SpecialPriceService{

    @Autowired
    private SpecialPricingDataService specialPricingDataService;

    public void saveSpecialPrice(SpecialPrice specialPrices) {

        List<SpecialPrice> specialPriceList;
        Date currentDate = Date.from(Instant.now());
        try {
            specialPrices.setStatus(SNOWFLAKE_IMPORT_STATUS.COMPLETE);
            specialPrices.setUpdatedAt(currentDate);
            specialPriceList = specialPricingDataService.findByCustomerIdAndProductId(specialPrices.getCustomerId(), specialPrices.getProductId());
            if (specialPriceList.isEmpty() || specialPriceList.get(0).getCreatedAt() == null || specialPriceList.get(0).getCreatedAt().toString().startsWith("19"))
                specialPrices.setCreatedAt(currentDate);
            else
                specialPrices.setCreatedAt(specialPriceList.get(0).getCreatedAt());

            specialPricingDataService.save(specialPrices);
        }
        catch (DataAccessException de){
            log.error("SpecialPrice with customerId '"+specialPrices.getCustomerId()+"' & productId '"+specialPrices.getProductId()+"' couldn't save due to DataAccessException: "+de.getMessage());
        }
        catch (Exception e) {
            specialPrices.setStatus(SNOWFLAKE_IMPORT_STATUS.ERROR);
            specialPrices.setErrorDetails(e.getMessage());
            specialPrices.setUpdatedAt(currentDate);
            specialPriceList = specialPricingDataService.findByCustomerIdAndProductId(specialPrices.getCustomerId(), specialPrices.getProductId());
            if (specialPriceList.isEmpty() || specialPriceList.get(0).getCreatedAt() == null || specialPriceList.get(0).getCreatedAt().toString().startsWith("19"))
                specialPrices.setCreatedAt(currentDate);
            else
                specialPrices.setCreatedAt(specialPriceList.get(0).getCreatedAt());

            specialPricingDataService.save(specialPrices);

        }

    }
}