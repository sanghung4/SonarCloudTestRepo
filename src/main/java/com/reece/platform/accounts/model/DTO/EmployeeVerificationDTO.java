package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.utilities.ErpUtility;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeVerificationDTO {

    public EmployeeVerificationDTO(CreateUserDTO createUserDTO, UUID token) {
        this.email = createUserDTO.getEmail();
        this.firstName = createUserDTO.getFirstName();
        this.lastName = createUserDTO.getLastName();
        this.verificationToken = token;
        this.domain = email.substring(email.indexOf("@") + 1, email.lastIndexOf("."));
        this.brand = createUserDTO.getIsWaterworksSubdomain() ? ErpUtility.MINCRON_BRAND_NAME : "Reece";
        this.isWaterworksSubdomain = createUserDTO.getIsWaterworksSubdomain();
    }

    // TODO: Delete
    public EmployeeVerificationDTO(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.verificationToken = user.getId();
        this.domain = email.substring(email.indexOf("@") + 1, email.lastIndexOf("."));
        this.brand = "Reece";
    }

    private String email;
    private String firstName;
    private String lastName;
    private UUID verificationToken;
    private String brand;
    private String domain;
    private boolean isWaterworksSubdomain;
}
