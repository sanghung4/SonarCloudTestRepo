package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.enums.ErpEnum;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@Table(name = "erps_users", uniqueConstraints = @UniqueConstraint(columnNames = {"erp", "user_id"}))
public class ErpsUsers extends AuditableEntity {

    @Column(name = "erp")
    @Enumerated(EnumType.STRING)
    private ErpEnum erp;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "erp_user_id")
    private String erpUserId;

    @Column(name = "erp_user_login")
    private String erpUserLogin;

    @Column(name = "erp_user_password")
    private String erpUserPassword;
}
