package com.reece.platform.eclipse.service;

import com.reece.platform.eclipse.dto.EclipseRestSessionDTO;
import com.reece.platform.eclipse.dto.inventory.EclipseCredentials;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EclipseSessionService extends BaseEclipseService {

    /**
     * Get session token to be used to reach other Eclipse API endpoints.
     * @return session token
     */
    @Cacheable("rest-api-session-token")
    public Optional<EclipseRestSessionDTO> getSessionToken() {
        Map<String, String> eclipseApiCredentials = Map.of("username", apiLoginId, "password", apiPassword);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(eclipseApiCredentials);
        ResponseEntity<EclipseRestSessionDTO> response = restTemplate.postForEntity(
            eclipseApiEndpoint + "/Sessions",
            request,
            EclipseRestSessionDTO.class
        );
        return Optional.ofNullable(response.getBody());
    }

    /**
     * Used to verify users credentials in Picking App
     * @param credentials
     * @return
     */
    public Optional<EclipseRestSessionDTO> verifyCredentials(EclipseCredentials credentials) {
        Map<String, String> eclipseApiCredentials = Map.of(
            "username",
            credentials.getUsername(),
            "password",
            credentials.getPassword()
        );
        HttpEntity<Map<String, String>> request = new HttpEntity<>(eclipseApiCredentials);
        ResponseEntity<EclipseRestSessionDTO> response = restTemplate.postForEntity(
            eclipseApiEndpoint + "/Sessions",
            request,
            EclipseRestSessionDTO.class
        );
        return Optional.ofNullable(response.getBody());
    }
}
