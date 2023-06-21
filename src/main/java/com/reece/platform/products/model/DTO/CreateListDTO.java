package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateListDTO {

    private String name;
    private UUID billToAccountId;
    private List<ListLineItemDTO> listLineItems;
}
