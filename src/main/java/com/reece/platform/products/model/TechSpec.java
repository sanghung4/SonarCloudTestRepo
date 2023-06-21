package com.reece.platform.products.model;

import java.io.Serializable;
import lombok.Data;

/**
 * Data model for product tech specifications
 */
@Data
public class TechSpec implements Serializable {

    private String name;
    private String value;
}
