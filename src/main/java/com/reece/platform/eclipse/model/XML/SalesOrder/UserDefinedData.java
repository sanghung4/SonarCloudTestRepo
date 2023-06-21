package com.reece.platform.eclipse.model.XML.SalesOrder;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Dictionary;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "dictionary" })
public class UserDefinedData {
    private List<Dictionary<String, String>> userDefinedData;
}
