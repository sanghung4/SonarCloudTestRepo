package com.reece.platform.accounts.model.entity;

import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "rejection_reasons")
public class RejectionReason {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private RejectionReasonEnum type;

    @Column
    private String description;
}
