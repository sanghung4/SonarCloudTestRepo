package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.ProductDto;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import java.util.ArrayList;
import javax.persistence.Persistence;

/**
 * Utility for mapping between Product entities and DTOs.
 * @author john.valentino
 */
public class ProductMapper {

  public static ProductDto toDTO(ProductDao input) {
    if (input == null || !Persistence.getPersistenceUtil().isLoaded(input)) {
      return null;
    }
    ProductDto output = ProductDto.builder()
        .id(input.getId())
        .categoryLevel2Name(input.getCategoryLevel2Name())
        .categoryLevel3Name(input.getCategoryLevel3Name())
        .deliveryInDays(input.getDeliveryInDays())
        .description(input.getDescription())
        .imageFullSize(input.getImageFullSize())
        .name(input.getName())
        .categoryLevel1Name(input.getCategoryLevel1Name())
        .manufacturer(input.getManufacturer())
        .manufacturerPartNumber(input.getManufacturerPartNumber())
        .maxSyncDatetime(DateUtil.fromDate(input.getMaxSyncDatetime()))
        .unspsc(input.getUnspsc())
        .imageThumb(input.getImageThumb())
        .partNumber(input.getPartNumber())
        .build();
    return output;
  }

}
