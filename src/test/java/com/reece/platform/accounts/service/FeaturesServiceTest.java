package com.reece.platform.accounts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.reece.platform.accounts.exception.FeatureNotFoundException;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.model.repository.FeaturesDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;

@SpringBootTest(classes = { FeaturesService.class })
@AutoConfigureMockMvc
public class FeaturesServiceTest {

    public static final UUID FEATURE_ID = UUID.randomUUID();

    @MockBean
    private FeaturesDAO featuresDAO;

    @Autowired
    private FeaturesService service;

    @BeforeEach
    public void setup() throws Exception {}

    @Test
    void setFeatureEnabled_FeatureNotFoundException() {
        when(featuresDAO.findById(any())).thenReturn(Optional.empty());
        assertThrows(FeatureNotFoundException.class, () -> service.setFeatureEnabled(FEATURE_ID, false));
    }

    @Test
    void setFeatureEnabled_statusChanged() {
        Feature feature = new Feature();
        feature.setId(FEATURE_ID);
        feature.setName("Feature name");
        feature.setIsEnabled(true);

        when(featuresDAO.findById(any())).thenReturn(Optional.of(feature));
        service.setFeatureEnabled(FEATURE_ID, false);

        assertTrue(feature.getIsEnabled().equals(false), "feature should now be toggled to false");
    }

    @Test
    void getFeatures_Success() {
        List<Feature> listFeature = new ArrayList<>();
        Feature feature = new Feature();
        listFeature.add(feature);
        when(featuresDAO.findAll()).thenReturn(listFeature);
        var response = service.getFeatures();
        assertEquals(response.size(), 1);
    }
}
