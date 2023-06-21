package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import java.util.UUID;
import lombok.Data;

/**
 * Representation of all user creation data
 */
@Data
public class CreateUserDTO {

    private String email;
    private String branchId;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PhoneTypeEnum phoneTypeId;
    private AccountInfoDTO accountInfo;
    private String userContactTitle;
    private String preferredLocationId;
    private String customerCategory;
    private boolean tosAccepted;
    private boolean ppAccepted;
    private UUID verificationToken;
    private Boolean isWaterworksSubdomain;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
