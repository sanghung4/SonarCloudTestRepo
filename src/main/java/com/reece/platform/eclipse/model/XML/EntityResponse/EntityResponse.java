package com.reece.platform.eclipse.model.XML.EntityResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "EntityInquiryResponse" })
public class EntityResponse {

    public EntityResponse() {}

    @XmlElement(name = "EntityInquiryResponse")
    private EntityInquiryResponse EntityInquiryResponse;

    @Override
    public String toString()
    {
        return "ClassPojo [EntityInquiryResponse = "+EntityInquiryResponse+"]";
    }
}
