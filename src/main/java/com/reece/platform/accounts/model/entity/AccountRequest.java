package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedDate;
import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import com.reece.platform.accounts.utilities.GetCurrentTimeUTC;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Audited
@RequiredArgsConstructor
@Table(name = "account_requests")
public class AccountRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    @Column(name = "id")
    private UUID id;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "erp")
    @Enumerated(EnumType.STRING)
    private ErpEnum erp;

    @Column(name = "email")
    private String email;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "company_address_street")
    private String companyAddressStreet;

    @Column(name = "company_address_2")
    private String companyAddress2;

    @Column(name = "company_address_city")
    private String companyAddressCity;

    @Column(name = "company_address_state")
    private String companyAddressState;

    @Column(name = "company_address_zip")
    private String companyAddressZip;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "phone_type")
    @Enumerated(EnumType.STRING)
    private PhoneTypeEnum phoneType;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "rejection_reason")
    @Enumerated(EnumType.STRING)
    private RejectionReasonEnum rejectionReason;

    @Column(name = "custom_rejection_reason")
    private String customRejectionReason;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "branch_phone_number")
    private String branchPhoneNumber;

    @Column(name = "is_employee")
    private boolean isEmployee;

    @Column(name = "is_verified")
    private boolean isVerified;

    @CreatedDate
    @Column(name = "rejected_at")
    private Date rejectedAt;

    @Column(name = "rejected_by")
    private String rejectedBy;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    @Column(name = "verification_token")
    private UUID verificationToken;

    public AccountRequest(
            CreateUserDTO createUserDTO,
                          boolean isCompleted,
                          UUID accountId,
                          ErpAccountInfo accountInfo,
                          PhoneTypeEnum phoneType,
                          String branchPhoneNumber,
                          boolean isEmployee
    ) {
        var accountInfoDTO = createUserDTO.getAccountInfo();

        if (accountInfoDTO != null) {
            this.erp = accountInfoDTO.getErpId();
            this.accountNumber = accountInfoDTO.getAccountNumber();
            this.companyAddress2 = accountInfoDTO.getCompanyAddress2();
            this.companyAddressCity = accountInfoDTO.getCompanyAddressCity();
            this.companyAddressState = accountInfoDTO.getCompanyAddressState();
            this.companyAddressStreet = accountInfoDTO.getCompanyAddress1();
            this.companyAddressZip = accountInfoDTO.getCompanyAddressZip();
            this.companyName = accountInfoDTO.getCompanyName();
        }

        if (accountInfo != null) {
            this.companyName = accountInfo.getCompanyName();
        }

        this.accountId = accountId;
        this.branchId = createUserDTO.getBranchId();
        this.email = createUserDTO.getEmail();
        this.firstName = createUserDTO.getFirstName();
        this.lastName = createUserDTO.getLastName();
        this.phoneNumber = createUserDTO.getPhoneNumber();
        this.phoneType = phoneType;
        this.branchPhoneNumber = branchPhoneNumber;
        this.createdAt = GetCurrentTimeUTC.getCurrentDateTime();
        this.isEmployee = isEmployee;
        this.verificationToken = UUID.randomUUID();
        this.isCompleted = false;
        this.isVerified = false;
    }
}
