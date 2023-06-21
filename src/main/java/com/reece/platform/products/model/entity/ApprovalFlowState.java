package com.reece.platform.products.model.entity;

import com.reece.platform.products.model.ApprovalFlowStateEnum;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "approval_flow_states")
public class ApprovalFlowState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalFlowStateEnum name;

    @Column(name = "display_name")
    private String displayName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApprovalFlowState that = (ApprovalFlowState) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
