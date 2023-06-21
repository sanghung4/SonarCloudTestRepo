package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.FeatureNotFoundException;
import com.reece.platform.accounts.model.entity.Feature;
import com.reece.platform.accounts.model.repository.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FeaturesService {

    @Autowired
    private FeaturesDAO featuresDAO;

    /**
     * Get all features and their respective statuses.
     * @return all features from features table
     */
    public List<Feature> getFeatures() {
        return featuresDAO.findAll();
    }

    /**
     * Turns feature on or off.
     * @param featureId id for feature to update
     * @param isEnabled
     */
    @Transactional
    public void setFeatureEnabled(UUID featureId, Boolean isEnabled) {
        val feature = featuresDAO
            .findById(featureId)
            .orElseThrow(() -> new FeatureNotFoundException(featureId.toString()));
        feature.setIsEnabled(isEnabled);
        featuresDAO.save(feature);
    }
}
