package com.reece.platform.eclipse.model.XML.AccountInquiryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"accountInquiryItemList"} )
public class AccountInquiryItemList {
    @XmlElement(name = "AccountInquiryItem")
    private List<AccountInquiryItem> accountInquiryItemList;
}
