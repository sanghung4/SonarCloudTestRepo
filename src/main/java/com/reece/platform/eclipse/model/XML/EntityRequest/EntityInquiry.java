package com.reece.platform.eclipse.model.XML.EntityRequest;

import com.reece.platform.eclipse.model.XML.common.Security;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "" })
public class EntityInquiry {

    @XmlElement(name = "")
    private String EntityID;

    @XmlElement(name = "")
    private String IncludeCreditCardData;

    @XmlElement(name = "")
    private com.reece.platform.eclipse.model.XML.common.Security Security;

    @Override
    public String toString()
    {
        return "ClassPojo [EntityID = "+EntityID+", IncludeCreditCardData = "+ IncludeCreditCardData +", Security = "+Security+"]";
    }
}
