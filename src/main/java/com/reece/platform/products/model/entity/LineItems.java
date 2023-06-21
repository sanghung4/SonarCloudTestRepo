package com.reece.platform.products.model.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "line_items")
public class LineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "erp_part_number")
    private String erpPartNumber;

    @Column(name = "customer_part_number")
    private String customerPartNumber;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_per_unit")
    private int pricePerUnit;

    @Column(name = "price_last_updated_at")
    private Date priceLastUpdatedAt;

    @Column(name = "qty_available")
    private int qtyAvailable;

    @Column(name = "qty_available_last_updated_at")
    private Date qtyAvailableLastUpdatedAt;

    @Column(name = "cart_id")
    @Type(type = "pg-uuid")
    private UUID cartId;

    @Column(name = "uom")
    private String uom;

    @Column(name = "umqt")
    private String umqt;
}
