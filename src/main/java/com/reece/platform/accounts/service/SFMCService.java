package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.SalesForceException;
import com.reece.platform.accounts.model.DTO.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class SFMCService {

    @Value("${sfmcAuthUrl}")
    private String sfmcAuthUrl;

    @Value("${sfmcRestUrl}")
    private String sfmcRestUrl;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${accountId}")
    private String accountId;

    private final String grantType = "client_credentials";

    private final String deleteOperationType = "ContactsAndAttributes";

    @Autowired
    private RestTemplate restTemplate;

    public SFMCResponseToken getToken() throws SalesForceException {
        SFMCTokenRequestDTO sMFCTokenRequestDTO = new SFMCTokenRequestDTO();
        sMFCTokenRequestDTO.setGrant_type(grantType);
        sMFCTokenRequestDTO.setClient_id(clientId);
        sMFCTokenRequestDTO.setClient_secret(clientSecret);
        sMFCTokenRequestDTO.setAccount_id(accountId);

        HttpEntity<SFMCTokenRequestDTO> request = new HttpEntity<>(sMFCTokenRequestDTO);
        String tokenUrl = String.format("%s/v2/token", sfmcAuthUrl);

        try {
            ResponseEntity<SFMCResponseToken> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                SFMCResponseToken.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new SalesForceException(
                "There was a problem in getting access token: Invalid Credential.",
                e.getStatusCode()
            );
        }
    }

    @Transactional
    public SMFCResponseDTO deleteContacts(List<String> contactKeys, String token) throws SalesForceException {
        SFMCDeleteContactsRequestDTO sFMCDeleteContactsRequestDTO = new SFMCDeleteContactsRequestDTO();
        sFMCDeleteContactsRequestDTO.setValues(contactKeys);
        sFMCDeleteContactsRequestDTO.setDelete_operation_type(deleteOperationType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<SFMCDeleteContactsRequestDTO> request = new HttpEntity<>(sFMCDeleteContactsRequestDTO, headers);
        String deleteContactUrl = String.format("%s/contacts/v1/contacts/actions/delete?type=keys", sfmcRestUrl);

        try {
            ResponseEntity<SMFCResponseDTO> response = restTemplate.postForEntity(
                deleteContactUrl,
                request,
                SMFCResponseDTO.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new SalesForceException(
                "There was a problem in deleting the Contacts: No Contact deleted from SalesForce",
                e.getStatusCode()
            );
        }
    }
}
