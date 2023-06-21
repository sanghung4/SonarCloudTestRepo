package com.reece.platform.eclipse.model.XML.ElementAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "elementSetupQuery" })
public class ElementSetupQueryRequest {

    @XmlElement(name = "ElementSetupQuery")
    private ElementSetupQuery elementSetupQuery;
}
