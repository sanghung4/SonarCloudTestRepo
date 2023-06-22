package com.reece.platform.mincron.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.as400.data.PcmlException;
import com.reece.platform.mincron.exceptions.AccountNotFoundException;
import com.reece.platform.mincron.exceptions.InvalidPhoneNumberException;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.reece.platform.mincron.service.MincronService;

import java.util.List;

@Controller()
public class MincronController {

    @Autowired
    public MincronController(MincronService mincronService) {
        this.mincronService = mincronService;
    }

    private final MincronService mincronService;

    @GetMapping("hello")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

    /**
     * @param accountId ID of account to fetch.
     * @return the requested account
     * @throws PcmlException            if there is an error response from Mincron
     * @throws AccountNotFoundException if no account was found in Mincron
     */
    @GetMapping("/accounts/{accountId}")
    public @ResponseBody ResponseEntity<GetAccountResponseDTO> getAccount(
        @PathVariable String accountId,
        @RequestParam(required = false, defaultValue = "false") Boolean retrieveShipToList
    ) throws PcmlException, AccountNotFoundException, JsonProcessingException {
        return new ResponseEntity<GetAccountResponseDTO>(mincronService.getAccount(accountId, retrieveShipToList), HttpStatus.OK);
    }

    // This is unused by the application but was added for debug purposes
    @GetMapping("/accounts/{accountId}/users")
    public @ResponseBody ResponseEntity<List<ContactResponseDTO>> getContacts(@PathVariable String accountId) {
        return new ResponseEntity<>(mincronService.getContacts(accountId), HttpStatus.OK);
    }

    @PostMapping("accounts/{accountId}/user")
    public @ResponseBody ResponseEntity<CreateContactResponseDTO> createContact(@PathVariable String accountId, @RequestBody CreateContactRequestDTO createContactRequestDTO) throws InvalidPhoneNumberException, JsonProcessingException, PcmlException, AccountNotFoundException {
        CreateContactResponseDTO response = mincronService.createContact(accountId, createContactRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("accounts/{accountId}/user/{userId}")
    public @ResponseBody ResponseEntity<EditContactResponseDTO> updateContact(@PathVariable String accountId, @PathVariable String userId, @RequestBody EditContactRequestDTO editContactRequestDTO) throws InvalidPhoneNumberException {
        EditContactResponseDTO response = mincronService.updateContact(accountId, userId, editContactRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("accounts/{accountId}/user/{userId}")
    public @ResponseBody ResponseEntity<DeleteContactResponseDTO> deleteContact(@PathVariable String accountId, @PathVariable String userId) {
        DeleteContactResponseDTO response = mincronService.deleteContact(accountId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
