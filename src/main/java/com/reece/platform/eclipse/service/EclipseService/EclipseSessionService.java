package com.reece.platform.eclipse.service.EclipseService;

import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.DTO.EclipseRestSessionDTO;
import com.reece.platform.eclipse.model.XML.LoginSubmit.LoginResponse;
import com.reece.platform.eclipse.model.XML.LoginSubmit.LoginSubmitRequest;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.utils.ImmutableMap;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EclipseSessionService extends BaseEclipseService {

    public EclipseSessionService(@Qualifier("xml") RestTemplate xml, @Qualifier("json") RestTemplate json){
        this.restTemplateXML = xml;
        this.restTemplate = json;
    }

    public LoginResponse getSessionKey(String accountId) throws EclipseTokenException {
        LoginSubmitRequest loginRequest = new LoginSubmitRequest(accountId, loginId, password);
        return sendRequest(
                loginRequest,
                LoginResponse.class).orElseThrow(EclipseTokenException::new);
    }

    public LoginResponse getSessionKey(String loginId, String password) throws EclipseTokenException {
        LoginSubmitRequest loginRequest = new LoginSubmitRequest(loginId, password);
        return sendRequest(
                loginRequest,
                LoginResponse.class).orElseThrow(EclipseTokenException::new);
    }

    @Cacheable("classic-eclipse-session")
    public Security getRequestSecurity(String accountId, String userLoginId, String userPassword, boolean isEmployee) throws EclipseTokenException {
        Security security = new Security();
        if (isEmployee) {
            security.setSessionId(getSessionKey(accountId).getLoginSubmitResponse().getSessionId());
        } else {
            security.setSessionId(getSessionKey(userLoginId, userPassword).getLoginSubmitResponse().getSessionId());
        }
        return security;
    }

    /**
     * Get session token to be used to reach other Eclipse API endpoints.
     * @return session token
     */
    @Cacheable("rest-api-session-token")
    public Optional<EclipseRestSessionDTO> getSessionToken() {
        Map<String, String> eclipseApiCredentials = ImmutableMap.of(
                "username", apiLoginId,
                "password", apiPassword
        );
        HttpEntity<Map<String,String>> request = new HttpEntity<>(eclipseApiCredentials);
        ResponseEntity<EclipseRestSessionDTO> response =
                restTemplate.postForEntity(eclipseApiEndpoint + "/Sessions", request, EclipseRestSessionDTO.class);
        return Optional.ofNullable(response.getBody());
    }
}
