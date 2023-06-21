package com.reece.platform.products.model.DTO;

import java.util.UUID;
import lombok.Data;

@Data
public class FeatureDTO {

    private UUID id;
    private String name;
    private Boolean isEnabled;
}
