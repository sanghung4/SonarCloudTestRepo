package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToggleListItemResponseDTO {

    private List<ListDTO> lists;
}
