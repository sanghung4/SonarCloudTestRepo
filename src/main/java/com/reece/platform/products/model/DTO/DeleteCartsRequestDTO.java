package com.reece.platform.products.model.DTO;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartsRequestDTO {

    private UUID shipToAccountId;
}
