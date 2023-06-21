package com.reece.platform.eclipse.model.XML.ElementAccount;

import com.reece.platform.eclipse.model.XML.common.DateWrapper;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"success", "description"})
public class StatusResult {

    @XmlAttribute(name = "Success")
    private String success;

    @XmlElement(name = "Description")
    private String description;

}
