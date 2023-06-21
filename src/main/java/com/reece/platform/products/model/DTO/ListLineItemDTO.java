package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.CustomerPartNumber;
import com.reece.platform.products.model.ImageUrls;
import com.reece.platform.products.model.Stock;
import com.reece.platform.products.model.StoreStock;
import com.reece.platform.products.model.TechSpec;
import com.reece.platform.products.model.entity.ListLineItem;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLineItemDTO {

    private UUID id;
    private String erpPartNumber;
    private Integer quantity;
    private Integer minIncrementQty;
    private UUID listId;
    private Integer sortOrder;
    private int pricePerUnit;
    private Stock stock;
    private String status;
    private String name;
    private ImageUrls imageUrls;
    List<TechSpec> techSpecs;
    private String manufacturerName;
    private String manufacturerNumber;

    private List<String> customerPartNumber;
    private List<CustomerPartNumber> customerPartNumbers;

    public ListLineItemDTO(ListLineItem listLineItem) {
        this.id = listLineItem.getId();
        this.erpPartNumber = listLineItem.getErpPartNumber();
        this.quantity = listLineItem.getQuantity();
        this.listId = listLineItem.getListId();
        this.sortOrder = listLineItem.getSortOrder();
    }

    public void setCustomerPartNumbers(List<CustomerPartNumber> customerPartNumbers, String customerNumber) {
        if (customerPartNumbers != null) {
            customerPartNumbers.forEach(a -> {
                if (a.getCustomer().equals(customerNumber)) {
                    this.customerPartNumber = a.getPartNumbers();
                }
            });
        }
    }
}
