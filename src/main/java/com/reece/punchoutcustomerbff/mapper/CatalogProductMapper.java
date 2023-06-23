package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.Persistence;

/**
 * Class used for mapping between catalog-product entities and DTOs.
 * @author john.valentino
 */
public class CatalogProductMapper {

  public static List<CatalogProductDto> toDTOs(Collection<CatalogProductDao> inputs) {
    if (inputs == null || !Persistence.getPersistenceUtil().isLoaded(inputs)) {
      return new ArrayList<>();
    }
    List<CatalogProductDto> outputs = new ArrayList<>();
    for (CatalogProductDao input : inputs) {
      outputs.add(CatalogProductMapper.toDTO(input));
    }
    return outputs;
  }

  public static CatalogProductDto toDTO(CatalogProductDao input) {
    CatalogProductDto output = CatalogProductDto.builder()
        .lastPullDatetime(DateUtil.fromDate(input.getLastPullDatetime()))
        .uom(input.getUom())
        .id(input.getId())
        .product(ProductMapper.toDTO(input.getProduct()))
        .sellPrice(input.getSellPrice())
        .skuQuantity(input.getSkuQuantity())
        .listPrice(input.getListPrice())
        .partNumber(input.getPartNumber())
        .build();
    return output;
  }

}
