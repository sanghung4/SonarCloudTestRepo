package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.Product;
import com.reece.specialpricing.postgres.ProductDataService;
import com.reece.specialpricing.snowflake.SNOWFLAKE_IMPORT_STATUS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDataService productDataService;

    public void saveProduct(Product products) {
        Optional<Product> product;
        var nameLength = Math.min((products.getDisplayName()== null ? "" : products.getDisplayName()).trim().length(), 500);
        Date currentDate = Date.from(Instant.now());
        try {
            products.setId(products.getId() == null ? "" : products.getId().trim());
            products.setDisplayName(products.getDisplayName() == null ? "" : products.getDisplayName().trim().substring(0, nameLength - 1));
            products.setStatus(SNOWFLAKE_IMPORT_STATUS.COMPLETE);
            products.setUpdatedAt(currentDate);
            product = productDataService.findById(products.getId());
            if(product.isEmpty() || product.get().getCreatedAt()==null || product.get().getCreatedAt().toString().startsWith("19"))
                products.setCreatedAt(currentDate);
            else
                products.setCreatedAt(product.get().getCreatedAt());

            productDataService.save(products);
        }
        catch (DataAccessException de){
            log.error("Product with id '"+products.getId()+"' couldn't save due to DataAccessException: "+de.getMessage());
        }
        catch (Exception e)
        {
            products.setStatus(SNOWFLAKE_IMPORT_STATUS.ERROR);
            products.setErrorDetails(e.getMessage());
            products.setUpdatedAt(currentDate);
            product = productDataService.findById(products.getId());
            if(product.isEmpty() || product.get().getCreatedAt()==null || product.get().getCreatedAt().toString().startsWith("19"))
                products.setCreatedAt(currentDate);
            else
                products.setCreatedAt(product.get().getCreatedAt());

            productDataService.save(products);
        }

    }
}
