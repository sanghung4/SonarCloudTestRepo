package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model for product information on front end display
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductResponseDTO implements Serializable {

    private String id;
    private String name;
}
