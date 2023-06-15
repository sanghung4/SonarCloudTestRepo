package com.reece.platform.inventory.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.inventory.dto.metrics.MetricsOverviewDTO;
import com.reece.platform.inventory.dto.metrics.MetricsPercentageChangeDTO;
import com.reece.platform.inventory.dto.metrics.MetricsPercentageTotalDTO;
import com.reece.platform.inventory.dto.metrics.MetricsUsageDTO;
import com.reece.platform.inventory.service.MetricsService;
import java.time.LocalDate;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

class MetricsControllerTest {

    @Mock
    private MetricsService mockMetricsService;

    @Mock
    private JwtAuthenticationToken mockBearerTokenAuthentication;

    @InjectMocks
    private MetricsController metricsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        metricsController = new MetricsController(mockMetricsService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(metricsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    //TODO: Error authentication is null
    @Test
    void registerLogin() throws Exception {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "Testing");
        when(mockBearerTokenAuthentication.getTokenAttributes()).thenReturn(attributes);

        //        var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        //        HttpHeaders httpHeaders = new HttpHeaders();
        //        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        //        httpHeaders.add("X-Branch-Id", "test");
        //
        //        mockMvc.perform(post("/metrics/_login")
        //                        .headers(httpHeaders)
        //                )
        //                .andExpect(status().isNoContent())
        //                .andReturn();

        metricsController.registerLogin(mockBearerTokenAuthentication, "Testing");

        verify(mockBearerTokenAuthentication, times(1)).getTokenAttributes();
        verify(mockMetricsService, times(1)).registerLogin(anyString(), anyString());
    }

    @Test
    void getTotalOverview_shouldReturnStatus200() throws Exception {
        when(
            mockMetricsService.getTotalOverview(
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
            )
        )
            .thenReturn(new MetricsOverviewDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("startDateWeekOld", "2020-10-31");
        requestParams.add("endDateWeekOld", "2020-11-07");
        requestParams.add("startDateWeekNew", "2020-11-14");
        requestParams.add("endDateWeekNew", "2020-11-21");

        mockMvc.perform(get("/metrics/totalOverview").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockMetricsService, times(1))
            .getTotalOverview(any(LocalDate.class), any(LocalDate.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getTotalOverview_shouldReturnBadRequestStatus400() throws Exception {
        when(
            mockMetricsService.getTotalOverview(
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
            )
        )
            .thenReturn(new MetricsOverviewDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "2020-10-31");
        requestParams.add("endDateWeekOld", "2020-11-07");
        requestParams.add("startDateWeekNew", "2020-11-14");
        requestParams.add("endDateWeekNew", "2020-11-21");

        mockMvc
            .perform(get("/metrics/totalOverview").params(requestParams))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getTotalUsage_shouldReturnStatus200() throws Exception {
        when(mockMetricsService.getTotalUsage(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(new MetricsUsageDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("startDate", "2020-10-31");
        requestParams.add("endDate", "2020-11-07");

        mockMvc.perform(get("/metrics/totalUsage").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockMetricsService, times(1)).getTotalUsage(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getTotalUsage_shouldReturnBadRequestStatus400() throws Exception {
        when(mockMetricsService.getTotalUsage(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(new MetricsUsageDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "2020-10-31");
        requestParams.add("endDate", "2020-11-07");

        mockMvc
            .perform(get("/metrics/totalUsage").params(requestParams))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getPercentageChange_shouldReturnStatus200() throws Exception {
        when(
            mockMetricsService.getPercentageChange(
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
            )
        )
            .thenReturn(new MetricsPercentageChangeDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("startDateWeekOld", "2020-10-31");
        requestParams.add("endDateWeekOld", "2020-11-07");
        requestParams.add("startDateWeekNew", "2020-11-14");
        requestParams.add("endDateWeekNew", "2020-11-21");

        mockMvc.perform(get("/metrics/percentageChange").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockMetricsService, times(1))
            .getPercentageChange(
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
            );
    }

    @Test
    void getPercentageChange_shouldReturnBadRequestStatus400() throws Exception {
        when(
            mockMetricsService.getPercentageChange(
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
            )
        )
            .thenReturn(new MetricsPercentageChangeDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "2020-10-31");
        requestParams.add("endDateWeekOld", "2020-11-07");
        requestParams.add("startDateWeekNew", "2020-11-14");
        requestParams.add("endDateWeekNew", "2020-11-21");

        mockMvc
            .perform(get("/metrics/percentageChange").params(requestParams))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getPercentageTotal_shouldReturnStatus200() throws Exception {
        when(mockMetricsService.getPercentageTotal(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(new MetricsPercentageTotalDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("startDate", "2020-10-31");
        requestParams.add("endDate", "2020-11-07");

        mockMvc.perform(get("/metrics/percentageTotal").params(requestParams)).andExpect(status().isOk()).andReturn();

        verify(mockMetricsService, times(1)).getPercentageTotal(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getPercentageTotal_shouldReturnBadRequestStatus400() throws Exception {
        when(mockMetricsService.getPercentageTotal(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(new MetricsPercentageTotalDTO());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("Wrong For Testing", "2020-10-31");
        requestParams.add("endDate", "2020-11-07");

        mockMvc
            .perform(get("/metrics/percentageTotal").params(requestParams))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
