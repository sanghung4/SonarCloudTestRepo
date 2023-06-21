package com.reece.platform.products.model.DTO;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteListResponseDTO {

    private UUID id;
    private Boolean success;
}
