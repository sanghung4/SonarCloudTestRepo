package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.FeatureDTO;
import com.reece.platform.accounts.model.entity.*;
import com.reece.platform.accounts.service.*;
import com.reece.platform.accounts.utilities.DecodedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller()
@RequestMapping("/features")
public class FeaturesController {

    private final FeaturesService featuresService;
    private final AuthenticationService authenticationService;

    @Autowired
    public FeaturesController (
            FeaturesService featuresService,
            AuthenticationService authenticationService

    ) {
        this.featuresService = featuresService;
        this.authenticationService = authenticationService;
    }


    /**
     * Gets the status of feature toggles.
     * @return statuses for features
     */
    @GetMapping
    public ResponseEntity<List<Feature>> getFeatures() {
        List<Feature> features = featuresService.getFeatures();
        return new ResponseEntity<>(features, HttpStatus.OK);
    }

    /**
     * Turns feature on or off.
     * @param authorization
     * @param featureId id for feature to update
     * @return string that confirms the features new status
     * @throws FeatureNotFoundException
     */
    /**
     * Turns feature on or off.
     * @param authorization
     * @param featureId id for feature to update
     * @param featureDTO request body that contains boolean flag
     * @return HTTP status
     */
    @PutMapping("{featureId}")
    public ResponseEntity<Boolean> setFeatureEnabled(
            @RequestHeader(required = true) String authorization,
            @PathVariable UUID featureId,
            @RequestBody FeatureDTO featureDTO) throws UserUnauthorizedException {
        DecodedToken token = DecodedToken.getDecodedHeader(authorization);
        if (!authenticationService.userCanToggleFeatures(token)) {
            throw new UserUnauthorizedException();
        }
        featuresService.setFeatureEnabled(featureId, featureDTO.getIsEnabled());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
