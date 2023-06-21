package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.AccountUserLoginUpdatedDTO;

import java.util.HashMap;

// Data extension can be found at the following location in SFMC:
// Data Extensions > Reece Ecomm Transactional Journeys > AccountUserEmailUpdatedDE
public class AccountUserLoginUpdatedDE extends BaseDataExtension {
    public AccountUserLoginUpdatedDE(String email, String supportEmail, String firstName, String lastName) {
        super.setTo(email);
        HashMap<String, String> attr = new HashMap<>();
        attr.put("SupportEmail", supportEmail);
        attr.put("FirstName", firstName);
        attr.put("LastName", lastName);
        super.setAttr(attr);
    }
}
