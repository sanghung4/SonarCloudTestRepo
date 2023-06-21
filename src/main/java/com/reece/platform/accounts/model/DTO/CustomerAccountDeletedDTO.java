package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerAccountDeletedDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
    private String companyName;
    private List<String> adminEmails;
    private String branchManagerEmail;
    private String brand;
    private String domain;

    public CustomerAccountDeletedDTO(User user, String erpAccountId, String companyName) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accountNumber = erpAccountId;
        this.companyName = companyName;
    }
}
