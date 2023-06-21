package com.reece.platform.accounts.model.DTO;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountValidationRequestDTO {

    @NotBlank(message = "Invalid Parameter: 'accountNumber' is null or empty, which is not valid")
    private String accountNumber;

    @NotBlank(message = "Invalid Parameter: 'zipcode' is null or empty, which is not valid")
    private String zipcode;

    @NotBlank(message = "Invalid Parameter: 'brand' is null or empty, which is not valid")
    private String brand;
}
