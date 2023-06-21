package com.reece.platform.eclipse.model.XML.EntityUpdateSubmit;

import com.reece.platform.eclipse.model.XML.EntityResponse.Entity;
import com.reece.platform.eclipse.model.XML.common.Security;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "Security", "Entity"})
public class EntityUpdateSubmitRequestWrapper {

    @XmlElement(name = "Security")
    private Security Security;

    @XmlElement(name = "Entity")
    private Entity Entity;
}
