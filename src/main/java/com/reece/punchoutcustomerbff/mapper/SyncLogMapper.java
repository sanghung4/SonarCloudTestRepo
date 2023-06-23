package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomersync.dto.SyncLogDto;

/**
 * Used for mapping between SyncLog DTOs and entities.
 * @author john.valentio
 */
public class SyncLogMapper {

  public static SyncLogDto toDTO(SyncLogDao input) {
    SyncLogDto output = SyncLogDto.builder()
        .id(input.getId())
        .startDatetime(DateUtil.fromDate(input.getStartDatetime()))
        .endDatetime(DateUtil.fromDate(input.getEndDatetime()))
        .status(input.getStatus())
        .build();
    return output;
  }

}
