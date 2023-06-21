package com.reece.platform.eclipse.model.XML.ContactNewSubmit;

import com.reece.platform.eclipse.model.DTO.CreateContactRequestDTO;
import com.reece.platform.eclipse.model.XML.common.*;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "contactNewSubmit" })
@XmlRootElement(name = "IDMS-XML")
public class ContactNewSubmitRequest {
    private static final int ECLIPSE_PASSWORD_CHARACTER_LIMIT = 14;

    public ContactNewSubmitRequest() {}

    public ContactNewSubmitRequest(String entityId, Security security, CreateContactRequestDTO createContactRequestDTO) {
        ContactID contactID = new ContactID();
        contactID.setIsNew("Yes");

        ContactName contactName = new ContactName();
        contactName.setFirstName(createContactRequestDTO.getFirstName());
        contactName.setLastName(createContactRequestDTO.getLastName());

        List<Telephone> telephones = new ArrayList<>();
        Telephone telephone = new Telephone();
        telephone.setNumber(createContactRequestDTO.getTelephone());
        telephone.setDescription(createContactRequestDTO.getPhoneType());
        telephones.add(telephone);
        TelephoneList telephoneList = new TelephoneList();
        telephoneList.setTelephone(telephones);

        Login login = new Login();
        String userLoginId = UUID.randomUUID().toString().replace("-", "");
        String userPassword = UUID.randomUUID().toString().replace("-", "");
        userPassword = userPassword.substring(userPassword.length() - ECLIPSE_PASSWORD_CHARACTER_LIMIT);
        login.setLoginID(userLoginId);
        login.setPassword(userPassword);
        EmailAddressList emailAddressList = new EmailAddressList();
        List<EmailAddress> emails = new ArrayList<>();
        EmailAddress ea = new EmailAddress(createContactRequestDTO.getEmail());
        emails.add(ea);
        emailAddressList.setEmailAddresses(emails);

        Contact contact = new Contact();
        contact.setContactID(contactID);
        contact.setEntityID(entityId);
        contact.setContactName(contactName);
        contact.setTelephoneList(telephoneList);
        contact.setEmailAddressList(emailAddressList);
        contact.setIsSuperuser("No");
        contact.setLogin(login);

        ContactNewSubmit contactNewSubmit = new ContactNewSubmit();
        contactNewSubmit.setSecurity(security);
        contactNewSubmit.setContact(contact);

        this.contactNewSubmit = contactNewSubmit;
    }

    @XmlElement(name = "ContactNewSubmit")
    private ContactNewSubmit contactNewSubmit;
}
