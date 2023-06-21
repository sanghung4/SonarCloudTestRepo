package com.reece.platform.eclipse.model.XML.MassSalesOrderInquiry;

import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.EndDate;
import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.PostDateRange;
import com.reece.platform.eclipse.model.XML.AccountHistoryInquiry.StartDate;
import com.reece.platform.eclipse.model.XML.common.Security;
import com.reece.platform.eclipse.model.XML.common.SitePass;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "IDMS-XML")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"MassSalesOrderInquiry"})
@NoArgsConstructor
public class MassSalesOrderInquiryRequest {

    public MassSalesOrderInquiryRequest(Security security, String startDateString, String endDateString, Integer page) {

        MassSalesOrderInquiry massSalesOrderInquiry = new MassSalesOrderInquiry();
        massSalesOrderInquiry.setSecurity(security);

        StartDate startDate = new StartDate();
        startDate.setDate(startDateString);

        EndDate endDate = new EndDate();
        endDate.setDate(endDateString);

        PostDateRange orderDateRange = new PostDateRange();
        orderDateRange.setEndDate(endDate);
        orderDateRange.setStartDate(startDate);

        massSalesOrderInquiry.setOrderDateRange(orderDateRange);

        if (page != null) {
            massSalesOrderInquiry.setStartIndex(String.valueOf(page));
            massSalesOrderInquiry.setMaxResults("10");
        }

        this.MassSalesOrderInquiry = massSalesOrderInquiry;
    }

    @XmlElement(name = "MassSalesOrderInquiry")
    private MassSalesOrderInquiry MassSalesOrderInquiry;
}
