package com.reece.platform.eclipse.model.XML.LoginSubmit;

import com.reece.platform.eclipse.model.XML.common.Login;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"loginSubmit"})
@NoArgsConstructor
public class LoginSubmitRequest {

    public LoginSubmitRequest(String accountId, String loginId, String password) {

        loginSubmit = new LoginSubmit();

        SitePass sitePass = new SitePass();
        sitePass.setLoginId(loginId);
        sitePass.setPassword(password);
        sitePass.setActiveCustomer(accountId);

        Security security = new Security();
        security.setSitePass(sitePass);

        loginSubmit.setSecurity(security);
    }

    public LoginSubmitRequest(String loginId, String password) {
        loginSubmit = new LoginSubmit();

        val login = new Login();
        login.setLoginID(loginId);
        login.setPassword(password);

        val security = new Security();
        security.setLogin(login);

        loginSubmit.setSecurity(security);
    }

    @XmlElement(name = "LoginSubmit")
    private LoginSubmit loginSubmit;
}
