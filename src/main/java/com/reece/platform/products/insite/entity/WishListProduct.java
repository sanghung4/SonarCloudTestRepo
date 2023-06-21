package com.reece.platform.products.insite.entity;

import java.util.UUID;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class WishListProduct {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "QtyOrdered")
    private Integer qtyOrdered;

    @Column(name = "SortOrder")
    private Integer sortOrder;

    @Column(name = "WishListId")
    @Type(type = "uuid-char")
    private UUID wishListId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ProductId")
    @Type(type = "uuid-char")
    private Product product;
}
