package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "Invalid Parameter: 'email' is null or empty, which is not valid")
    private String email;

    @NotBlank(message = "Invalid Parameter: 'password' is null or empty, which is not valid")
    private String password;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Invalid Parameter: 'accountNumber' is null or empty, which is not valid")
    private String accountNumber;

    @NotBlank(message = "Invalid Parameter: 'brand' is null or empty, which is not valid")
    private String brand;

    @NotBlank(message = "Invalid Parameter: 'zipcode' is null or empty, which is not valid")
    private String zipcode;

    @NotBlank(message = "Invalid Parameter: 'phoneNumber' is null or empty, which is not valid")
    private String phoneNumber;

    private PhoneTypeEnum phoneType;
    private boolean tosAccepted;
    private boolean ppAccepted;
}
