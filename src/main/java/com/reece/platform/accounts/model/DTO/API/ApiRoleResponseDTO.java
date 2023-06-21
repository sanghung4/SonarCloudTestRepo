package com.reece.platform.accounts.model.DTO.API;

import com.reece.platform.accounts.model.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class ApiRoleResponseDTO {
    private UUID id;
    private String name;
    private String description;

    public ApiRoleResponseDTO() {}

    public ApiRoleResponseDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
    }
}
