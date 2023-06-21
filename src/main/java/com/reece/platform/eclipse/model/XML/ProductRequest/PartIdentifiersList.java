package com.reece.platform.eclipse.model.XML.ProductRequest;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "partIdentifiers" })
public class PartIdentifiersList {
    @XmlElement(name = "PartIdentifiers")
    private List<PartIdentifiers> partIdentifiers;
}