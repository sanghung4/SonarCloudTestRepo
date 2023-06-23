package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.models.daos.AuditErrorDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomersync.dto.AuditErrorDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Persistence;

/**
 * Used for mapping to and from DTOs and entities for AuditError records.
 * @author john.valentino
 */
public class AuditErrorMapper {

  public static List<AuditErrorDto> toDTOs(Set<AuditErrorDao> inputs) {
    if (inputs == null || !Persistence.getPersistenceUtil().isLoaded(inputs)) {
      return new ArrayList<>();
    }
    List<AuditErrorDto> results = new ArrayList<>();
    for (AuditErrorDao input : inputs) {
      results.add(toDTO(input));
    }

    return results;
  }

  public static AuditErrorDto toDTO(AuditErrorDao input) {
    AuditErrorDto output = AuditErrorDto.builder()
        .errorDateTime(DateUtil.fromDate(input.getErrorDateTime()))
        .error(input.getError())
        .partNumber(input.getPartNumber())
        .build();
    return output;
  }

}
