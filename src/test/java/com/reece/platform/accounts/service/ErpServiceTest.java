package com.reece.platform.accounts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.exception.AccountNotFoundException;
import com.reece.platform.accounts.exception.EclipseException;
import com.reece.platform.accounts.exception.InvalidPhoneNumberException;
import com.reece.platform.accounts.exception.UserNotFoundException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.DTO.ERP.EntitySearchResult;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.FeaturesEnum;
import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import com.reece.platform.accounts.model.repository.AccountDAO;
import com.reece.platform.accounts.model.repository.ErpsUsersDAO;
import com.reece.platform.accounts.model.repository.FeaturesDAO;
import com.reece.platform.accounts.model.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = { ErpService.class, RestTemplate.class, ObjectMapper.class })
public class ErpServiceTest {

    @Autowired
    private ErpService erpService;

    private MockRestServiceServer mockRestServiceServer;

    @MockBean
    private UserDAO userDao;

    @MockBean
    private AccountDAO accountsDAO;

    @MockBean
    private ErpsUsersDAO erpsUsersDAO;

    @MockBean
    private FeaturesDAO featuresDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        Feature feature = new Feature(UUID.randomUUID(), FeaturesEnum.WATERWORKS.name(), true);

        ReflectionTestUtils.setField(erpService, "eclipseServiceUrl", "http://ecomm-dev-eclipse-service:8080");
        ReflectionTestUtils.setField(erpService, "mincronServiceUrl", "http://ecomm-dev-mincron-service:8080");

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

        when(featuresDAO.findByName(FeaturesEnum.WATERWORKS.name())).thenReturn(feature);
    }

    @Test
    void createEclipseAccount() throws Exception {
        ErpContactCreationResponse erpContactCreationResponse = new ErpContactCreationResponse();
        List<ErpAccountInfo> shipTos = List.of(new ErpAccountInfo());

        var accountRequest = new AccountRequest();
        accountRequest.setErp(ErpEnum.ECLIPSE);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpContactCreationResponse))
            );

        erpService.createErpAccount(accountRequest);
    }

    @Test
    void createErpContact_success() throws Exception {
        ErpContactCreationResponse erpContactCreationResponse = new ErpContactCreationResponse();
        List<ErpAccountInfo> shipTos = List.of(new ErpAccountInfo());

        var user = new User();
        user.setPhoneType(PhoneTypeEnum.MOBILE);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpContactCreationResponse))
            );

        erpService.createErpContact(user, "1234", ErpEnum.ECLIPSE);
    }

    @Test
    void getErpBillToAccount_success_eclipse() throws Exception {
        String id = "123";
        String eclipseAccountName = "ECLIPSE_ACCOUNT";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName(eclipseAccountName);

        var customerDto = new CustomerDTO();
        customerDto.setIsBillTo(true);
        customerDto.setName(erpAccountInfo.getCompanyName());
        customerDto.setTotalCreditHoldFlag(false);
        customerDto.setIsBranch(false);
        customerDto.setIsShipTo(false);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-eclipse-service:8080/customers/123"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(customerDto))
            );

        var response = erpService.getErpBillToAccount(id, ErpEnum.ECLIPSE);
        assertEquals(
            customerDto.getName(),
            response.getCompanyName(),
            "Expected eclipse account to return from get erp accounts call"
        );
    }

    @Test
    void getErpBillToAccount_invalidBillTo_eclipse() throws Exception {
        String id = "123";
        String eclipseAccountName = "ECLIPSE_ACCOUNT";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName(eclipseAccountName);

        var customerDto = new CustomerDTO();
        customerDto.setIsBillTo(false);
        customerDto.setName(erpAccountInfo.getCompanyName());
        customerDto.setTotalCreditHoldFlag(false);
        customerDto.setIsBranch(false);
        customerDto.setIsShipTo(false);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-eclipse-service:8080/customers/123"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(customerDto))
            );

        assertThrows(AccountNotFoundException.class, () -> erpService.getErpBillToAccount(id, ErpEnum.ECLIPSE));
    }

    @Test
    void getErpBillToAccount_success_mincron() throws Exception {
        String id = "123";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName("MINCRON_ACCOUNT");

        var customerDto = new CustomerDTO();
        customerDto.setIsBillTo(false);
        customerDto.setName(erpAccountInfo.getCompanyName());
        customerDto.setTotalCreditHoldFlag(false);
        customerDto.setIsBranch(false);
        customerDto.setIsShipTo(false);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-mincron-service:8080/accounts/123?retrieveShipToList=false")
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpAccountInfo))
            );

        var response = erpService.getErpBillToAccount(id, ErpEnum.MINCRON);

        assertEquals(response.getCompanyName(), erpAccountInfo.getCompanyName());
    }

    @Test
    void getEclipseAccount_success() throws Exception {
        String id = "123";

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    "http://ecomm-dev-eclipse-service:8080/accounts/123?retrieveBillTo=true&retrieveShipToList=false"
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(new ErpAccountInfo()))
            );

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-eclipse-service:8080/customers/123"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(new CustomerDTO()))
            );

        ErpAccountInfo response = erpService.getEclipseAccount(id, true, false);

        ErpAccountInfo expected = new ErpAccountInfo();
        expected.setErp(ErpEnum.ECLIPSE);
        expected.setErpName(ErpEnum.ECLIPSE.toString());
        expected.setErpAccountId(id);

        assertEquals(response, expected);
    }

    @Test
    void updateErpUser_Eclipse() throws Exception {
        UserDTO userDTO = new UserDTO();

        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers eclipseErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        eclipseErpsUser.setErpUserId("54234");

        account.setErp(ErpEnum.ECLIPSE);

        eclipseErpsUser.setErp(ErpEnum.ECLIPSE);

        account.setErpAccountId("123");

        erpUserSet.add(eclipseErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.updateErpUser(new UUID(0, 0), new UUID(0, 0), userDTO);
    }

    @Test
    void updateErpUser_Mincron() throws Exception {
        UserDTO userDTO = new UserDTO();

        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        mincronErpsUser.setErpUserId("54233");

        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("122");
        account.setErp(ErpEnum.MINCRON);

        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.updateErpUser(new UUID(0, 0), new UUID(0, 0), userDTO);
    }

    @Test
    void updateErpUser_success() throws Exception {
        UserDTO userDTO = new UserDTO();

        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers eclipseErpsUser = new ErpsUsers();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        eclipseErpsUser.setErpUserId("54234");
        mincronErpsUser.setErpUserId("54233");

        eclipseErpsUser.setErp(ErpEnum.ECLIPSE);
        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("123");
        account.setErp(ErpEnum.ECLIPSE);

        erpUserSet.add(eclipseErpsUser);
        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.OK));

        UserDTO res = erpService.updateErpUser(new UUID(0, 0), new UUID(0, 0), userDTO);
        assertEquals(userDTO, res);
    }

    @Test
    void updateErpUser_failure() {
        UserDTO userDTO = new UserDTO();

        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers eclipseErpsUser = new ErpsUsers();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        eclipseErpsUser.setErpUserId("54234");
        mincronErpsUser.setErpUserId("54233");

        eclipseErpsUser.setErp(ErpEnum.ECLIPSE);
        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("123");
        account.setErp(ErpEnum.ECLIPSE);

        erpUserSet.add(eclipseErpsUser);
        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(
            AccountNotFoundException.class,
            () -> erpService.updateErpUser(new UUID(0, 0), new UUID(0, 0), userDTO)
        );
    }

    @Test
    void updateErpUser_Invalid_Phone_Format() {
        UserDTO userDTO = new UserDTO();

        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        mincronErpsUser.setErpUserId("54233");

        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("122");
        account.setErp(ErpEnum.MINCRON);

        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST).body("Invalid phone number"));
        assertThrows(
            InvalidPhoneNumberException.class,
            () -> erpService.updateErpUser(new UUID(0, 0), new UUID(0, 0), userDTO)
        );
    }

    @Test
    void getErpAccount_success_eclipse() throws Exception {
        String eclipseAccountName = "ECLIPSE_ACCOUNT";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName(eclipseAccountName);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setIsBillTo(true);
        customerDTO.setTotalCreditHoldFlag(true);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    "http://ecomm-dev-eclipse-service:8080/accounts/123?retrieveBillTo=false&retrieveShipToList=false"
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpAccountInfo))
            );

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-eclipse-service:8080/customers/123"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(customerDTO))
            );

        List<ErpAccountInfo> response = erpService.getErpAccounts("123", ErpEnum.ECLIPSE);
        assertEquals(response.size(), 1, "Expected only one erp account to return for eclipse success test");
        assertEquals(
            response.get(0).getCompanyName(),
            eclipseAccountName,
            "Expected eclipse account to return from get erp accounts call"
        );
    }

    @Test
    void getErpAccount_success_mincron() throws Exception {
        String mincronAccountName = "MINCRON_ACCOUNT";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName(mincronAccountName);

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo("http://ecomm-dev-mincron-service:8080/accounts/123?retrieveShipToList=false")
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpAccountInfo))
            );

        List<ErpAccountInfo> response = erpService.getErpAccounts("123", ErpEnum.MINCRON);
        assertEquals(response.size(), 1, "Expected only one erp account to return for mincron success test");
        assertEquals(
            response.get(0).getCompanyName(),
            mincronAccountName,
            "Expected mincron account to return from get erp accounts call"
        );
    }

    @Test
    void getErpAccount_success_no_account_found() {
        mockRestServiceServer
            .expect(ExpectedCount.twice(), anything())
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(AccountNotFoundException.class, () -> erpService.getErpAccounts("123", ErpEnum.ECLIPSE));
    }

    @Test
    void deleteErpUser_throwsUserNotFoundException() {
        when(userDao.findById(any())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> erpService.deleteErpUser(new UUID(0, 0), new UUID(0, 0))
        );
        assertTrue(exception.getMessage().equals("User with given ID not found."));
    }

    @Test
    void deleteErpUser_throwsAccountNotFoundException() {
        User user = new User();
        HashSet erpUserSet = new HashSet();
        user.setErpsUsers(erpUserSet);
        when(userDao.findById(any())).thenReturn(Optional.of(user));
        assertThrows(AccountNotFoundException.class, () -> erpService.deleteErpUser(new UUID(0, 0), new UUID(0, 0)));
    }

    @Test
    void sendDeleteErpUserRequest_throwsUserNotFoundException() {
        mockRestServiceServer
            .expect(ExpectedCount.twice(), anything())
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        assertThrows(EclipseException.class, () -> erpService.sendDeleteErpUserRequest(1, "1", "1"));
    }

    @Test
    void deleteErpUser_Eclipse() throws Exception {
        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers eclipseErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        eclipseErpsUser.setErpUserId("54234");

        eclipseErpsUser.setErp(ErpEnum.ECLIPSE);

        account.setErpAccountId("123");
        account.setErp(ErpEnum.ECLIPSE);

        erpUserSet.add(eclipseErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.deleteErpUser(new UUID(0, 0), new UUID(0, 0));
    }

    @Test
    void deleteErpUser_Mincron() throws Exception {
        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        mincronErpsUser.setErpUserId("54233");

        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("122");
        account.setErp(ErpEnum.MINCRON);

        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.deleteErpUser(new UUID(0, 0), new UUID(0, 0));
    }

    @Test
    void deleteErpUser_Mincron_and_Eclipse() throws Exception {
        UUID eCommAccountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(eCommAccountId);

        User user = new User();
        ErpsUsers eclipseErpsUser = new ErpsUsers();
        ErpsUsers mincronErpsUser = new ErpsUsers();
        HashSet erpUserSet = new HashSet();
        HashSet accountsSet = new HashSet();

        eclipseErpsUser.setErpUserId("54234");
        mincronErpsUser.setErpUserId("54233");

        eclipseErpsUser.setErp(ErpEnum.ECLIPSE);
        mincronErpsUser.setErp(ErpEnum.MINCRON);

        account.setErpAccountId("123");
        account.setErp(ErpEnum.ECLIPSE);

        erpUserSet.add(eclipseErpsUser);
        erpUserSet.add(mincronErpsUser);

        accountsSet.add(account);

        user.setBillToAccounts(accountsSet);
        user.setErpsUsers(erpUserSet);

        when(userDao.findById(any())).thenReturn(Optional.of(user));
        when(accountsDAO.findById(any())).thenReturn(Optional.of(account));

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.deleteErpUser(new UUID(0, 0), new UUID(0, 0));
    }

    @Test
    void uploadTaxDocument_success() throws Exception {
        String file = "file";
        String erpAccountId = "erp";
        DocumentImagingFileDTO documentImagingFileDTO = new DocumentImagingFileDTO();
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(documentImagingFileDTO))
            );

        DocumentImagingFileDTO document = erpService.uploadTaxDocument(file, erpAccountId);
        assertEquals(document, documentImagingFileDTO);
    }

    @Test
    void uploadTaxDocument_Failure() throws Exception {
        String file = "file";
        String erpAccountId = "erp";
        DocumentImagingFileDTO documentImagingFileDTO = new DocumentImagingFileDTO();
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(AccountNotFoundException.class, () -> erpService.uploadTaxDocument(file, erpAccountId));
    }

    @Test
    void createJobForm_success() throws Exception {
        JobFormDTO jobFormDTO = new JobFormDTO();
        String response = "success";
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response));

        String result = erpService.createJobForm(jobFormDTO);
        assertEquals(response, result);
    }

    @Test
    void createJobForm_Failure() throws Exception {
        JobFormDTO jobFormDTO = new JobFormDTO();
        String response = "Failure";
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response));

        assertThrows(AccountNotFoundException.class, () -> erpService.createJobForm(jobFormDTO));
    }

    @Test
    void getSetupURL_success() throws Exception {
        ElementSetupUrlDTO elementSetupUrlDTO = new ElementSetupUrlDTO();
        elementSetupUrlDTO.setCardHolder("John Doe");
        elementSetupUrlDTO.setStreetAddress("123 Test Drive");
        elementSetupUrlDTO.setPostalCode("76227");
        elementSetupUrlDTO.setReturnUrl("localhost");
        ElementSetUpUrlResponseDTO elementSetUpUrlResponseDTO = new ElementSetUpUrlResponseDTO();
        elementSetUpUrlResponseDTO.setElementSetupUrl("localhost");
        elementSetUpUrlResponseDTO.setElementSetupId("132546");
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(elementSetUpUrlResponseDTO))
            );
        ElementSetUpUrlResponseDTO responseDTO = erpService.getCreditCardSetupUrl("123", elementSetupUrlDTO);
        assertEquals(elementSetUpUrlResponseDTO.getElementSetupUrl(), responseDTO.getElementSetupUrl());
        assertEquals(elementSetUpUrlResponseDTO.getElementSetupId(), responseDTO.getElementSetupId());
    }

    @Test
    void getSetupURL_failure() throws Exception {
        ElementSetupUrlDTO elementSetupUrlDTO = new ElementSetupUrlDTO();
        elementSetupUrlDTO.setCardHolder("John Doe");
        elementSetupUrlDTO.setStreetAddress("123 Test Drive");
        elementSetupUrlDTO.setPostalCode("76227");
        elementSetupUrlDTO.setReturnUrl("localhost");
        ElementSetUpUrlResponseDTO elementSetUpUrlResponseDTO = new ElementSetUpUrlResponseDTO();
        elementSetUpUrlResponseDTO.setElementSetupUrl("localhost");
        elementSetUpUrlResponseDTO.setElementSetupId("132546");
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(elementSetUpUrlResponseDTO))
            );
        assertThrows(AccountNotFoundException.class, () -> erpService.getCreditCardSetupUrl("123", elementSetupUrlDTO));
    }

    @Test
    void getCreditCardElementInfo_success() throws Exception {
        ElementSetupUrlDTO elementSetupUrlDTO = new ElementSetupUrlDTO();
        elementSetupUrlDTO.setCardHolder("John Doe");
        elementSetupUrlDTO.setStreetAddress("123 Test Drive");
        elementSetupUrlDTO.setPostalCode("76227");
        elementSetupUrlDTO.setReturnUrl("localhost");
        ElementSetupQueryResponseDTO elementSetUpUrlResponseDTO = new ElementSetupQueryResponseDTO();
        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);
        creditCard.setElementPaymentAccountId(UUID.randomUUID());
        elementSetUpUrlResponseDTO.setCreditCard(creditCard);
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(elementSetUpUrlResponseDTO))
            );
        ElementSetupQueryResponseDTO responseDTO = erpService.getCreditCardElementInfo("123", "1321464");
        assertEquals(elementSetUpUrlResponseDTO.getCreditCard(), responseDTO.getCreditCard());
    }

    @Test
    void getCreditCardElementInfo_Failure() throws Exception {
        ElementSetupUrlDTO elementSetupUrlDTO = new ElementSetupUrlDTO();
        elementSetupUrlDTO.setCardHolder("John Doe");
        elementSetupUrlDTO.setStreetAddress("123 Test Drive");
        elementSetupUrlDTO.setPostalCode("76227");
        elementSetupUrlDTO.setReturnUrl("localhost");
        ElementSetupQueryResponseDTO elementSetUpUrlResponseDTO = new ElementSetupQueryResponseDTO();
        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);
        creditCard.setElementPaymentAccountId(UUID.randomUUID());
        elementSetUpUrlResponseDTO.setCreditCard(creditCard);
        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(elementSetUpUrlResponseDTO))
            );
        assertThrows(AccountNotFoundException.class, () -> erpService.getCreditCardElementInfo("123", "1321464"));
    }

    @Test
    void addCreditCardInfo_Failure() throws Exception {
        EntityUpdateSubmitRequestDTO updateSubmitRequestDTO = new EntityUpdateSubmitRequestDTO();

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Test");
        addressDTO.setPostalCode("76227");
        addressDTO.setCountry("US");
        addressDTO.setStreetLineOne("123 Test Drive");
        addressDTO.setStreetLineTwo("");
        addressDTO.setStreetLineOne("");
        addressDTO.setState("TX");

        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);

        updateSubmitRequestDTO.setCreditCard(creditCard);

        EntityUpdateSubmitResponseDTO entityUpdateSubmitResponseDTO = new EntityUpdateSubmitResponseDTO();
        StatusResult statusResult = new StatusResult();
        statusResult.setSuccess("Yes");
        statusResult.setDescription("Entity successfully updated");
        entityUpdateSubmitResponseDTO.setStatusResult(statusResult);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(
            AccountNotFoundException.class,
            () -> erpService.updateCreditCardList("123", updateSubmitRequestDTO)
        );
    }

    @Test
    void addCreditCardInfo_success() throws Exception {
        EntityUpdateSubmitRequestDTO updateSubmitRequestDTO = new EntityUpdateSubmitRequestDTO();

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Test");
        addressDTO.setPostalCode("76227");
        addressDTO.setCountry("US");
        addressDTO.setStreetLineOne("123 Test Drive");
        addressDTO.setStreetLineTwo("");
        addressDTO.setStreetLineOne("");
        addressDTO.setState("TX");

        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);

        updateSubmitRequestDTO.setCreditCard(creditCard);

        EntityUpdateSubmitResponseDTO entityUpdateSubmitResponseDTO = new EntityUpdateSubmitResponseDTO();
        StatusResult statusResult = new StatusResult();
        statusResult.setSuccess("Yes");
        statusResult.setDescription("Entity successfully updated");
        entityUpdateSubmitResponseDTO.setStatusResult(statusResult);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(entityUpdateSubmitResponseDTO))
            );

        EntityUpdateSubmitResponseDTO responseDTO = erpService.updateCreditCardList("123", updateSubmitRequestDTO);
        assertEquals(entityUpdateSubmitResponseDTO.getStatusResult(), responseDTO.getStatusResult());
    }

    @Test
    void getCreditCardListInfo_success() throws Exception {
        CreditCardListResponseDTO cardListResponseDTO = new CreditCardListResponseDTO();

        CreditCardList creditCardList = new CreditCardList();
        List<CreditCard> cardList = new ArrayList<>();
        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);
        cardList.add(creditCard);
        creditCardList.setCreditCard(cardList);

        cardListResponseDTO.setCreditCardList(creditCardList);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(cardListResponseDTO))
            );

        CreditCardListResponseDTO responseDTO = erpService.getCreditCardList("123");
        assertEquals(cardListResponseDTO.getCreditCardList(), responseDTO.getCreditCardList());
    }

    @Test
    void getCreditCardListInfo_Failure() throws Exception {
        CreditCardListResponseDTO cardListResponseDTO = new CreditCardListResponseDTO();

        CreditCardList creditCardList = new CreditCardList();
        List<CreditCard> cardList = new ArrayList<>();
        CreditCard creditCard = new CreditCard();
        DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setDate("01/01/2025");
        creditCard.setCreditCardType("VISA");
        creditCard.setCardHolder("John Doe");
        creditCard.setCreditCardNumber("4444");
        creditCard.setStreetAddress("123 Test Drive");
        creditCard.setPostalCode("76227");
        creditCard.setExpirationDate(dateWrapper);
        cardList.add(creditCard);
        creditCardList.setCreditCard(cardList);

        cardListResponseDTO.setCreditCardList(creditCardList);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(cardListResponseDTO))
            );

        assertThrows(AccountNotFoundException.class, () -> erpService.getCreditCardList("123"));
    }

    @Test
    void getEclipseCustomer_success() throws Exception {
        String id = "123";
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName("MINCRON_ACCOUNT");

        var customerDto = new CustomerDTO();
        customerDto.setIsBillTo(false);
        customerDto.setId(id);
        customerDto.setIsBillTo(false);
        customerDto.setName(erpAccountInfo.getCompanyName());
        customerDto.setTotalCreditHoldFlag(false);
        customerDto.setIsBranch(false);
        customerDto.setIsShipTo(false);

        String getEclipseCustomerUrl = String.format("%s/customers/%s", eclipseServiceUrl, id);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getEclipseCustomerUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(customerDto))
            );

        var response = erpService.getEclipseCustomer(id);

        assertEquals(response.getId(), id);
    }

    @Test
    void getEclipseCustomer_Failure() throws Exception {
        String id = "123";
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        ErpAccountInfo erpAccountInfo = new ErpAccountInfo();
        erpAccountInfo.setCompanyName("MINCRON_ACCOUNT");

        var customerDto = new CustomerDTO();
        customerDto.setIsBillTo(false);
        customerDto.setId(id);
        customerDto.setIsBillTo(false);
        customerDto.setName(erpAccountInfo.getCompanyName());
        customerDto.setTotalCreditHoldFlag(false);
        customerDto.setIsBranch(false);
        customerDto.setIsShipTo(false);

        String getEclipseCustomerUrl = String.format("%s/customers/%s", eclipseServiceUrl, id);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getEclipseCustomerUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(customerDto))
            );

        assertThrows(AccountNotFoundException.class, () -> erpService.getEclipseCustomer(id));
    }

    @Test
    void deleteCreditCard_success() throws Exception {
        String creditCardId = "123";
        String accountId = UUID.randomUUID().toString();
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String deleteCreditCardUrl = String.format("%s/credit-card/%s/%s", eclipseServiceUrl, accountId, creditCardId);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(deleteCreditCardUrl))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.OK));

        erpService.deleteCreditCard(accountId, creditCardId);
    }

    @Test
    void deleteCreditCard_Failure() throws Exception {
        String creditCardId = "123";
        String accountId = UUID.randomUUID().toString();
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String deleteCreditCardUrl = String.format("%s/credit-card/%s/%s", eclipseServiceUrl, accountId, creditCardId);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(deleteCreditCardUrl))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(AccountNotFoundException.class, () -> erpService.deleteCreditCard(accountId, creditCardId));
    }

    @Test
    void sendUpdateErpUserRequest_Failure() throws Exception {
        String erpServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        UUID userId = UUID.randomUUID();
        int accountId = 123;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        String getERPAccountUrl = String.format("%s/accounts/%s/user/%s", erpServiceUrl, accountId, userId);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getERPAccountUrl))
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(
            AccountNotFoundException.class,
            () -> erpService.sendUpdateErpUserRequest(accountId, userId.toString(), userDTO, erpServiceUrl)
        );
    }

    @Test
    void sendUpdateErpUserRequest_BadRequest() throws Exception {
        String erpServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        UUID userId = UUID.randomUUID();
        int accountId = 123;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        String getERPAccountUrl = String.format("%s/accounts/%s/user/%s", erpServiceUrl, accountId, userId);

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getERPAccountUrl))
            .andExpect(method(HttpMethod.PUT))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(
            HttpClientErrorException.class,
            () -> erpService.sendUpdateErpUserRequest(accountId, userId.toString(), userDTO, erpServiceUrl)
        );
    }

    @Test
    void searchEntity_Success() throws Exception {
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String accountId = "123";
        String getEclipseEntityUrl = String.format("%s/entitySearch/%s", eclipseServiceUrl, accountId);
        EntitySearchResult entitySearchResult = new EntitySearchResult();
        entitySearchResult.setId("123");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getEclipseEntityUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(entitySearchResult))
            );

        var response = erpService.searchEntity(accountId);
    }

    @Test
    void searchEntity_Failure() throws Exception {
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String accountId = "123";
        String getEclipseEntityUrl = String.format("%s/entitySearch/%s", eclipseServiceUrl, accountId);
        EntitySearchResult entitySearchResult = new EntitySearchResult();
        entitySearchResult.setId("123");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getEclipseEntityUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(AccountNotFoundException.class, () -> erpService.searchEntity(accountId));
    }

    @Test
    void searchEntity_BadRequest() throws Exception {
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String accountId = "123";
        String getEclipseEntityUrl = String.format("%s/entitySearch/%s", eclipseServiceUrl, accountId);
        EntitySearchResult entitySearchResult = new EntitySearchResult();
        entitySearchResult.setId("123");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getEclipseEntityUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () -> erpService.searchEntity(accountId));
    }

    @Test
    void getEclipseAccount_Failure() throws Exception {
        String id = "123";

        mockRestServiceServer
            .expect(
                ExpectedCount.once(),
                requestTo(
                    "http://ecomm-dev-eclipse-service:8080/accounts/123?retrieveBillTo=true&retrieveShipToList=false"
                )
            )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(new ErpAccountInfo()))
            );

        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo("http://ecomm-dev-eclipse-service:8080/customers/123"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () -> erpService.getEclipseAccount(id, true, false));
    }

    @Test
    void recreateErpAccounts() throws Exception {
        UUID id = UUID.randomUUID();
        List<String> userEmails = new ArrayList<>();
        String userEmail = "example@Test.com";
        userEmails.add(userEmail);

        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(id);

        Set<ErpsUsers> setErpsUsers = new HashSet<>();
        ErpsUsers erpUser = new ErpsUsers();
        erpUser.setErpUserId("123");
        erpUser.setErp(ErpEnum.ECLIPSE);
        setErpsUsers.add(erpUser);
        user.setErpsUsers(setErpsUsers);

        Set<Account> setAccount = new HashSet<>();
        Account account = new Account();
        account.setErpAccountId("123");
        setAccount.add(account);
        user.setBillToAccounts(setAccount);

        users.add(user);
        String eclipseServiceUrl = "http://ecomm-dev-eclipse-service:8080";
        String mincronServiceUrl = " ";
        String getEclipseEntityUrl = String.format(
            "%s/entitySearch/%s",
            eclipseServiceUrl,
            user.getBillToAccounts().iterator().next().getErpAccountId()
        );
        EntitySearchResult entitySearchResult = new EntitySearchResult();
        entitySearchResult.setId("123");

        String createContactUrl = String.format(
            "%s/accounts/%s/user",
            erpUser.getErp() == ErpEnum.ECLIPSE ? eclipseServiceUrl : mincronServiceUrl,
            user.getBillToAccounts().iterator().next().getErpAccountId()
        );
        ErpContactCreationResponse erpResponse = new ErpContactCreationResponse();
        erpResponse.setErpUsername("Morsco");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(createContactUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(erpResponse))
            );
        when(userDao.findAllByEmailIn(userEmails)).thenReturn(users);
        erpService.recreateErpAccounts(userEmails);
        verify(userDao, times(1)).findAllByEmailIn(userEmails);
    }
}
