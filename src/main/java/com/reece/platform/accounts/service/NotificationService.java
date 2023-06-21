package com.reece.platform.accounts.service;

import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.AccountRequest;
import com.reece.platform.accounts.model.entity.InvitedUser;
import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    @Value("${notification_service_url}")
    private String notificationServiceUrl;

    @Value("${enable_notification_calls:true}")
    private Boolean enableNotificationCalls;

    @Value("${notification_from_email:support@reecedev.us}")
    private String notificationFromEmail;

    @Value("${employee_domains_no_dns}")
    private String[] EMPLOYEE_DOMAINS_NO_DNS;

    @Value("${default_domain}")
    private String DEFAULT_DOMAIN;

    @Value("${waterworks_domain}")
    private String WATERWORKS_DOMAIN;

    @Value("${morrison_supply_domain}")
    private String MORRISON_SUPPLY;

    @Value("${morsco_hvac_supply_domain}")
    private String MORSCO_HVAC_SUPPLY;

    @Value("${murray_supply_domain}")
    private String MURRAY_SUPPLY;

    @Value("${farnsworth_wholesale_domain}")
    private String FARNSWORTH_WHOLESALE;

    @Value("${express_pipe_domain}")
    private String EXPRESS_PIPE;

    @Value("${devore_and_johnson_domain}")
    private String DEVORE_AND_JOHNSON;

    @Value("${wholesale_specialities_domain}")
    private String WHOLESALE_SPECIALITIES;

    @Value("${expressions_home_gallery_domain}")
    private String EXPRESSIONS_HOME_GALLERY;

    @Value("${landbpipe_domain}")
    private String LANDBPIPE_DOMAIN;

    @Value("${irvinepipe_domain}")
    private String IRVINEPIPE_DOMAIN;

    private static final String REECE = "app";

    @Autowired
    private RestTemplate restTemplate;

    public void sendEmployeeVerificationEmail(EmployeeVerificationDTO employeeVerificationDTO) {
        employeeVerificationDTO.setDomain(
            employeeVerificationDTO.isWaterworksSubdomain()
                ? WATERWORKS_DOMAIN
                : getEmployeeDomain(employeeVerificationDTO.getEmail())
        );
        String verifyEmployeeNotificationUrl = String.format(
            "%s/notifications/employee-email-activate",
            notificationServiceUrl
        );
        restTemplate.postForEntity(verifyEmployeeNotificationUrl, employeeVerificationDTO, String.class);
    }

    public void sendEmployeeVerificationEmailNew(EmployeeVerificationDTO employeeVerificationDTO) {
        employeeVerificationDTO.setDomain(getDomain(employeeVerificationDTO.getBrand()));
        String verifyEmployeeNotificationUrl = String.format(
            "%s/notifications/employee-email-activate",
            notificationServiceUrl
        );
        restTemplate.postForEntity(verifyEmployeeNotificationUrl, employeeVerificationDTO, String.class);
    }

    public String getDomain(String brand) {
        switch (brand) {
            case "Fortiline Waterworks":
                return WATERWORKS_DOMAIN;
            case "Morrison Supply":
                return MORRISON_SUPPLY;
            case "DeVore & Johnson":
                return DEVORE_AND_JOHNSON;
            case "Express Pipe & Supply":
                return EXPRESS_PIPE;
            case "Farnsworth Wholesale Company":
                return FARNSWORTH_WHOLESALE;
            case "Morsco HVAC Supply":
                return MORSCO_HVAC_SUPPLY;
            case "Murray Supply Company":
                return MURRAY_SUPPLY;
            case "Wholesale Specialties":
                return WHOLESALE_SPECIALITIES;
            case "Expressions Home Gallery":
                return EXPRESSIONS_HOME_GALLERY;
            case "L&B Supply":
                return LANDBPIPE_DOMAIN;
            case "Irvine Pipe & Supply":
                return IRVINEPIPE_DOMAIN;
            default:
                return REECE;
        }
    }

    public void sendUserWelcomeEmail(BranchDTO branch, User newUser) {
        // branch may be null if it does not exist in maX yet. Setting fallback values
        if (branch == null) {
            branch = new BranchDTO();
            branch.setBrand("Reece");
            branch.setDomain(DEFAULT_DOMAIN);
            // phone number is not required for welcome email
            branch.setPhone("");
        }
        var welcomeUserDTO = new WelcomeUserDTO(branch, newUser, notificationFromEmail);
        String userWelcomeNotificationUrl = String.format("%s/notifications/welcome-user", notificationServiceUrl);
        restTemplate.postForEntity(userWelcomeNotificationUrl, welcomeUserDTO, String.class);
    }

    public void sendUserApprovedEmail(BranchDTO branch, AccountRequest accountRequest) {
        ApprovedUserNotificationDTO approvedUserNotificationDTO = new ApprovedUserNotificationDTO();

        approvedUserNotificationDTO.setBrand(branch.getBrand());
        approvedUserNotificationDTO.setDomain(branch.getDomain());

        BranchContactInfoNotificationDTO branchContactInfoNotificationDTO = new BranchContactInfoNotificationDTO();
        // TODO: replace support email with branch specific support email
        branchContactInfoNotificationDTO.setSupportEmail(notificationFromEmail);
        branchContactInfoNotificationDTO.setPhoneNumber(accountRequest.getBranchPhoneNumber());
        approvedUserNotificationDTO.setEmail(accountRequest.getEmail());
        approvedUserNotificationDTO.setFirstName(accountRequest.getFirstName());
        approvedUserNotificationDTO.setBranchInfo(branchContactInfoNotificationDTO);
        String approveUserNotificationUrl = String.format("%s/notifications/approve-user", notificationServiceUrl);
        restTemplate.postForEntity(approveUserNotificationUrl, approvedUserNotificationDTO, String.class);
    }

    public void sendUserLoginUpdatedEmail(BranchDTO branch, User savedUser, String oldEmail) {
        AccountUserLoginUpdatedNotificationDTO accountUserLoginUpdatedNotificationDTO = new AccountUserLoginUpdatedNotificationDTO();
        String accountUserLoginUpdatedEmailNotificationUrl = String.format(
            "%s/notifications/updated-login",
            notificationServiceUrl
        );
        accountUserLoginUpdatedNotificationDTO.setSupportEmail(notificationFromEmail);
        accountUserLoginUpdatedNotificationDTO.setFirstName(savedUser.getFirstName());
        accountUserLoginUpdatedNotificationDTO.setLastName(savedUser.getLastName());

        if (branch != null) {
            accountUserLoginUpdatedNotificationDTO.setBrand(branch.getBrand());
            accountUserLoginUpdatedNotificationDTO.setDomain(branch.getDomain());
        }

        // send email to old and new email
        List<String> emailList = new ArrayList<>();
        if (oldEmail != null) {
            emailList.add(oldEmail);
        }
        emailList.add(savedUser.getEmail());
        accountUserLoginUpdatedNotificationDTO.setEmails(emailList);

        restTemplate.postForEntity(
            accountUserLoginUpdatedEmailNotificationUrl,
            accountUserLoginUpdatedNotificationDTO,
            String.class
        );
    }

    public void sendRejectUserEmail(AccountRequest accountRequest, BranchDTO branch) {
        RejectedUserNotificationDTO rejectedUserDTO = new RejectedUserNotificationDTO();
        BranchContactInfoNotificationDTO branchContactInfoNotificationDTO = new BranchContactInfoNotificationDTO();
        // TODO: replace support email with branch specific support email
        branchContactInfoNotificationDTO.setSupportEmail(notificationFromEmail);
        branchContactInfoNotificationDTO.setPhoneNumber(accountRequest.getBranchPhoneNumber());
        rejectedUserDTO.setEmail(accountRequest.getEmail());
        rejectedUserDTO.setFirstName(accountRequest.getFirstName());
        rejectedUserDTO.setBranchInfo(branchContactInfoNotificationDTO);
        rejectedUserDTO.setBrand(branch.getBrand());
        rejectedUserDTO.setDomain(branch.getDomain());

        if (accountRequest.getRejectionReason() == RejectionReasonEnum.OTHER) rejectedUserDTO.setRejectionReason(
            accountRequest.getCustomRejectionReason()
        ); else rejectedUserDTO.setRejectionReason(accountRequest.getRejectionReason().toString());

        String rejectUserNotificationUrl = String.format("%s/notifications/reject-user", notificationServiceUrl);
        restTemplate.postForEntity(rejectUserNotificationUrl, rejectedUserDTO, String.class);
    }

    public void sendUserDeletedEmail(CustomerAccountDeletedDTO customerAccountDeletedDTO, Boolean userLeftCompany) {
        String customerDeletedUrl = String.format("%s/notifications/customer-deleted", notificationServiceUrl);
        if (userLeftCompany) {
            customerDeletedUrl += "?user-left-company=true";
        }
        restTemplate.postForEntity(customerDeletedUrl, customerAccountDeletedDTO, String.class);
    }

    public void sendInviteUserEmail(BranchDTO branch, InvitedUser invite) {
        InviteUserNotificationDTO inviteUserNotificationDTO = new InviteUserNotificationDTO();
        inviteUserNotificationDTO.setEmail(invite.getEmail());
        inviteUserNotificationDTO.setFirstName(invite.getFirstName());
        inviteUserNotificationDTO.setInviteId(invite.getId());
        inviteUserNotificationDTO.setBrand(branch.getBrand());
        inviteUserNotificationDTO.setDomain(branch.getDomain());
        String inviteUserNotificationUrl = String.format("%s/notifications/invite-user", notificationServiceUrl);
        restTemplate.postForEntity(inviteUserNotificationUrl, inviteUserNotificationDTO, String.class);
    }

    private String getEmployeeDomain(String email) {
        if (Arrays.stream(EMPLOYEE_DOMAINS_NO_DNS).anyMatch(email::endsWith)) {
            return DEFAULT_DOMAIN;
        }
        return email.substring(email.indexOf("@") + 1, email.lastIndexOf("."));
    }

    /**
     * Sends email to branch manager when user submits an account request for a new bill-to account
     * @param newCustomerNotificationDTO data for email
     */
    public void sendNewCustomerEmailBranchManager(NewCustomerNotificationDTO newCustomerNotificationDTO) {
        String newCustomerNotificationUrl = String.format(
            "%s/notifications/new-customer-branch-manager",
            notificationServiceUrl
        );
        restTemplate.postForEntity(newCustomerNotificationUrl, newCustomerNotificationDTO, String.class);
    }

    /**
     * Sends email to admin(s) and branch manager when user on an existing account submits an account request
     * @param newUserNotificationDTO data for email
     */
    public void sendNewUserAwaitingApprovalEmail(NewCustomerNotificationDTO newUserNotificationDTO) {
        String inviteUserNotificationUrl = String.format(
            "%s/notifications/new-user-awaiting-approval",
            notificationServiceUrl
        );
        restTemplate.postForEntity(inviteUserNotificationUrl, newUserNotificationDTO, String.class);
    }

    /**
     * Sends email to branch manage or admin(s) if any when user created on an existing account
     * @param newUserNotificationDTO data for email
     */
    public void sendNewUserRegistrationEmail(NewCustomerNotificationDTO newUserNotificationDTO) {
        String newUserRegistrationEmailNotificationUrl = String.format(
            "%s/notifications/new-user-registration",
            notificationServiceUrl
        );
        restTemplate.postForEntity(newUserRegistrationEmailNotificationUrl, newUserNotificationDTO, String.class);
    }
}
