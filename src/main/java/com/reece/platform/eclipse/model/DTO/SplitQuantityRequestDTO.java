package com.reece.platform.eclipse.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SplitQuantityRequestDTO {
    private CompletePickInputDTO product;
    private SerialListDTO serialNumbers;
    private Integer pickedItemsCount;
}
