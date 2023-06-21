package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.ERP.EntitySearchResult;
import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.entity.AccountRequest;
import com.reece.platform.accounts.model.entity.ErpsUsers;
import com.reece.platform.accounts.model.entity.User;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.FeaturesEnum;
import com.reece.platform.accounts.model.repository.AccountDAO;
import com.reece.platform.accounts.model.repository.ErpsUsersDAO;
import com.reece.platform.accounts.model.repository.FeaturesDAO;
import com.reece.platform.accounts.model.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ErpService {

    // Get address from helm-configured env var
    @Value("${eclipse_service_url}")
    private String eclipseServiceUrl;

    @Value("${mincron_service_url}")
    private String mincronServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ErpsUsersDAO erpsUsersDAO;

    @Autowired
    private FeaturesDAO featuresDAO;

    public void setRestTemplate(RestTemplate newTemplate) {
        this.restTemplate = newTemplate;
    }

    // TODO: Delete
    /**
     * Reach out to the eclipse endpoint to create a new user
     *
     * @param accountRequest user to create
     * @return response from eclipse service
     */
    @Transactional
    public ErpContactCreationResponse createErpAccount(AccountRequest accountRequest) {
        // Create the request
        CreateContactRequestDTO createContactRequestDTO = new CreateContactRequestDTO();
        createContactRequestDTO.setFirstName(accountRequest.getFirstName());
        createContactRequestDTO.setLastName(accountRequest.getLastName());
        createContactRequestDTO.setEmail(accountRequest.getEmail());
        createContactRequestDTO.setTelephone(accountRequest.getPhoneNumber());

        if (accountRequest.getPhoneType() != null) {
            createContactRequestDTO.setPhoneType(accountRequest.getPhoneType().getDisplayName());
        }

        ErpEnum erp = accountRequest.getErp();

        HttpEntity<CreateContactRequestDTO> request = new HttpEntity<>(createContactRequestDTO);
        String createContactUrl = String.format(
            "%s/accounts/%s/user",
            erp == ErpEnum.ECLIPSE ? eclipseServiceUrl : mincronServiceUrl,
            accountRequest.getAccountNumber()
        );
        ResponseEntity<ErpContactCreationResponse> response = restTemplate.postForEntity(
            createContactUrl,
            request,
            ErpContactCreationResponse.class
        );
        return response.getBody();
    }

    /**
     * Create Erp Contact record in eclipse
     * @param user
     * @param accountNumber
     * @param erp
     * @return
     */
    @Transactional
    public ErpContactCreationResponse createErpContact(User user, String accountNumber, ErpEnum erp) {
        // Create the request
        CreateContactRequestDTO createContactRequestDTO = new CreateContactRequestDTO();
        createContactRequestDTO.setFirstName(user.getFirstName());
        createContactRequestDTO.setLastName(user.getLastName());
        createContactRequestDTO.setEmail(user.getEmail());
        createContactRequestDTO.setTelephone(user.getPhoneNumber());

        if (user.getPhoneType() != null) {
            createContactRequestDTO.setPhoneType(user.getPhoneType().getDisplayName());
        }

        HttpEntity<CreateContactRequestDTO> request = new HttpEntity<>(createContactRequestDTO);
        String createContactUrl = String.format(
            "%s/accounts/%s/user",
            erp == ErpEnum.ECLIPSE ? eclipseServiceUrl : mincronServiceUrl,
            accountNumber
        );
        ResponseEntity<ErpContactCreationResponse> response = restTemplate.postForEntity(
            createContactUrl,
            request,
            ErpContactCreationResponse.class
        );
        return response.getBody();
    }

    /**
     * NOTE - FOR TEST / DEV ENV USE ONLY
     *
     * Reach out to the eclipse endpoint to re-create a user. This function is intended to recover users if prod
     * ERP data has been copied over test ERP data.
     *
     * NOTE - this only works for the first bill-to account for a given user, meaning the contact will only be created
     * on that account in the ERP system. If a user can be associated to multiple in the future, we'll need to update
     * this function.
     *
     * @param userEmails users to re-create
     * @return response from eclipse service
     */
    @Transactional
    public void recreateErpAccounts(List<String> userEmails) {
        List<User> users = userDAO.findAllByEmailIn(userEmails);

        users.forEach(user -> {
            // Create the request
            CreateContactRequestDTO createContactRequestDTO = new CreateContactRequestDTO();
            createContactRequestDTO.setFirstName(user.getFirstName());
            createContactRequestDTO.setLastName(user.getLastName());
            createContactRequestDTO.setEmail(user.getEmail());
            createContactRequestDTO.setTelephone(user.getPhoneNumber());

            if (user.getPhoneType() != null) {
                createContactRequestDTO.setPhoneType(user.getPhoneType().getDisplayName());
            }

            user
                .getErpsUsers()
                .forEach(erpUser -> {
                    HttpEntity<CreateContactRequestDTO> request = new HttpEntity<>(createContactRequestDTO);
                    String createContactUrl = String.format(
                        "%s/accounts/%s/user",
                        erpUser.getErp() == ErpEnum.ECLIPSE ? eclipseServiceUrl : mincronServiceUrl,
                        user.getBillToAccounts().iterator().next().getErpAccountId()
                    );
                    ResponseEntity<ErpContactCreationResponse> response = restTemplate.postForEntity(
                        createContactUrl,
                        request,
                        ErpContactCreationResponse.class
                    );

                    ErpContactCreationResponse erpContactCreationResponse = response.getBody();
                    erpUser.setErpUserId(erpContactCreationResponse.getContactId());
                    erpUser.setErpUserLogin(erpContactCreationResponse.getErpUsername());
                    erpUser.setErpUserPassword(erpContactCreationResponse.getErpPassword());
                    erpsUsersDAO.save(erpUser);
                });
        });
    }

    /**
     * Get ERP accounts by accountId
     * - Eventually consider
     * @param accountId ERP account ID
     * @return the account information
     */
    public List<ErpAccountInfo> getErpAccounts(String accountId, ErpEnum erp) throws AccountNotFoundException {
        List<ErpAccountInfo> erpAccounts = new ArrayList<>();
        if (erp == ErpEnum.ECLIPSE) {
            var eclipseAccount = getEclipseAccount(accountId, false, false);
            var customerData = getEclipseCustomer(accountId);
            eclipseAccount.setTerritory(customerData.getHomeTerritory());
            eclipseAccount.setBillToFlag(customerData.getIsBillTo());
            eclipseAccount.setAlwaysCod(customerData.getAlwaysCod());
            eclipseAccount.setCreditHold(customerData.getTotalCreditHoldFlag());
            erpAccounts.add(eclipseAccount);
        } else {
            if (featuresDAO.findByName(FeaturesEnum.WATERWORKS.name()).getIsEnabled()) erpAccounts.add(
                getMincronAccount(accountId, false, false)
            );
        }

        if (erpAccounts.isEmpty()) throw new AccountNotFoundException();
        return erpAccounts;
    }

    /**
     * Get ERP Bill To Account
     * Used to verify a users account id during registration. If the account number they have entered is NOT a billTo this function will throw
     * @param accountId
     * @param erp
     * @return
     * @throws AccountNotFoundException
     */
    public ErpAccountInfo getErpBillToAccount(String accountId, ErpEnum erp) throws AccountNotFoundException {
        if (erp.equals(ErpEnum.ECLIPSE)) {
            var account = getEclipseCustomer(accountId);
            // TODO Surround this exception with feature flag check. Or alternatively, just create a new function like getERPBillToOrShipTo
            if (!account.getIsBillTo()) throw new AccountNotFoundException();
            return new ErpAccountInfo(account);
        } else {
            return getMincronAccount(accountId, false, false);
        }
    }

    public ErpAccountInfo getErpBillToOrShipToAccount(String accountId, ErpEnum erp) throws AccountNotFoundException {
        if (erp.equals(ErpEnum.ECLIPSE)) {
            var account = getEclipseCustomer(accountId);
            return new ErpAccountInfo(account);
        } else {
            return getMincronAccount(accountId, false, false);
        }
    }

    /**
     * Get ERP billTo account and all shipTo accounts
     * @param accountId ID of billTo account
     * @return the accounts
     * @throws AccountNotFoundException if account not found
     */
    public ErpAccountInfo getErpAccount(String accountId, ErpEnum erp) throws AccountNotFoundException {
        if (erp.equals(ErpEnum.ECLIPSE)) {
            return getEclipseAccount(accountId, true, false);
        } else {
            return getMincronAccount(accountId, false, false);
        }
    }

    /**
     * Get Mincron account
     *
     * @param accountId Mincron account ID
     * @return the account information
     */
    public ErpAccountInfo getMincronAccount(String accountId, Boolean shouldIncludeShipToAccounts, Boolean isRetry)
        throws AccountNotFoundException {
        String getMincronAccountUrl = String.format(
            "%s/accounts/%s?retrieveShipToList=%s",
            mincronServiceUrl,
            accountId,
            shouldIncludeShipToAccounts
        );

        try {
            ResponseEntity<ErpAccountInfo> response = restTemplate.getForEntity(
                getMincronAccountUrl,
                ErpAccountInfo.class
            );
            ErpAccountInfo info = response.getBody();
            info.setErp(ErpEnum.MINCRON);
            info.setErpAccountId(accountId);
            info.setErpName(ErpEnum.MINCRON.toString());
            return info;
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        } catch (HttpServerErrorException exception) {
            // There is an issue where we get a CONNECTION RESET sometimes when making calls to the Mincron service to get account.
            // This is a "temporary" fix.
            if (!isRetry) return getMincronAccount(accountId, shouldIncludeShipToAccounts, true); else throw exception;
        }
    }

    /**
     * Get Eclipse account
     *
     * @param accountId Eclipse account ID
     * @return the account information
     */
    public ErpAccountInfo getEclipseAccount(
        String accountId,
        boolean shouldGetBillToAccount,
        boolean shouldIncludeShipToAccounts
    ) throws AccountNotFoundException {
        String getEclipseAccountUrl = String.format(
            "%s/accounts/%s?retrieveBillTo=%s&retrieveShipToList=%s",
            eclipseServiceUrl,
            accountId,
            shouldGetBillToAccount,
            shouldIncludeShipToAccounts
        );

        try {
            ResponseEntity<ErpAccountInfo> response = restTemplate.getForEntity(
                getEclipseAccountUrl,
                ErpAccountInfo.class
            );

            // tw - pull this from an enum?
            // dj - yes, pull this from an enum
            ErpAccountInfo info = response.getBody();
            info.setErp(ErpEnum.ECLIPSE);
            info.setErpAccountId(accountId);
            info.setErpName(ErpEnum.ECLIPSE.toString());
            return info;
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) throw new AccountNotFoundException();
            throw exception;
        }
    }

    /**
     * Get Eclipse account
     *
     * @param accountId Eclipse account ID
     * @return the account information
     */
    public ResponseEntity<EntitySearchResult> searchEntity(String accountId) throws AccountNotFoundException {
        String getEclipseEntityUrl = String.format("%s/entitySearch/%s", eclipseServiceUrl, accountId);

        try {
            ResponseEntity<EntitySearchResult> response = restTemplate.getForEntity(
                getEclipseEntityUrl,
                EntitySearchResult.class
            );
            response.getBody().setErpName(ErpEnum.ECLIPSE.name());
            return response;
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) throw new AccountNotFoundException();
            throw exception;
        }
    }

    /**
     * Sends a request to update user information in Mincron and/or Eclipse ERP.
     *
     * @param accountId e-commerce accoundId
     * @param userId e-commerce userId
     * @param userDTO updated user information
     * @return update user information
     * @throws AccountNotFoundException
     * @throws PhoneTypeNotFoundException
     */
    @Transactional
    public UserDTO updateErpUser(UUID accountId, UUID userId, UserDTO userDTO)
        throws AccountNotFoundException, PhoneTypeNotFoundException, InvalidPhoneNumberException, UserNotFoundException {
        if (userDTO.getPhoneTypeId() != null) {
            userDTO.setPhoneTypeDisplayName(userDTO.getPhoneTypeId().getDisplayName());
        }

        Set<ErpsUsers> erpsUsers = userDAO.findById(userId).orElseThrow(UserNotFoundException::new).getErpsUsers();
        // erpsAccountsDAO makes assumption that there is only one ERP account per accountId
        // Using .getAccount().getErpsAccounts() is a current workaround for case where one accountId maps to multiple ERPs
        var account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);

        for (ErpsUsers erpUser : erpsUsers) {
            if (erpUser.getErp().equals(ErpEnum.MINCRON)) {
                String mincronUserId = erpUser.getErpUserId();

                if (account.getErp().equals(ErpEnum.MINCRON)) {
                    String mincronAccountId = account.getErpAccountId();
                    userDTO.setErpUserPassword(erpUser.getErpUserPassword());
                    this.sendUpdateErpUserRequest(
                            Integer.parseInt(mincronAccountId),
                            mincronUserId,
                            userDTO,
                            mincronServiceUrl
                        );
                }
            }

            if (erpUser.getErp().equals(ErpEnum.ECLIPSE)) {
                String eclipseUserId = erpUser.getErpUserId();

                if (account.getErp().equals(ErpEnum.ECLIPSE)) {
                    String eclipseAccountId = account.getErpAccountId();
                    this.sendUpdateErpUserRequest(
                            Integer.parseInt(eclipseAccountId),
                            eclipseUserId,
                            userDTO,
                            eclipseServiceUrl
                        );
                }
            }
        }
        return userDTO;
    }

    /**
     * @todo Refactor this to only support one ErpAccount per Account
     * Queries for ERP account and user ID's given E-commerce account and user ID's.
     * Then sends a request to delete a user from Mincron and/or Eclipse ERP.
     * @param accountId
     * @param userId
     * @throws AccountNotFoundException
     * @throws UserNotFoundException
     */
    @Transactional
    public void deleteErpUser(UUID accountId, UUID userId)
        throws AccountNotFoundException, UserNotFoundException, EclipseException {
        Optional<User> user = this.userDAO.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with given ID not found.");
        }
        Set<ErpsUsers> erpsUsers = user.get().getErpsUsers();

        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);

        for (ErpsUsers erpUser : erpsUsers) {
            if (erpUser.getErp().equals(ErpEnum.MINCRON)) {
                String mincronUserId = erpUser.getErpUserId();

                if (account.getErp().equals(ErpEnum.MINCRON)) {
                    String mincronAccountId = account.getErpAccountId();
                    this.sendDeleteErpUserRequest(Integer.parseInt(mincronAccountId), mincronUserId, mincronServiceUrl);
                }
            }

            if (erpUser.getErp().equals(ErpEnum.ECLIPSE)) {
                String eclipseUserId = erpUser.getErpUserId();

                if (account.getErp().equals(ErpEnum.ECLIPSE)) {
                    String eclipseAccountId = account.getErpAccountId();
                    this.sendDeleteErpUserRequest(Integer.parseInt(eclipseAccountId), eclipseUserId, eclipseServiceUrl);
                }
            }
        }
    }

    public void sendDeleteErpUserRequest(int accountId, String userId, String erpServiceUrl) throws EclipseException {
        String deleteErpUserUrl = String.format("%s/accounts/%s/user/%s", erpServiceUrl, accountId, userId);
        try {
            restTemplate.delete(deleteErpUserUrl);
        } catch (HttpClientErrorException e) {
            throw new EclipseException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public UserDTO sendUpdateErpUserRequest(int accountId, String userId, UserDTO userDTO, String erpServiceUrl)
        throws AccountNotFoundException, InvalidPhoneNumberException {
        String getERPAccountUrl = String.format("%s/accounts/%s/user/%s", erpServiceUrl, accountId, userId);

        try {
            restTemplate.put(getERPAccountUrl, userDTO);
            return userDTO;
        } catch (HttpClientErrorException.BadRequest exception) {
            if (exception.getMessage().contains("Invalid phone number")) {
                throw new InvalidPhoneNumberException();
            } else {
                throw exception;
            }
        } catch (HttpClientErrorException.NotFound exception) {
            throw new AccountNotFoundException();
        }
    }

    public DocumentImagingFileDTO uploadTaxDocument(String file, String erpAccountId) throws AccountNotFoundException {
        String getERPAccountUrl = String.format("%s/accounts/%s/tax-document", eclipseServiceUrl, erpAccountId);

        try {
            ResponseEntity<DocumentImagingFileDTO> responseEntity = restTemplate.postForEntity(
                getERPAccountUrl,
                file,
                DocumentImagingFileDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Get credit card list elements
     *
     * @param accountId
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    public CreditCardListResponseDTO getCreditCardList(String accountId) throws AccountNotFoundException {
        String getERPAccountUrl = String.format("%s/credit-card/%s", eclipseServiceUrl, accountId);
        try {
            ResponseEntity<CreditCardListResponseDTO> responseEntity = restTemplate.getForEntity(
                getERPAccountUrl,
                CreditCardListResponseDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Update account with credit card list
     *
     * @param accountId
     * @param updateSubmitRequestDTO
     *
     * @return  EntityUpdateSubmitResponseDTO
     * @throws AccountNotFoundException
     */
    public EntityUpdateSubmitResponseDTO updateCreditCardList(
        String accountId,
        EntityUpdateSubmitRequestDTO updateSubmitRequestDTO
    ) throws AccountNotFoundException {
        String getERPAccountUrl = String.format("%s/credit-card/%s", eclipseServiceUrl, accountId);
        try {
            ResponseEntity<EntityUpdateSubmitResponseDTO> responseEntity = restTemplate.postForEntity(
                getERPAccountUrl,
                updateSubmitRequestDTO,
                EntityUpdateSubmitResponseDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Update account with credit card list
     *
     * @param accountId
     *
     * @return  EntityUpdateSubmitResponseDTO
     * @throws AccountNotFoundException
     */
    public void deleteCreditCard(String accountId, String creditCardId)
        throws AccountNotFoundException, CardInUseException {
        String deleteCreditCardUrl = String.format("%s/credit-card/%s/%s", eclipseServiceUrl, accountId, creditCardId);
        try {
            restTemplate.delete(deleteCreditCardUrl);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.CONFLICT) throw new CardInUseException(creditCardId); else if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Get credit card setup URL for iframe
     * @param accountId
     * @param elementSetupUrlDTO
     *
     * @return ElementSetUpUrlResponseDTO
     * @throws AccountNotFoundException
     */
    public ElementSetUpUrlResponseDTO getCreditCardSetupUrl(String accountId, ElementSetupUrlDTO elementSetupUrlDTO)
        throws AccountNotFoundException {
        String getCreditCardUrl = String.format("%s/credit-card/%s/setup-url", eclipseServiceUrl, accountId);

        try {
            ResponseEntity<ElementSetUpUrlResponseDTO> responseEntity = restTemplate.postForEntity(
                getCreditCardUrl,
                elementSetupUrlDTO,
                ElementSetUpUrlResponseDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Get credit card information entered into WorldPay
     *
     * @param accountId
     * @param  elementSetupId
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    public ElementSetupQueryResponseDTO getCreditCardElementInfo(String accountId, String elementSetupId)
        throws AccountNotFoundException {
        String getERPAccountUrl = String.format(
            "%s/credit-card/%s/info/%s",
            eclipseServiceUrl,
            accountId,
            elementSetupId
        );
        try {
            ResponseEntity<ElementSetupQueryResponseDTO> responseEntity = restTemplate.postForEntity(
                getERPAccountUrl,
                null,
                ElementSetupQueryResponseDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Submit job form to Eclipse for job creation
     *
     * @param jobFormDTO job data
     * @return message indicating success or failure information
     * @throws AccountNotFoundException when Eclipse call returns a 404
     */
    public String createJobForm(JobFormDTO jobFormDTO) throws AccountNotFoundException {
        String postJobFormUrl = String.format("%s/accounts/job", eclipseServiceUrl);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                postJobFormUrl,
                jobFormDTO,
                String.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }

    /**
     * Get Eclipse Customer Data
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    public CustomerDTO getEclipseCustomer(String accountId) throws AccountNotFoundException {
        String getEclipseCustomerUrl = String.format("%s/customers/%s", eclipseServiceUrl, accountId);
        try {
            ResponseEntity<CustomerDTO> customerResponse = restTemplate.getForEntity(
                getEclipseCustomerUrl,
                CustomerDTO.class
            );

            return customerResponse.getBody();
        } catch (HttpClientErrorException exception) {
            if (
                exception.getStatusCode() == HttpStatus.NOT_FOUND
            ) throw new AccountNotFoundException(); else throw exception;
        }
    }
}
