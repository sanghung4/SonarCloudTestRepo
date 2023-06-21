package com.reece.platform.products.insite.entity;

import java.util.List;
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
public class WishList {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "Name")
    private String name;

    @OneToOne
    @JoinColumn(name = "CustomerId")
    @Type(type = "uuid-char")
    private Customer customer;

    @OneToMany(targetEntity = WishListProduct.class, mappedBy = "wishListId", fetch = FetchType.EAGER)
    private List<WishListProduct> wishListProducts;
}
