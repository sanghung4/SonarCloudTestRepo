package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.ElementAccount.ElementAccountResponseWrapper;
import com.reece.platform.eclipse.model.XML.ElementAccount.StatusResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ElementAccountSetupResponseDTO {

    private String sessionId;
    private String elementSetupUrl;
    private String elementSetupId;
    private StatusResult statusResult;

    public ElementAccountSetupResponseDTO(ElementAccountResponseWrapper elementAccountResponseWrapper) {
        this.sessionId = elementAccountResponseWrapper.getElementAccountSetupResponse().getSessionId();
        this.elementSetupUrl = elementAccountResponseWrapper.getElementAccountSetupResponse().getElementSetupUrl();
        this.elementSetupId = elementAccountResponseWrapper.getElementAccountSetupResponse().getElementSetupId();
        this.statusResult = elementAccountResponseWrapper.getElementAccountSetupResponse().getStatusResult();
    }
}
