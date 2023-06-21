package com.reece.platform.products.model.entity;

import com.reece.platform.products.orders.model.WebStatus;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "order_status")
public class OrderStatus extends AuditableEntity {

    @Column(name = "erp_account_id")
    private String erpAccountId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "web_status")
    @Enumerated(EnumType.STRING)
    private WebStatus webStatus;

    @Column(name = "ship_to_id")
    private UUID shipToId;

    @Column(name = "error_message")
    private String errorMessage;
}
