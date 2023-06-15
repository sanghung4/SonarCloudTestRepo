package com.reece.platform.inventory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletedMultipleCountResponse {

    private List<DeletedCountDTO> deletedCounts;
}
