package com.reece.platform.accounts.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RemoveDuplicateAccountsDTO {
    private Map<String, Integer> erpAccountIdToNumberOfDupesMap;
    private List<String> userEmailsMoved;
}
