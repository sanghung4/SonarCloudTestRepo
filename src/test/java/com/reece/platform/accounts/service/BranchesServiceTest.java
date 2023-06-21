package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.accounts.exception.AccountNotFoundException;
import com.reece.platform.accounts.exception.BranchNotFoundException;
import com.reece.platform.accounts.model.DTO.BranchDTO;
import com.reece.platform.accounts.model.DTO.DeliveriesDeleteResponseDTO;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.model.enums.ErpEnum;
import com.reece.platform.accounts.model.enums.FeaturesEnum;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { BranchService.class, RestTemplate.class, ObjectMapper.class })
public class BranchesServiceTest {

    @Autowired
    private BranchService branchService;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        Feature feature = new Feature(UUID.randomUUID(), FeaturesEnum.WATERWORKS.name(), true);

        ReflectionTestUtils.setField(branchService, "productServiceUrl", "http://ecomm-dev-branches-service:8080");
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getBranch_success() throws Exception {
        String productServiceUrl = "http://ecomm-dev-branches-service:8080";
        String branchId = "123";
        String getBranchUrl = String.format("%s/branches/%s", productServiceUrl, branchId);
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId("123");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getBranchUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(branchDTO))
            );

        var response = branchService.getBranch(branchId);
        assertEquals(response.getBranchId(), "123");
    }

    @Test
    void getBranch_Failure() {
        String productServiceUrl = "http://ecomm-dev-branches-service:8080";
        String branchId = "87878";
        String getBranchUrl = String.format("%s/branches/%s", productServiceUrl, branchId);
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId("123345");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getBranchUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND));
        assertThrows(BranchNotFoundException.class, () -> branchService.getBranch(branchId));
    }

    @Test
    void getBranch_Bad_Request() {
        String productServiceUrl = "http://ecomm-dev-branches-service:8080";
        String branchId = "87878";
        String getBranchUrl = String.format("%s/branches/%s", productServiceUrl, branchId);
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchId("123345");
        mockRestServiceServer
            .expect(ExpectedCount.once(), requestTo(getBranchUrl))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> branchService.getBranch(branchId));
    }
}
