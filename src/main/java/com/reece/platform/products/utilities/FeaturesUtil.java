package com.reece.platform.products.utilities;

import com.reece.platform.products.model.DTO.FeatureDTO;
import com.reece.platform.products.model.FeaturesEnum;
import java.util.List;

public class FeaturesUtil {

    /**
     * Determines if the given list of features contains WATERWORKS feature and if that feature is enabled.
     *
     * @param featureDTOList list of all available features
     * @return boolean indicating whether WATERWORKS feature is enabled in given list
     */
    public static boolean isWaterworksEnabled(List<FeatureDTO> featureDTOList) {
        return featureDTOList
            .stream()
            .anyMatch(
                (featureDTO -> featureDTO.getName().equals(FeaturesEnum.WATERWORKS.name()) && featureDTO.getIsEnabled())
            );
    }

    public static boolean isWorkdayEnabled(List<FeatureDTO> features) {
        return features
            .stream()
            .anyMatch(
                (featureDTO -> featureDTO.getName().equals(FeaturesEnum.WORKDAY.name()) && featureDTO.getIsEnabled())
            );
    }

    public static boolean isPostEnabled(List<FeatureDTO> features) {
        return features
            .stream()
            .anyMatch(
                (
                    featureDTO ->
                        featureDTO.getName().equals(FeaturesEnum.POST_SEARCH_API.name()) && featureDTO.getIsEnabled()
                )
            );
    }

    public static boolean isEHGEnabled(List<FeatureDTO> features) {
        return features
            .stream()
            .anyMatch(
                (
                    featureDTO ->
                        featureDTO.getName().equals(FeaturesEnum.EXPRESSIONSHOMEGALLERY.name()) &&
                        featureDTO.getIsEnabled()
                )
            );
    }

    public static boolean isCartPricingAvailabilityEnabled(List<FeatureDTO> features) {
        return features
            .stream()
            .anyMatch(
                (
                    featureDTO ->
                        featureDTO.getName().equals(FeaturesEnum.CART_PRICING_AVAIL.name()) && featureDTO.getIsEnabled()
                )
            );
    }
}
