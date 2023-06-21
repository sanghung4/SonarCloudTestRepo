package com.reece.platform.accounts.model.DTO;

import java.io.Serializable;

/**
 * Response model for contact creation request to Eclipse service
 */
public class ErpContactCreationResponse implements Serializable {

    private String contactId;
    private String erpUsername;
    private String erpPassword;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getErpUsername() {
        return erpUsername;
    }

    public void setErpUsername(String erpUsername) {
        this.erpUsername = erpUsername;
    }

    public String getErpPassword() {
        return erpPassword;
    }

    public void setErpPassword(String erpPassword) {
        this.erpPassword = erpPassword;
    }
}
