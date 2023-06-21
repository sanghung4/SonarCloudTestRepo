package com.reece.platform.eclipse.model.XML.common;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlType(propOrder = { "description", "ErrorMessageList" })
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusResult {

    @XmlAttribute(name = "Success")
    private String success;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "ErrorMessageList")
    private ErrorMessageList ErrorMessageList;

}