package com.reece.platform.notifications.model.DataExtensions;

import java.util.HashMap;

// Data extension can be found at the following location in SFMC:
// Data Extensions > Reece Ecomm Transactional Journeys > CustomerAccountDeletedAdminNotificationDE OR CustomerAccountDeletedBranchNotificationDE OR CustomerAccountDeletedCreditDeptNotificationDE
// Both data extensions have the same fields, but are separated so we can differentiate between admins and branch managers
public class CustomerAccountDeletedNotificationDE extends BaseDataExtension {
    public CustomerAccountDeletedNotificationDE(String adminEmail, CustomerAccountDeletedDE customerAccountDeletedDE) {
        HashMap<String, String> attrs = customerAccountDeletedDE.getAttr();
        attrs.put("CustomerEmailAddress", customerAccountDeletedDE.getTo());
        super.setAttr(attrs);
        super.setTo(adminEmail);
    }
}
