package com.reece.platform.eclipse.model.XML.OpenOrderInquiry;

import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.*;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;


@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "OpenOrderInquiry" })
@NoArgsConstructor
public class OpenOrderInquiryRequest {

    public OpenOrderInquiryRequest(String accountId, Security security, String startDateInput, String endDateInput) {
        StartDate startDate = new StartDate();
        startDate.setDate(startDateInput);

        EndDate endDate = new EndDate();
        endDate.setDate(endDateInput);

        OrderDateRange orderDateRange = new OrderDateRange();
        orderDateRange.setEndDate(endDate);
        orderDateRange.setStartDate(startDate);

        OpenOrderInquiry openOrderInquiry = new OpenOrderInquiry();
        openOrderInquiry.setSecurity(security);
        openOrderInquiry.setEntityID(accountId);
        openOrderInquiry.setOrderDateRange(orderDateRange);

        this.OpenOrderInquiry = openOrderInquiry;
    }

    @XmlElement(name = "OpenOrderInquiry")
    private OpenOrderInquiry OpenOrderInquiry;
}
