package com.reece.punchoutcustomersync.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Dto representation of the sync log
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SyncLogDto {
  private UUID id;
  private String startDatetime;
  private String endDatetime;
  private String status;
}
