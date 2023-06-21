package com.reece.platform.accounts.model.entity;

import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import liquibase.pro.packaged.E;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_internal")
    private Boolean isInternal;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
