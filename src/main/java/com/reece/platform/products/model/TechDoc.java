package com.reece.platform.products.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Data model for product technical documents
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechDoc implements Serializable {

    private String name;
    private String url;
}
