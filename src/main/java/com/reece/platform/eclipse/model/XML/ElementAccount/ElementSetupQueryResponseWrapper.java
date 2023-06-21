package com.reece.platform.eclipse.model.XML.ElementAccount;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"elementSetupQueryResponse"})
public class ElementSetupQueryResponseWrapper {

    @XmlElement(name = "ElementSetupQueryResponse")
    private ElementSetupQueryResponse elementSetupQueryResponse;
}
