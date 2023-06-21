package com.reece.platform.eclipse.model.XML.ContactUpdateSubmit;

import com.reece.platform.eclipse.model.DTO.UpdateContactRequestDTO;
import com.reece.platform.eclipse.model.XML.ContactInquiryResponse.ContactInquiryResponseWrapper;
import com.reece.platform.eclipse.model.XML.common.*;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ContactUpdateSubmit" })
public class ContactUpdateSubmitRequest {

    public ContactUpdateSubmitRequest() {}

    public ContactUpdateSubmitRequest(String userId, String accountId, Security security, UpdateContactRequestDTO updateContactRequestDTO, ContactInquiryResponseWrapper contactInquiryResponseWrapper) {
        Contact existingContact = contactInquiryResponseWrapper.getContactInquiryResponse().getContact();

        ContactID contactID = new ContactID();
        contactID.setContactId(userId);

        ContactName contactName = new ContactName();

        if(updateContactRequestDTO.getFirstName() != null && !updateContactRequestDTO.getFirstName().isEmpty()) {
            contactName.setFirstName(updateContactRequestDTO.getFirstName());
        } else {
            contactName.setFirstName(existingContact.getContactName().getFirstName());
        }

        if(updateContactRequestDTO.getLastName() != null && !updateContactRequestDTO.getLastName().isEmpty()) {
            contactName.setLastName(updateContactRequestDTO.getLastName());
        } else {
            contactName.setLastName(existingContact.getContactName().getLastName());
        }


        List<Telephone> telephones = new ArrayList<>();
        Telephone telephone = new Telephone();
        
        // TODO: This does not account for multiple phone numbers yet
        String phone = null;
        
        if(existingContact.getTelephoneList() != null) {
            phone = existingContact.getTelephoneList().getTelephone().get(0).getNumber();    
        }
        
        if (updateContactRequestDTO.getPhoneNumber() != null) {
            phone = updateContactRequestDTO.getPhoneNumber();
        }

        telephone.setNumber(phone);
        telephone.setDescription(updateContactRequestDTO.getPhoneTypeDisplayName());
        telephones.add(telephone);
        TelephoneList telephoneList = new TelephoneList();
        telephoneList.setTelephone(telephones);

        // TODO: This does not account for multiple email addresses yet
        String email = null;
        
        if(existingContact.getEmailAddressList() != null) {
            email = existingContact.getEmailAddressList().getEmailAddresses().get(0).getContent();
        }
        
        if (updateContactRequestDTO.getEmail() != null) {
            email = updateContactRequestDTO.getEmail();
        }

        EmailAddressList emailAddressList = new EmailAddressList();
        List<EmailAddress> emails = new ArrayList<>();
        EmailAddress ea = new EmailAddress();
        ea.setContent(email);
        emails.add(ea);
        emailAddressList.setEmailAddresses(emails);

        existingContact.setContactID(contactID);
        existingContact.setEntityID(accountId);
        existingContact.setContactName(contactName);
        existingContact.setTelephoneList(telephoneList);
        existingContact.setEmailAddressList(emailAddressList);

        ContactUpdateSubmit contactUpdateSubmit = new ContactUpdateSubmit();
        contactUpdateSubmit.setSecurity(security);
        contactUpdateSubmit.setContact(existingContact);

        this.ContactUpdateSubmit = contactUpdateSubmit;
    }

    @XmlElement(name = "ContactUpdateSubmit")
    private ContactUpdateSubmit ContactUpdateSubmit;
}
