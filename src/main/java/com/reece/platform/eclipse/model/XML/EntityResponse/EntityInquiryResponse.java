package com.reece.platform.eclipse.model.XML.EntityResponse;

import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Entity", "StatusResult", "SessionID" })
public class EntityInquiryResponse {

    public EntityInquiryResponse() {}
    public EntityInquiryResponse(Entity entity) {
        this.setEntity(entity);
    }

    @XmlElement(name = "Entity")
    private Entity Entity;

    @XmlElement(name = "StatusResult")
    private StatusResult StatusResult;

    @XmlElement(name = "SessionID")
    private String SessionID;

    @Override
    public String toString()
    {
        return "ClassPojo [Entity = "+Entity+", StatusResult = "+StatusResult+", SessionID = "+SessionID+"]";
    }
}
