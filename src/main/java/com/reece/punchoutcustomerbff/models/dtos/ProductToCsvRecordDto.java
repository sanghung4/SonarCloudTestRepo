package com.reece.punchoutcustomerbff.models.dtos;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.dto.InputCsvRecord;
import lombok.*;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@Data
public class ProductToCsvRecordDto {

    public CatalogProductDto catalogProduct;
    public InputCsvRecord inputCsvRecord;
}
