package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListUploadResponseDTO {

    private List<ListUploadErrorDTO> errors;
    private long successfulRowCount;
    private UUID listId;

    public ListUploadResponseDTO(List<ListUploadErrorDTO> errors, long successfulRowCount, UUID listId) {
        this.errors = errors;
        this.successfulRowCount = successfulRowCount;
        this.listId = listId;
    }
}
