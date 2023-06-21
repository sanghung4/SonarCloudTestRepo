package com.reece.platform.accounts.model.DTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCustomerNotificationDTO {

    private String email;
    private String brand;
    private String domain;
    private String managerFirstName;
    private String requestFirstName;
    private String requestLastName;
    private String billToName;
    private String requestEmail;

    private List<UserDTO> accountAdmins;
    private boolean isExistingEcommAccount;

    private String phoneNumber;

    private String accountNumber;

    public NewCustomerNotificationDTO(
        BranchDTO branch,
        ErpAccountInfo accountInfo,
        CreateUserDTO createUserDTO,
        boolean isExistingEcommAccount
    ) {
        this.email = branch.getActingBranchManagerEmail();
        this.brand = branch.getBrand();
        this.domain = branch.getDomain();
        this.billToName = accountInfo.getCompanyName();
        this.managerFirstName = branch.getActingBranchManager();
        this.requestEmail = createUserDTO.getEmail();
        this.requestFirstName = createUserDTO.getFirstName();
        this.requestLastName = createUserDTO.getLastName();
        this.isExistingEcommAccount = isExistingEcommAccount;
        this.phoneNumber = createUserDTO.getPhoneNumber();
        this.accountNumber = accountInfo.getErpAccountId();
    }

    public NewCustomerNotificationDTO(
        BranchDTO branch,
        ErpAccountInfo accountInfo,
        UserRegistrationDTO userRegistrationDTO,
        boolean isExistingEcommAccount
    ) {
        this.email = branch.getActingBranchManagerEmail();
        this.brand = branch.getBrand();
        this.domain = branch.getDomain();
        this.billToName = accountInfo.getCompanyName();
        this.managerFirstName = branch.getActingBranchManager();
        this.requestEmail = userRegistrationDTO.getEmail();
        this.requestFirstName = userRegistrationDTO.getFirstName();
        this.requestLastName = userRegistrationDTO.getLastName();
        this.isExistingEcommAccount = isExistingEcommAccount;
        this.phoneNumber = userRegistrationDTO.getPhoneNumber();
        this.accountNumber = accountInfo.getErpAccountId();
    }
}
