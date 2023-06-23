package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.models.daos.AuditDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomersync.dto.AuditDto;

/**
 * Used for mapping between Audit DTOs and entities.
 * @author john.valentino
 */
public class AuditMapper {

  public static AuditDto toDTO(AuditDao input) {
    AuditDto output = AuditDto.builder()
        .id(input.getId())
        .fileName(input.getFileName())
        .s3DateTime(DateUtil.fromDate(input.getS3DateTime()))
        .ftpDateTime(DateUtil.fromDate(input.getFtpDateTime()))
        .s3Location(input.getS3Location())
        .ftpLocation(input.getFtpLocation())
        .uploadDatetime(DateUtil.fromDate(input.getUploadDatetime()))
        .customer(CustomerMapper.toDTO(input.getCustomer()))
        .errors(AuditErrorMapper.toDTOs(input.getErrors()))
        .build();
    return output;
  }

}
