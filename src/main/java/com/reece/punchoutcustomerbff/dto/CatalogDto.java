package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a customer's catalog
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CatalogDto {

  private UUID id;
  private String status;
  private String fileName;
  private String name;
  private String lastUpdate;
  private String dateArchived;
  private String procSystem;
  private List<CatalogProductDto> mappings = new ArrayList<>();

}
