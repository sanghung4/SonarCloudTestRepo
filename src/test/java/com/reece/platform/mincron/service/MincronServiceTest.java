package com.reece.platform.mincron.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ConnectionPool;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import com.reece.platform.mincron.exceptions.AccountNotFoundException;
import com.reece.platform.mincron.exceptions.InvalidPhoneNumberException;
import com.reece.platform.mincron.model.*;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Array;
import java.util.*;

import static com.reece.platform.mincron.service.PooledMincronConnection.PROGRAM_CALL_DOCUMENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MincronServiceTest {

    private MincronService service;

    private ProgramCallDocumentFactory factory;
    private ProgramCallDocument pcd;
    private RestTemplate restTemplate;
    private PooledMincronConnection connection;

    private static final String PASSWORD = "Password1";
    private static final String EXISTING_ACCOUNT = "123";
    private static final String EXISTING_USER = "123";
    private static final String INVALID_ACCOUNT = "321";
    private static final String CUSTOMER_NAME = "Customer name";
    private static final String FIRST_NAME = "Customer";
    private static final String LAST_NAME = "name";
    private static final String ADDRESS_1 = "add 1";
    private static final String ADDRESS_2 = "add 2";
    private static final String ADDRESS_3 = "add 3";
    private static final String CITY = "City";
    private static final String STATE = "State";
    private static final String ZIP = "11111";
    private static final String AREA_CODE = "123";
    private static final String PREFIX = "123";
    private static final String SUFFIX = "1235";
    private static final String EMAIL = "email@email.com";
    private static final String PHONE_NUMBER = "1231231235";
    private static final String FORMATTED_PHONE_NUMBER = "123-123-1235";
    private static final String MINCRON_ACCOUNT_NOT_FOUND_MSG = "Invalid Account ID.";

    @BeforeEach
    public void setup() {
        pcd = mock(ProgramCallDocument.class);
        factory = mock(ProgramCallDocumentFactory.class);
        restTemplate = mock(RestTemplate.class);
        service = new MincronService(restTemplate, factory);
        connection = mock(PooledMincronConnection.class);
    }

    @Test
    public void createContact_success() throws InvalidPhoneNumberException, JsonProcessingException, AccountNotFoundException, PcmlException {
        CreateContactRequestDTO request = new CreateContactRequestDTO();
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setTelephone(PHONE_NUMBER);

        Map<String, Object> accountResult = new HashMap<>();
        accountResult.put("CUSTNAME", CUSTOMER_NAME);
        accountResult.put("PIUSERNAME", EXISTING_ACCOUNT);
        accountResult.put("PIUSERID", EXISTING_USER);
        accountResult.put("PIPWD", PASSWORD);
        accountResult.put("ADDRLINE1", ADDRESS_1);
        accountResult.put("ADDRLINE2", ADDRESS_2);
        accountResult.put("ADDRLINE3", ADDRESS_3);
        accountResult.put("CITY", CITY);
        accountResult.put("STATE", STATE);
        accountResult.put("ZIP", ZIP);
        accountResult.put("AREACODE", AREA_CODE);
        accountResult.put("PREFIX", PREFIX);
        accountResult.put("SUFFIX", SUFFIX);

        ResponseEntity jobAccountResponse = new ResponseEntity("{ \"returnTable\": { \"customerJobs\": []}}", HttpStatus.OK);
        when(factory.getPooledMincronConnection()).thenReturn(connection);
        when(connection.callProgram(anyString(), any(), any())).thenReturn(accountResult);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(jobAccountResponse);

        var response = service.createContact(EXISTING_ACCOUNT, request);

        assertEquals(response.getErpUsername(), EXISTING_ACCOUNT);
        assertEquals(response.getContactId(), EXISTING_USER);
        assertEquals(response.getErpPassword(), PASSWORD);
    }

    @Test
    public void deleteContact_success() {
        Map<String, Object> result = new HashMap<>();
        result.put("PIACCOUNTID", EXISTING_ACCOUNT);
        result.put("PIUSERID", EXISTING_USER);
        when(factory.getPooledMincronConnection()).thenReturn(connection);
        when(connection.callProgram(anyString(), any(), any(), any())).thenReturn(result);
        DeleteContactResponseDTO responseDTO = service.deleteContact(EXISTING_ACCOUNT, EXISTING_USER);

        assertTrue(responseDTO.getErpAccountId().equals(EXISTING_ACCOUNT));
        assertTrue(responseDTO.getErpUserId().equals(EXISTING_USER));
    }

    @Test
    public void updateContact_success() throws Exception {
        EditContactRequestDTO editContactRequestDTO = new EditContactRequestDTO();
        editContactRequestDTO.setFirstName(FIRST_NAME);
        editContactRequestDTO.setLastName(LAST_NAME);
        editContactRequestDTO.setEmail(EMAIL);
        editContactRequestDTO.setPhoneNumber(PHONE_NUMBER);

        Map<String, Object> result = new HashMap<>();
        result.put("PIUSERNAME", CUSTOMER_NAME);
        result.put("PIAREACODE", PHONE_NUMBER.substring(0,3));
        result.put("PIPREFIX", PHONE_NUMBER.substring(3,6));
        result.put("PISUFFIX", PHONE_NUMBER.substring(6,10));
        result.put("PIEMAIL", EMAIL);
        when(factory.getPooledMincronConnection()).thenReturn(connection);
        when(connection.callProgram(anyString(), any(), any())).thenReturn(result);

        EditContactResponseDTO responseDTO = service.updateContact("123", "123", editContactRequestDTO);

        assertTrue(responseDTO.getFirstName().equals(FIRST_NAME));
        assertTrue(responseDTO.getLastName().equals(LAST_NAME));
        assertTrue(responseDTO.getPhoneNumber().equals(FORMATTED_PHONE_NUMBER));
        assertTrue(responseDTO.getEmail().equals(EMAIL));
    }

    @Test
    public void getAccount_success() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("CUSTNAME", CUSTOMER_NAME);
        result.put("ADDRLINE1", ADDRESS_1);
        result.put("ADDRLINE2", ADDRESS_2);
        result.put("ADDRLINE3", ADDRESS_3);
        result.put("CITY", CITY);
        result.put("STATE", STATE);
        result.put("ZIP", ZIP);
        result.put("AREACODE", AREA_CODE);
        result.put("PREFIX", PREFIX);
        result.put("SUFFIX", SUFFIX);
        when(factory.getPooledMincronConnection()).thenReturn(connection);
        when(connection.callProgram(anyString(), any(), any())).thenReturn(result);

        GetAccountResponseDTO getAccountResponseDTO = service.getAccount(EXISTING_ACCOUNT, false);

        assertEquals(getAccountResponseDTO.getErpAccountId(), EXISTING_ACCOUNT, "Expected account id of dto returned to be equal to customer id");
        assertEquals(getAccountResponseDTO.getStreet1(), ADDRESS_1, "Expected address1 of dto returned to be equal to mocked data");
        assertEquals(getAccountResponseDTO.getStreet2(), ADDRESS_2 + " " + ADDRESS_3, "Expected address2 of dto returned to be equal to mocked data");
        assertEquals(getAccountResponseDTO.getCity(), CITY, "Expected city of dto returned to be equal to mocked data");
        assertEquals(getAccountResponseDTO.getState(), STATE, "Expected state of dto returned to be equal to mocked data");
        assertEquals(getAccountResponseDTO.getZip(), ZIP, "Expected zip of dto returned to be equal to mocked data");
        assertEquals(getAccountResponseDTO.getPhoneNumber(), FORMATTED_PHONE_NUMBER, "Expected phone of dto returned to be equal to mocked data");
    }

    @Test
    public void getAccount_notFound() {
        Map<String, Object> result = new HashMap<>();
        result.put("CUSTNAME", MINCRON_ACCOUNT_NOT_FOUND_MSG);
        when(factory.getPooledMincronConnection()).thenReturn(connection);
        when(connection.callProgram(anyString(), any(), any())).thenReturn(result);
        assertThrows(AccountNotFoundException.class, () -> service.getAccount(INVALID_ACCOUNT, false));
    }
}

