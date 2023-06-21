package com.reece.platform.eclipse.model.XML.LoginSubmit;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "loginSubmitResponse" })
public class LoginResponse implements Serializable {

    @XmlElement(name="LoginSubmitResponse")
    LoginSubmitResponse loginSubmitResponse;
}
