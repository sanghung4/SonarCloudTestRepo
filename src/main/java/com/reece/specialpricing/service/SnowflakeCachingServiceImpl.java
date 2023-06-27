package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.*;
import com.reece.specialpricing.snowflake.SnowflakeCustomerDataService;
import com.reece.specialpricing.snowflake.SnowflakeProductDataService;
import com.reece.specialpricing.snowflake.SnowflakeSpecialPrice;
import com.reece.specialpricing.snowflake.SnowflakeSpecialPricingDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SnowflakeCachingServiceImpl implements SnowflakeCachingService {
    @Autowired
    private SnowflakeCustomerDataService snowflakeCustomerDataService;

    @Autowired
    private SnowflakeProductDataService snowflakeProductDataService;

    @Autowired
    private SnowflakeSpecialPricingDataService snowflakeSpecialPricingDataService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SpecialPriceService specialPriceService;

    public boolean refreshCustomerCache() {
        log.info("Started Refresh Customer Cache");
        snowflakeCustomerDataService.alterSessionformat();

        log.info("Fetching Customers from Snowflake");
        var snowflakeCustomers = snowflakeCustomerDataService.findAll();

        var customers = snowflakeCustomers.stream().map(c -> new Customer(c.getId(), c.getName())).collect(Collectors.toList());

        log.info("Saving {} customers to postgresql", customers.size());
        for (Customer customer : customers) {
            customerService.saveCustomer(customer);
        }
        log.info("Completed Refresh Customer Cache");

        return true;
    }

    public boolean refreshProductCache() {
        log.info("Started Refresh Product Cache");
        snowflakeProductDataService.alterSessionformat();

        log.info("Fetching Products from Snowflake");
        var snowflakeProducts = snowflakeProductDataService.findAll();

        var products = snowflakeProducts.stream().map(c -> new Product(c.getId(), c.getName())).collect(Collectors.toList());

        log.info("Saving {} products to postgresql", products.size());
        for (Product product : products) {
            productService.saveProduct(product);
        }
        log.info("Completed Refresh Product Cache");

        return true;
    }

    public boolean refreshSpecialPriceCache() {
        log.info("Started Refresh Special Price Cache");
        snowflakeSpecialPricingDataService.alterSessionformat();

        log.info("Fetching Special Prices from Snowflake");
        var snowflakeSpecialPrices = snowflakeSpecialPricingDataService.getAll();

        var specialPrices = snowflakeSpecialPrices.stream().map(SpecialPrice::new).collect(Collectors.toList());

        log.info("Saving {} prices to postgresql", specialPrices.size());
        for (SpecialPrice specialPrice : specialPrices) {
            specialPriceService.saveSpecialPrice(specialPrice);
        }
        log.info("Completed Refresh Special Price Cache");

        return true;
    }
}
