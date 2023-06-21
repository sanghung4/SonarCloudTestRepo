package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse.ReorderPadInquiryResponse;
import com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse.ReorderPadItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PreviouslyPurchasedProductsDTO {
    List<ReorderPadItem> products;
    PaginationDTO pagination;

    public PreviouslyPurchasedProductsDTO(ReorderPadInquiryResponse res, int currentPage, int pageSize) {
        if (res.getReorderPadList() != null) {
            var items = res.getReorderPadList().getReorderPadItems();
            pagination = new PaginationDTO(currentPage, pageSize, items.size());
            products = items.stream()
                    .skip(currentPage * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
        } else {
            // Set an empty list
            products = new ArrayList<ReorderPadItem>();
            pagination = new PaginationDTO(currentPage, pageSize, 0);
        }
    }
}
