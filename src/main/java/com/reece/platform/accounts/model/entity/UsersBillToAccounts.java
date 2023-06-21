package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.key.UserAccountKey;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users_billto_accounts")
@IdClass(UserAccountKey.class)
public class UsersBillToAccounts implements Serializable {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "is_restricted")
    private boolean isRestricted;
}
