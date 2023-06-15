package com.reece.platform.inventory.dto;

import com.reece.platform.inventory.model.WriteIn;
import java.util.Date;
import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Value;
import lombok.val;

@Value
public class WriteInDTO {

    UUID id;

    @NotBlank(message = "Invalid Parameter: 'locationId' is blank, which is not valid")
    String locationId;

    String catalogNum;
    String upcNum;

    @NotBlank(message = "Invalid Parameter: 'description' is blank, which is not valid")
    String description;

    @NotBlank(message = "Invalid Parameter: 'uom' is blank, which is not valid")
    String uom;

    @Min(value = 1, message = "Invalid parameter: 'quantity' is less than 1, which is not valid")
    Integer quantity;

    String comment;
    boolean resolved;
    String createdBy;
    Date createdAt;
    String updatedBy;
    Date updatedAt;

    public static WriteInDTO fromEntity(WriteIn entity) {
        return new WriteInDTO(
            entity.getId(),
            entity.getLocationName(),
            entity.getCatalogNum(),
            entity.getUpcNum(),
            entity.getDescription(),
            entity.getUom(),
            entity.getQuantity(),
            entity.getComment(),
            entity.isResolved(),
            entity.getCreatedBy(),
            entity.getCreatedAt(),
            entity.getUpdatedBy(),
            entity.getUpdatedAt()
        );
    }

    public WriteIn toEntity() {
        val entity = new WriteIn();
        entity.setCatalogNum(getCatalogNum());
        entity.setUpcNum(getUpcNum());
        entity.setDescription(getDescription());
        entity.setUom(getUom());
        entity.setQuantity(getQuantity());
        entity.setComment(getComment());
        return entity;
    }

    public WriteIn updateEntity(WriteIn entity) {
        entity.setCatalogNum(getCatalogNum());
        entity.setUpcNum(getUpcNum());
        entity.setDescription(getDescription());
        entity.setUom(getUom());
        entity.setQuantity(getQuantity());
        entity.setComment(getComment());
        entity.setResolved(isResolved());
        return entity;
    }
}
