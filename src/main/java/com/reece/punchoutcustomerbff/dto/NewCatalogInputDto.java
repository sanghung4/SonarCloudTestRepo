package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating a new catalog.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewCatalogInputDto {

    private String procurementSystem;
    private String name;
    private List<String> uploadIds;
}
