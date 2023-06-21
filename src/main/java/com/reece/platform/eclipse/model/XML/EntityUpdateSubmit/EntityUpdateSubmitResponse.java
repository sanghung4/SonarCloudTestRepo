package com.reece.platform.eclipse.model.XML.EntityUpdateSubmit;


import com.reece.platform.eclipse.model.XML.EntityResponse.Entity;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Entity", "StatusResult", "SessionID" })
public class EntityUpdateSubmitResponse {

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
