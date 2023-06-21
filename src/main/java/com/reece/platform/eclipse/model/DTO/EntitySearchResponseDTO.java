package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.EntitySearchResponse.EntitySearchResponseTypes;
import lombok.Data;

@Data
public class EntitySearchResponseDTO {
    private EntitySearchResponseTypes customers;
}

