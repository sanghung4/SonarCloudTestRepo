package com.reece.platform.eclipse.model.XML.EntityResponse;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "EntityNote" })
public class EntityNoteList {

    @XmlElement(name = "EntityNote")
    private EntityNote EntityNote;

    @Override
    public String toString()
    {
        return "ClassPojo [EntityNote = "+EntityNote+"]";
    }
}
