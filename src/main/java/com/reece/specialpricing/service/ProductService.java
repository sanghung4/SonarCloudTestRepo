package com.reece.specialpricing.service;

import com.reece.specialpricing.postgres.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    void saveProduct(Product products);
}
