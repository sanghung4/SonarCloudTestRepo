package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.CustomerAccountDeletedDTO;

import java.util.HashMap;

// Data extension can be found at the following location in SFMC:
// Data Extensions > Reece Ecomm Transactional Journeys > CustomerAccountDeletedDE
public class CustomerAccountDeletedDE extends BaseDataExtension {
    public CustomerAccountDeletedDE(CustomerAccountDeletedDTO customerAccountDeletedDTO) {
        super.setTo(customerAccountDeletedDTO.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("AccountNumber", customerAccountDeletedDTO.getAccountNumber());
        attr.put("FirstName", customerAccountDeletedDTO.getFirstName());
        attr.put("LastName", customerAccountDeletedDTO.getLastName());
        attr.put("CompanyName", customerAccountDeletedDTO.getCompanyName());
        super.setAttr(attr);
    }
}
