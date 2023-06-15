package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pi_write_ins")
@Getter
@Setter
public class WriteIn extends AuditableEntity {

    @Column(name = "location_count_id", nullable = true)
    private UUID locationCount;

    @Column(name = "catalog_num")
    private String catalogNum;

    @Column(name = "upc_num")
    private String upcNum;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "uom")
    private String uom;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;

    @ManyToOne
    @JoinColumn(name="count_id",nullable = false)
    private Count countId;

    @Column(name = "location_name", nullable = false)
    private String locationName;
}
