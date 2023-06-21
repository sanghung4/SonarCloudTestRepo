package com.reece.platform.products.model.entity;

import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.DTO.CreditCardDTO;
import com.reece.platform.products.model.DeliveryMethodEnum;
import com.reece.platform.products.model.PaymentMethodTypeEnum;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
@TypeDefs({ @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) })
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "owner_id")
    @Type(type = "pg-uuid")
    private UUID ownerId;

    @Column(name = "approver_id")
    @Type(type = "pg-uuid")
    private UUID approverId;

    @Column(name = "shipto_id")
    @Type(type = "pg-uuid")
    private UUID shipToId;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "pricing_branch_id")
    private String pricingBranchId;

    @Column(name = "shipping_branch_id")
    private String shippingBranchId;

    @Column(name = "payment_method_type")
    @Enumerated(EnumType.STRING)
    private PaymentMethodTypeEnum paymentMethodType;

    @Column(name = "credit_card", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private CreditCardDTO creditCard;

    @Column(name = "approval_state")
    @Type(type = "pg-uuid")
    private UUID approvalState;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "subtotal")
    private int subtotal;

    @Column(name = "shipping_handling")
    private int shippingHandling;

    @Column(name = "tax")
    private int tax;

    @Column(name = "total")
    private int total;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethodEnum deliveryMethod;

    @Column(name = "erp_system_name")
    @Enumerated(EnumType.STRING)
    private ERPSystem erpSystemName;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    @Type(type = "pg-uuid")
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "will_call_id")
    @Type(type = "pg-uuid")
    private WillCall willCall;

    @Transient
    private List<LineItems> products;
}
