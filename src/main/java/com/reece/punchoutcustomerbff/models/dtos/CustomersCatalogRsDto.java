package com.reece.punchoutcustomerbff.models.dtos;

import com.reece.punchoutcustomerbff.models.models.CustomerCatalogInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersCatalogRsDto {
	private List<CustomerCatalogInfo> customers;
}
