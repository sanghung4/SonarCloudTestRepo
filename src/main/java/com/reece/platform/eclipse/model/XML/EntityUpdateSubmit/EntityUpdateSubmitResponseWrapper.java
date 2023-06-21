package com.reece.platform.eclipse.model.XML.EntityUpdateSubmit;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "entityUpdateSubmitResponse" })
public class EntityUpdateSubmitResponseWrapper {

    @XmlElement(name = "EntityUpdateSubmitResponse")
    private EntityUpdateSubmitResponse entityUpdateSubmitResponse;
}
