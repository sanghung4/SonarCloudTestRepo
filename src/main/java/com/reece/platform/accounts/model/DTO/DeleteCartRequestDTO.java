package com.reece.platform.accounts.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartRequestDTO {
    private UUID shipToAccountId;
}
