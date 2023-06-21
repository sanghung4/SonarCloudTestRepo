package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.enums.PhoneTypeEnum;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "auth_id")
    private String authId;

    @Audited
    @Column(name = "email")
    private String email;

    @Audited
    @Column(name = "phone_number")
    private String phoneNumber;

    @Audited
    @Column(name = "first_name")
    private String firstName;

    @Audited
    @Column(name = "last_name")
    private String lastName;

    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneTypeEnum phoneType;

    @EqualsAndHashCode.Exclude
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @EqualsAndHashCode.Exclude
    @Audited
    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @EqualsAndHashCode.Exclude
    @NotAudited
    @ManyToMany
    @JoinTable(
        name = "users_billto_accounts",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> billToAccounts;

    @EqualsAndHashCode.Exclude
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToMany(targetEntity = ErpsUsers.class, mappedBy = "userId")
    private Set<ErpsUsers> erpsUsers;

    @Audited
    @Column(name = "tos_accepted_at")
    private Date tosAcceptedAt;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "pp_accepted_at")
    private Date ppAcceptedAt;

    public User(
        String authId,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Date tosAcceptedAt,
        Date ppAcceptedAt,
        PhoneTypeEnum phoneType
    ) {
        this.authId = authId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.tosAcceptedAt = tosAcceptedAt;
        this.ppAcceptedAt = ppAcceptedAt;
    }

    public Collection<Permission> getPermissions() {
        if (this.role != null) {
            return role.getPermissions();
        } else {
            return new ArrayList<>();
        }
    }
}
