package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    void saveCustomer(Customer customers);
}
