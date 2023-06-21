package com.reece.platform.accounts.controller;

import com.reece.platform.accounts.exception.AccountNotFoundException;
import com.reece.platform.accounts.exception.CardInUseException;
import com.reece.platform.accounts.model.DTO.*;
import com.reece.platform.accounts.service.ErpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/credit-card")
public class CreditCardController {

    private final ErpService erpService;

    @Autowired
    public CreditCardController(
        ErpService erpService
    ) {
        this.erpService = erpService;
    }

    /**
     * Get credit card list elements
     *
     * @param accountId
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    @GetMapping("/{accountId}")
    public @ResponseBody ResponseEntity<CreditCardListResponseDTO> getCreditCardList(@PathVariable String accountId) throws AccountNotFoundException {
        CreditCardListResponseDTO queryResponseDTO = erpService.getCreditCardList(accountId);
        return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
    }

    /**
     * Update account with credit card list
     *
     * @param accountId
     * @param updateSubmitRequestDTO
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    @PostMapping("/{accountId}")
    public @ResponseBody ResponseEntity<EntityUpdateSubmitResponseDTO> addCreditCard(@PathVariable String accountId, @RequestBody EntityUpdateSubmitRequestDTO updateSubmitRequestDTO) throws AccountNotFoundException {
        EntityUpdateSubmitResponseDTO creditCardListInfo = erpService.updateCreditCardList(accountId, updateSubmitRequestDTO);
        return new ResponseEntity<>(creditCardListInfo, HttpStatus.OK);
    }

    /**
     * Update account with credit card list
     *
     * @param accountId
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    @DeleteMapping("/{accountId}/{creditCardId}")
    public ResponseEntity<String> deleteCreditCard(@PathVariable String accountId, @PathVariable String creditCardId) throws AccountNotFoundException, CardInUseException {
        erpService.deleteCreditCard(accountId, creditCardId);
        return new ResponseEntity<>(creditCardId, HttpStatus.OK);
    }

    /**
     * Get credit card setup URL for iframe
     *
     * @param accountId
     * @param elementSetupUrlDTO
     *
     * @return ElementSetUpUrlResponseDTO
     * @throws AccountNotFoundException
     */
    @PostMapping("/{accountId}/setup-url")
    public @ResponseBody ResponseEntity<ElementSetUpUrlResponseDTO> getCreditCardSetupURL(@PathVariable String accountId, @RequestBody ElementSetupUrlDTO elementSetupUrlDTO) throws AccountNotFoundException {
        ElementSetUpUrlResponseDTO responseDTO = erpService.getCreditCardSetupUrl(accountId, elementSetupUrlDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * Get credit card information entered into WorldPay
     *
     * @param accountId
     * @param elementSetupId
     *
     * @return ElementSetupQueryResponseDTO
     * @throws AccountNotFoundException
     */
    @GetMapping("/{accountId}/info/{elementSetupId}")
    public @ResponseBody ResponseEntity<ElementSetupQueryResponseDTO> getCreditCardElementInfo(@PathVariable String accountId, @PathVariable String elementSetupId) throws AccountNotFoundException {
        ElementSetupQueryResponseDTO queryResponseDTO = erpService.getCreditCardElementInfo(accountId, elementSetupId);
        return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
    }

}
