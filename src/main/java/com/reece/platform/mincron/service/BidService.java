package com.reece.platform.mincron.service;

import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.model.BidDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    private final ManagedCallFactory mcf;
    private static final int BID_CALLS_STARTING_ROW = 2;

    @Autowired
    public BidService(ManagedCallFactory mcf) {
        this.mcf = mcf;
    }

    public PageDTO<BidDTO> getBids(
            String accountId,
            Integer startRow,
            Integer maxRows,
            String searchFilter,
            String fromDate,
            String toDate,
            String sortOrder,
            String sortDirection) throws Exception {

        PageDTO<BidDTO> bidPage = new PageDTO<>();
        bidPage.setStartRow(startRow);

        try (CallBuilderConfig cbc = mcf.makeManagedCall("AIR7050", 18, true)) {

            cbc.setInputString(accountId);
            cbc.setInputInt(startRow);
            cbc.setInputInt(maxRows);
            cbc.setInputString(searchFilter);
            cbc.setInputString(fromDate);
            cbc.setInputString(toDate);
            cbc.setInputString(sortOrder);
            cbc.setInputString(sortDirection);

            cbc.setOutputChar();
            cbc.setOutputChar();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();

            ResponseBuilderConfig rb = cbc.getResultSet(BID_CALLS_STARTING_ROW);

            while (rb.hasMoreData()) {
                BidDTO bid = new BidDTO();
                bid.setBidNumber(rb.getResultString());
                bid.setDescription(rb.getResultString());
                bid.setBidDate(rb.getMincronDate());
                bidPage.results.add(bid);
            }

            bidPage.setRowsReturned(bidPage.results.size());

            bidPage.setTotalRows(cbc.getNumberOfRows());
        }

        return bidPage;
    }
}
