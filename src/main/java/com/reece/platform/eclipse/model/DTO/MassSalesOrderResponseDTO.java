package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.MassSalesOrderResponse;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MassSalesOrderResponseDTO {
    private List<GetOrderResponseDTO> salesOrders;

    public MassSalesOrderResponseDTO(MassSalesOrderResponse massSalesOrderResponse, Boolean filterBids) throws IOException {
        this.salesOrders = massSalesOrderResponse
                .getMassSalesOrderInquiryResponse()
                .getSalesOrderList()
                .getSalesOrderList()
                .stream()
                .map(GetOrderResponseDTO::new)
                .filter(o -> !filterBids || o.getOrderStatus().equals("B")
                        && (o.getQuoteStatus() != null && (o.getQuoteStatus().equals("WEB EDIT") || o.getQuoteStatus().equals("WEB VIEW")
                || o.getQuoteStatus().equals("WEB NON-GENUINE"))))
                .collect(Collectors.toList());
    }
}
