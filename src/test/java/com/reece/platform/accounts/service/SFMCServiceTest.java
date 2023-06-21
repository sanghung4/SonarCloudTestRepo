package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.exception.SalesForceException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.model.entity.AccountRequest;
import com.reece.platform.accounts.model.enums.ErpEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { SFMCService.class, RestTemplate.class, ObjectMapper.class })
public class SFMCServiceTest {

    @Autowired
    private SFMCService sFMCService;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(
            sFMCService,
            "sfmcAuthUrl",
            "https://mcfb9fzg7jt844x3fbb7r796n2l4.auth.marketingcloudapis.com"
        );
        ReflectionTestUtils.setField(
            sFMCService,
            "sfmcRestUrl",
            "https://mcfb9fzg7jt844x3fbb7r796n2l4.rest.marketingcloudapis.com"
        );
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getToken_Success() throws Exception {
        SFMCResponseToken token = new SFMCResponseToken();
        token.setAccess_token("Bearer test token");
        var accountRequest = new AccountRequest();
        accountRequest.setErp(ErpEnum.ECLIPSE);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(token))
            );

        sFMCService.getToken();
    }

    @Test
    void getToken_Failure() {
        SMFCResponseDTO SMFCResponseDTO = new SMFCResponseDTO();
        SMFCResponseDTO.setOperationID(650250);
        SMFCResponseDTO.setOperationInitiated(true);

        var accountRequest = new AccountRequest();
        accountRequest.setErp(ErpEnum.ECLIPSE);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON));
        assertThrows(SalesForceException.class, () -> sFMCService.getToken());
    }

    @Test
    void deleteContacts_Success() {
        List<String> contactKeys = new ArrayList<>();
        contactKeys.add("TEST_317-531-5555");
        contactKeys.add("TEST_317-531-5556");

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
        sFMCService.deleteContacts(contactKeys, "Bearer test token");
    }

    @Test
    void deleteContacts_Failure() {
        List<String> contactKeys = new ArrayList<>();
        contactKeys.add("TEST_317-531-5555");
        contactKeys.add("TEST_317-531-5556");

        SMFCResponseDTO SMFCResponseDTO = new SMFCResponseDTO();
        SMFCResponseDTO.setOperationID(650250);
        SMFCResponseDTO.setOperationInitiated(true);

        var accountRequest = new AccountRequest();
        accountRequest.setErp(ErpEnum.ECLIPSE);

        mockRestServiceServer
            .expect(ExpectedCount.once(), anything())
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON));
        assertThrows(SalesForceException.class, () -> sFMCService.deleteContacts(contactKeys, "Bearer test token"));
    }
}
