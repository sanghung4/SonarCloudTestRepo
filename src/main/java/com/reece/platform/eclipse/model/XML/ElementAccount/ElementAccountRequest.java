package com.reece.platform.eclipse.model.XML.ElementAccount;

import com.reece.platform.eclipse.model.XML.EntityRequest.EntityInquiry;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "IDMS-XML")
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "ElementAccountSetup" })
public class ElementAccountRequest {

    @XmlElement(name = "ElementAccountSetup")
    private ElementAccountSetup ElementAccountSetup;
}
