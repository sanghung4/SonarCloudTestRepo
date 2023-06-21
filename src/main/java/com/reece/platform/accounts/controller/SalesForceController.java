package com.reece.platform.accounts.controller;

import com.okta.sdk.resource.ResourceException;
import com.reece.platform.accounts.exception.*;
import com.reece.platform.accounts.model.DTO.SFMCResponseToken;
import com.reece.platform.accounts.model.DTO.SMFCResponseDTO;
import com.reece.platform.accounts.service.SFMCService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/salesforce-account")
public class SalesForceController {

    @Autowired
    private SFMCService salesForceService;

    /**
     * Delete Contact
     */
    @PostMapping("/delete-contacts")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<SMFCResponseDTO> deleteContacts(@RequestBody List<String> contactKeys)
        throws ResourceException, HttpClientErrorException, SalesForceException {
        SFMCResponseToken token = salesForceService.getToken();

        SMFCResponseDTO SMFCResponseDTO = salesForceService.deleteContacts(contactKeys, token.getAccess_token());

        return ResponseEntity.ok(SMFCResponseDTO);
    }
}
