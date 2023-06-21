package com.reece.platform.eclipse.model.XML.EntityRequest;

import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "EntityInquiry" })
public class EntityRequest {

    public EntityRequest() {}

    public EntityRequest(String accountId, Security security) {
        EntityInquiry entityInquiry = new EntityInquiry();
        entityInquiry.setEntityID(accountId);
        entityInquiry.setSecurity(security);

        this.EntityInquiry = entityInquiry;
    }

    @XmlElement(name = "EntityInquiry")
    private EntityInquiry EntityInquiry;

    @Override
    public String toString()
    {
        return "ClassPojo [EntityInquiry = "+EntityInquiry+"]";
    }
}
