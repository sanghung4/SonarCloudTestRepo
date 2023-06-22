package com.reece.punchoutcustomerbff.models.dtos;

import com.reece.punchoutcustomerbff.models.models.FullCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Response with Catalogs requested.
 */
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class CatalogRsDto {
	private FullCatalog fullCatalog;
}
