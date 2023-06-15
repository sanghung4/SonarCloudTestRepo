package com.reece.platform.inventory.model;

import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pi_counts")
@Getter
@Setter
public class Count extends AuditableEntity {

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "erp_count_id", nullable = false)
    private String erpCountId;

    @Column(name = "all_locations_fetched", nullable = false)
    private Boolean allLocationsFetched = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CountStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "count", fetch = FetchType.LAZY)
    private List<LocationCount> locationCounts;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public static String getBranchAndCountId(Count count) {
        return count.getErpCountId() + count.getBranch().getErpBranchNum();
    }
}
