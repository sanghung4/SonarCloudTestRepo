package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ErpAccountsDTO {
    public ErpAccountsDTO() {}

    public ErpAccountsDTO(GetAccountResponseDTO getAccountResponseDTO) {
        this.billTo = new ErpAccountInfoDTO(getAccountResponseDTO);
        if (getAccountResponseDTO.getShipToAccounts() != null) {
            this.shipTo = getAccountResponseDTO.getShipToAccounts().stream()
                .map(account -> new ErpAccountInfoDTO(account)).collect(Collectors.toList());
        }
    }

    private ErpAccountInfoDTO billTo;
    private List<ErpAccountInfoDTO> shipTo;
}
