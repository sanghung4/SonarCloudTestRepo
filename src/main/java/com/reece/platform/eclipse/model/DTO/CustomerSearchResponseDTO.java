package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CustomerSearchResponseDTO {
    private EclipseSearchMetadata metadata;
    private List<CustomerSearchResult> results;
}
