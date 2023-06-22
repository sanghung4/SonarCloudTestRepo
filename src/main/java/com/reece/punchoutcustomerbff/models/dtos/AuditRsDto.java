package com.reece.punchoutcustomerbff.models.dtos;

import com.reece.punchoutcustomerbff.models.models.Audit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Information with Audit information requested.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuditRsDto {
  private int pagination;
  private List<Audit> audits;

  // this could be better with Enums
  private String orderBy;

  // this could be better with Enums
  private String filterBy;

}
