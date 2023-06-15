package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pi_location_count")
@Getter
@Setter
public class LocationCount extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "count_id", nullable = false)
    private Count count;

    @Column(name = "committed")
    private Boolean committed;

    @OneToMany(mappedBy = "locationCount", cascade = CascadeType.ALL)
    @OrderBy("tagNum")
    private List<CountItem> items;

    public boolean isCommitted() {
        return Boolean.TRUE.equals(committed);
    }
}
