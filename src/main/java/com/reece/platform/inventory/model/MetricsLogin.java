package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "metrics_logins")
@Getter
@Setter
public class MetricsLogin extends IdEntity{
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "login_at")
    private Date loginAt;
}
