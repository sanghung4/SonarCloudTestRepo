package com.reece.punchoutcustomersync.dto;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response of calling the audit endpoint, which is all for debug purposes.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuditOutputDto {

  private boolean success = true;
  private SyncLogDto sync;
  private AuditDto audit;
  private List<OutputCsvRecord> outputCsvRecords;
  private List<CatalogProductDto> mappings;

}
