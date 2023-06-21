package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.seed.PermissionType;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column
    private PermissionType name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
