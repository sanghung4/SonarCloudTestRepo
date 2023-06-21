package com.reece.platform.eclipse.model.XML.common;

import com.reece.platform.eclipse.model.XML.common.Telephone;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "telephone" })
public class TelephoneList {
    @XmlElement(name = "Telephone")
    List<Telephone> telephone;
}
