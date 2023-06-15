package com.reece.platform.inventory.controller;

import com.reece.platform.inventory.service.CountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

class CountsControllerTest {

    @Mock
    private CountsService mockCountsService;

    @InjectMocks
    private CountsController countsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        countsController = new CountsController(mockCountsService);
        mockMvc =
            MockMvcBuilders
                .standaloneSetup(countsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void getCount() {
        //TODO: Auth Token coming back null
    }

    @Test
    void getLocations() {
        //TODO: Auth Token coming back null
    }

    @Test
    void getLocation() {
        //TODO: Auth Token coming back null
    }

    @Test
    void getNextLocation() {
        //TODO: Auth Token coming back null
    }

    @Test
    void updateCount() {
        //TODO: Auth Token coming back null
    }

    @Test
    void commitLocation() {
        //TODO: Auth Token coming back null
    }

    @Test
    void addToCount() {
        //TODO: Auth Token coming back null
    }

    @Test
    void updateMetrics() {
        //TODO: Auth Token coming back null
    }

    @Test
    void getCountsStatus() {}
}
