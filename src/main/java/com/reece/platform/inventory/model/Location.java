package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pi_locations")
@Getter
@Setter
public class Location extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "erp_location_id", nullable = false)
    private String erpLocationId;
}
