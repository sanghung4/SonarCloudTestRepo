package com.reece.platform.eclipse.model.XML.AccountHistoryResponse;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "AccountHistoryItems" })
public class AccountHistoryItemList {

    @XmlElement(name = "AccountHistoryItem")
    private List<AccountHistoryItem> AccountHistoryItems;
}