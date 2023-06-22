package com.reece.punchoutcustomerbff.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@Data
public class AuditRqDto {
	private String name;
	private String status;
	private String s3Location;
	private String fileCheckSum;
	private String errors;
}
