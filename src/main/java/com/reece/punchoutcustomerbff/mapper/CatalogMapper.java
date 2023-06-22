package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.util.DateUtil;

/**
 * Utility class used for mapping to and from catalog entities and DTOs.
 * @author john.valentino
 */
public class CatalogMapper {

  public static CatalogDto toDTO(CatalogDao input) {
    CatalogDto output = CatalogDto.builder()
        .name(input.getName())
        .id(input.getId())
        .dateArchived(DateUtil.fromDate(input.getDateArchived()))
        .status(input.getStatus())
        .fileName(input.getFileName())
        .procSystem(input.getProcSystem())
        .lastUpdate(DateUtil.fromDate(input.getLastUpdate()))
        .mappings(CatalogProductMapper.toDTOs(input.getMappings()))
        .build();
    return output;
  }

}
