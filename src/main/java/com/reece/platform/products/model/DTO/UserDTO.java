package com.reece.platform.products.model.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class UserDTO {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UUID approverId;
    private Boolean isEmployee;
    private List<UUID> ecommShipToIds;
}
