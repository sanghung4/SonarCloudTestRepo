package com.reece.platform.products.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdersResponseDTO {

    private List<GetOrderResponseDTO> orders;
    private PaginationResponseDTO pagination;
}
