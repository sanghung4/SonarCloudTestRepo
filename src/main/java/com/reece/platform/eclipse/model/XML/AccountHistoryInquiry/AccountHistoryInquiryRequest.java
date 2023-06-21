package com.reece.platform.eclipse.model.XML.AccountHistoryInquiry;

import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;


@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "AccountHistoryInquiry" })
@NoArgsConstructor
public class AccountHistoryInquiryRequest {

    public AccountHistoryInquiryRequest(Security security, String accountId, String startDateInput, String endDateInput) {
        StartDate startDate = new StartDate();
        startDate.setDate(startDateInput);

        EndDate endDate = new EndDate();
        endDate.setDate(endDateInput);

        PostDateRange postDateRange = new PostDateRange();
        postDateRange.setEndDate(endDate);
        postDateRange.setStartDate(startDate);

        AccountHistoryInquiry accountHistoryInquiry = new AccountHistoryInquiry();
        accountHistoryInquiry.setSecurity(security);
        accountHistoryInquiry.setEntityID(accountId);
        accountHistoryInquiry.setPostDateRange(postDateRange);

        this.AccountHistoryInquiry = accountHistoryInquiry;
    }

    @XmlElement(name = "AccountHistoryInquiry")
    private AccountHistoryInquiry AccountHistoryInquiry;
}
