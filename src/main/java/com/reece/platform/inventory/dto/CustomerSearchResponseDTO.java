package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.CustomerSearchResult;
import com.reece.platform.inventory.model.EclipseSearchMetadata;
import lombok.Data;

import java.util.List;

@Data
public class CustomerSearchResponseDTO {
    private EclipseSearchMetadata metadata;
    private List<CustomerSearchResult> results;
}

