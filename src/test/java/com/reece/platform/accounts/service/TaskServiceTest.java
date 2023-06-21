package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.model.DTO.ErpAccountInfo;
import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.model.enums.FeaturesEnum;
import com.reece.platform.accounts.model.repository.AccountDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
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
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { TaskService.class, RestTemplate.class, ObjectMapper.class })
public class TaskServiceTest {

    @MockBean
    private AccountDAO accountDAO;

    @MockBean
    private ErpService erpService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        Feature feature = new Feature(UUID.randomUUID(), FeaturesEnum.WATERWORKS.name(), true);

        ReflectionTestUtils.setField(erpService, "eclipseServiceUrl", "http://ecomm-dev-eclipse-service:8080");
        ReflectionTestUtils.setField(erpService, "mincronServiceUrl", "http://ecomm-dev-mincron-service:8080");

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void refreshAccounts_success() throws Exception {
        String erpAccountId1 = "123";
        String erpAccountId2 = "321";
        Account account1 = new Account();
        account1.setErpAccountId(erpAccountId1);

        Account account2 = new Account();
        account2.setErpAccountId(erpAccountId2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        String companyName1 = "company name 1";
        String companyName2 = "company name 2";
        String street1 = "street 1";
        String city1 = "city 1";
        String state1 = "state 1";
        String street2 = "street 2";
        String city2 = "city 2";
        String state2 = "state 2";
        String zip1 = "zip 1";
        String zip2 = "zip 2";

        ErpAccountInfo erpAccountInfo1 = new ErpAccountInfo();
        erpAccountInfo1.setCompanyName(companyName1);
        erpAccountInfo1.setStreet1(street1);
        erpAccountInfo1.setState(state1);
        erpAccountInfo1.setCity(city1);
        erpAccountInfo1.setZip(zip1);
        ErpAccountInfo erpAccountInfo2 = new ErpAccountInfo();
        erpAccountInfo2.setCompanyName(companyName2);
        erpAccountInfo2.setStreet1(street2);
        erpAccountInfo2.setState(state2);
        erpAccountInfo2.setCity(city2);
        erpAccountInfo2.setZip(zip2);

        when(erpService.getEclipseAccount("123", false, false)).thenReturn(erpAccountInfo1);
        when(erpService.getEclipseAccount("321", false, false)).thenReturn(erpAccountInfo2);

        var response = taskService.getEclipseAccounts(accounts);

        verify(accountDAO, times(1))
            .save(
                argThat(account ->
                    account.getName().equals(companyName1) &&
                    account.getAddress().equals(String.format("%s %s, %s %s", street1, city1, state1, zip1))
                )
            );
        verify(accountDAO, times(1))
            .save(
                argThat(account ->
                    account.getName().equals(companyName2) &&
                    account.getAddress().equals(String.format("%s %s, %s %s", street2, city2, state2, zip2))
                )
            );
        assertNotNull(response);
    }

    @Test
    public void refreshAccounts_failure() throws Exception {
        String erpAccountId1 = "123";
        String erpAccountId2 = "321";
        Account account1 = new Account();
        account1.setErpAccountId(erpAccountId1);

        Account account2 = new Account();
        account2.setErpAccountId(erpAccountId2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        String companyName1 = "company name 1";
        String companyName2 = "company name 2";
        String street1 = "street 1";
        String city1 = "city 1";
        String state1 = "state 1";
        String street2 = "street 2";
        String city2 = "city 2";
        String state2 = "state 2";
        String zip1 = "zip 1";
        String zip2 = "zip 2";

        ErpAccountInfo erpAccountInfo1 = new ErpAccountInfo();
        erpAccountInfo1.setCompanyName(companyName1);
        erpAccountInfo1.setStreet1(street1);
        erpAccountInfo1.setState(state1);
        erpAccountInfo1.setCity(city1);
        erpAccountInfo1.setZip(zip1);
        ErpAccountInfo erpAccountInfo2 = new ErpAccountInfo();
        erpAccountInfo2.setCompanyName(companyName2);
        erpAccountInfo2.setStreet1(street2);
        erpAccountInfo2.setState(state2);
        erpAccountInfo2.setCity(city2);
        erpAccountInfo2.setZip(zip2);

        when(erpService.getEclipseAccount("123", false, false)).thenThrow(new NoSuchElementException());
        assertDoesNotThrow(() -> taskService.getEclipseAccounts(accounts));
    }
}
