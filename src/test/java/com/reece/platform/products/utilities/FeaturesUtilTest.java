package com.reece.platform.products.utilities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.reece.platform.products.model.DTO.FeatureDTO;
import com.reece.platform.products.model.FeaturesEnum;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FeaturesUtilTest {

    @Test
    void isWaterworksEnabled_true() {
        List<FeatureDTO> features = new ArrayList<>();
        FeatureDTO waterworksFeature = new FeatureDTO();
        waterworksFeature.setIsEnabled(true);
        waterworksFeature.setName(FeaturesEnum.WATERWORKS.name());
        features.add(waterworksFeature);
        assertTrue(FeaturesUtil.isWaterworksEnabled(features));
    }

    @Test
    void isWaterworksEnabled_false() {
        List<FeatureDTO> features = new ArrayList<>();
        FeatureDTO waterworksFeature = new FeatureDTO();
        waterworksFeature.setIsEnabled(false);
        waterworksFeature.setName(FeaturesEnum.WATERWORKS.name());
        features.add(waterworksFeature);
        assertFalse(FeaturesUtil.isWaterworksEnabled(features));
    }

    @Test
    void isWaterworksEnabled_noWaterworks() {
        List<FeatureDTO> features = new ArrayList<>();
        FeatureDTO otherFeature = new FeatureDTO();
        otherFeature.setIsEnabled(true);
        otherFeature.setName(FeaturesEnum.CHECKOUT_WITH_CARD.name());
        features.add(otherFeature);
        assertFalse(FeaturesUtil.isWaterworksEnabled(features));
    }
}
