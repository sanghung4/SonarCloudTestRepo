package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "date" })
public class DateTimeWrapper {

    @XmlElement(name = "DateTime")
    private Date date;
}
