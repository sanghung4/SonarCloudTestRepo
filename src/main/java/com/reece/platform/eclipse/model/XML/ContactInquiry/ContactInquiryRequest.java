package com.reece.platform.eclipse.model.XML.ContactInquiry;

import com.reece.platform.eclipse.model.XML.ContactInquiry.ContactInquiry;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ContactInquiry" })
public class ContactInquiryRequest {
    public ContactInquiryRequest() {}

    public ContactInquiryRequest(String userId, Security security) {
        ContactInquiry contactInquiry = new ContactInquiry();
        contactInquiry.setContactID(userId);
        contactInquiry.setSecurity(security);

        this.ContactInquiry = contactInquiry;
    }

    @XmlElement(name = "ContactInquiry")
    private ContactInquiry ContactInquiry;
}
