package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.enums.ErpEnum;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "users_billto_accounts",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @Column(name = "parent_account_id", insertable = false, updatable = false)
    private UUID parentAccountId;

    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", referencedColumnName = "id")
    private Account parentAccount;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private List<Account> shipToAccounts;

    @Column(name = "erp")
    @Enumerated(EnumType.STRING)
    private ErpEnum erp;

    @Column(name = "is_billto")
    private boolean isBillto;

    @Column(name = "erp_account_id")
    private String erpAccountId;

    @Column(name = "branch_id")
    private String branchId;
}
