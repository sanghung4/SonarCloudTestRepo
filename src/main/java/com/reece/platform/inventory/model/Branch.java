package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pi_branches")
@Getter
@Setter
public class Branch extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "erp_system", nullable = false)
    private ERPSystemName erpSystem;

    @Column(name = "erp_branch_num", nullable = false)
    private String erpBranchNum;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "branch")
    private Set<Count> counts;

    @OneToMany(mappedBy = "branch")
    @OrderBy("erpLocationId")
    private List<Location> locations;
}
