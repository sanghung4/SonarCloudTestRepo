package com.reece.platform.eclipse.model.XML.ProductRequest;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "eclipsePartNumber", "includeRichContent", "userDefined1" })
public class PartIdentifiers {

    @XmlElement(name = "EclipsePartNumber")
    private String eclipsePartNumber;

    @XmlElement(name = "IncludeRichContent")
    private String includeRichContent;

    @XmlElement(name = "UserDefined1")
    private String userDefined1;

}
