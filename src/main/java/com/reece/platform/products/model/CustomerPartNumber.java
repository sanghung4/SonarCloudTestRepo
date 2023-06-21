package com.reece.platform.products.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Data model for product customer part number mapping
 */

@Data
public class CustomerPartNumber implements Serializable {

    private String customer;
    private List<String> partNumbers;
}
