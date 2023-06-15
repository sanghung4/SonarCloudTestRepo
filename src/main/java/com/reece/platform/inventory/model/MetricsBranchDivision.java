package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "metrics_branch_division")
@Getter
@Setter
public class MetricsBranchDivision extends IdEntity{
    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "branch_state")
    private String branchState;

    @Column(name = "division_id")
    private String divisionId;
}
