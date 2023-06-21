package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.NewCustomerNotificationDTO;

import java.util.HashMap;

public class AccountRequestDE extends BaseDataExtension {
    public AccountRequestDE(NewCustomerNotificationDTO newCustomerNotificationDTO) {
        super.setTo(newCustomerNotificationDTO.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("ManagerFirstName", newCustomerNotificationDTO.getManagerFirstName());
        attr.put("RequestFirstName", newCustomerNotificationDTO.getRequestFirstName());
        attr.put("RequestLastName", newCustomerNotificationDTO.getRequestLastName());
        attr.put("BillToName", newCustomerNotificationDTO.getBillToName());
        attr.put("RequestEmail", newCustomerNotificationDTO.getRequestEmail());
        attr.put("Domain", newCustomerNotificationDTO.getDomain());
        attr.put("Brand", newCustomerNotificationDTO.getBrand());
        attr.put("AccountNumber", newCustomerNotificationDTO.getAccountNumber());
        attr.put("PhoneNumber", newCustomerNotificationDTO.getPhoneNumber());
        attr.put("isExistingEcommAccount", String.valueOf(newCustomerNotificationDTO.isExistingEcommAccount()));
        super.setAttr(attr);
    }
}
