package com.reece.platform.products.model.entity;

import com.reece.platform.products.insite.entity.WishListProduct;
import com.reece.platform.products.model.DTO.ListLineItemDTO;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_line_items")
public class ListLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "erp_part_number")
    private String erpPartNumber;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "list_id")
    private UUID listId;

    @Column(name = "sort_order")
    private Integer sortOrder;

    public ListLineItem(ListLineItemDTO listLineItemDTO) {
        this.erpPartNumber = listLineItemDTO.getErpPartNumber();
        this.quantity = listLineItemDTO.getQuantity();
        this.listId = listLineItemDTO.getListId();
        this.sortOrder = listLineItemDTO.getSortOrder();
        this.id = listLineItemDTO.getId();
    }

    public ListLineItem(WishListProduct wishListProduct) {
        this.erpPartNumber = wishListProduct.getProduct().getErpNumber();
        this.quantity = wishListProduct.getQtyOrdered();
        this.sortOrder = wishListProduct.getSortOrder();
    }

    public ListLineItem(ListLineItem listLineItem, UUID listID) {
        this.erpPartNumber = listLineItem.getErpPartNumber();
        this.quantity = listLineItem.getQuantity();
        this.listId = listID;
        this.sortOrder = listLineItem.getSortOrder();
        this.id = listLineItem.getId();
    }
}
