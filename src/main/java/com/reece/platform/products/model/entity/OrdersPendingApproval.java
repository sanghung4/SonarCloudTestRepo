package com.reece.platform.products.model.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "orders_pending_approval")
public class OrdersPendingApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "cart_id")
    @Type(type = "pg-uuid")
    private Cart cart;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "submission_date")
    private Date submissionDate;
}
