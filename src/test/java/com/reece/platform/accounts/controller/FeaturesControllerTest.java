package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.exception.UserUnauthorizedException;
import com.reece.platform.accounts.model.DTO.FeatureDTO;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.service.AuthenticationService;
import com.reece.platform.accounts.service.FeaturesService;
import com.reece.platform.accounts.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes={FeaturesController.class})
@AutoConfigureMockMvc
public class FeaturesControllerTest {

    public static final UUID FEATURE_ID = UUID.randomUUID();

    @MockBean
    private FeaturesService featuresService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private FeaturesController controller;

    @Test
    void getFeatures_success() {
        List<Feature> features = Arrays.asList();
        when(featuresService.getFeatures()).thenReturn(features);
        ResponseEntity<List<Feature>> response = controller.getFeatures();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void setFeatureEnabled_success() throws Exception{
        doNothing().when(featuresService).setFeatureEnabled(any(), any());
        when(authenticationService.userCanToggleFeatures(any())).thenReturn(true);
        ResponseEntity<Boolean> response = controller.setFeatureEnabled("Bearer " + Constants.authToken(""), FEATURE_ID, new FeatureDTO());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void setFeatureEnabled_userUnauthorizedException() throws Exception{
        when(authenticationService.userCanToggleFeatures(any())).thenReturn(false);
        assertThrows(UserUnauthorizedException.class, () -> controller.setFeatureEnabled("Bearer " + Constants.authToken(""), FEATURE_ID, new FeatureDTO()));
    }
}
