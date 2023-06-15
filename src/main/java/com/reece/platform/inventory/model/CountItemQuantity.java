package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pi_count_location_item_quantity")
@Getter
@Setter
public class CountItemQuantity extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "count_location_item_id")
    private CountItem countItem;

    @Column(name = "quantity")
    private Integer quantity;
}
