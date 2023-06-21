package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ToggleListLineItemDTO {

    private ListLineItemDTO listLineItemDTO;
    private List<UUID> listIds;
    private UUID billToAccountId;
}
