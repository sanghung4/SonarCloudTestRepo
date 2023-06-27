package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.Customer;
import com.reece.specialpricing.postgres.CustomerDataService;

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
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDataService customerDataService;

    public void saveCustomer(Customer customers) {
        Optional<Customer> customer;
        Date currentDate = Date.from(Instant.now());
        try {
            customers.setId(customers.getId() == null ? "" : customers.getId().trim());
            customers.setDisplayName(customers.getDisplayName() == null ? "" : customers.getDisplayName().trim());
            customers.setStatus(SNOWFLAKE_IMPORT_STATUS.COMPLETE);
            customers.setUpdatedAt(currentDate);
            customer = customerDataService.findById(customers.getId());
            if(customer.isEmpty() || customer.get().getCreatedAt()==null || customer.get().getCreatedAt().toString().startsWith("19"))
                customers.setCreatedAt(currentDate);
            else
                customers.setCreatedAt(customer.get().getCreatedAt());

            customerDataService.save(customers);
        }
        catch (DataAccessException de){
            log.error("Customer with id '"+customers.getId()+"' couldn't save due to DataAccessException: "+de.getMessage());
        }
        catch (Exception e)
        {
            customers.setStatus(SNOWFLAKE_IMPORT_STATUS.ERROR);
            customers.setErrorDetails(e.getMessage());
            customers.setUpdatedAt(currentDate);
            customer = customerDataService.findById(customers.getId());
            if(customer.isEmpty() || customer.get().getCreatedAt()==null || customer.get().getCreatedAt().toString().startsWith("19"))
                customers.setCreatedAt(currentDate);
            else
                customers.setCreatedAt(customer.get().getCreatedAt());

            customerDataService.save(customers);
        }

    }
}
