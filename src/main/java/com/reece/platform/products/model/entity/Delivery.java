package com.reece.platform.products.model.entity;

import com.reece.platform.products.model.DTO.DeliveryDTO;
import com.reece.platform.products.model.PreferredTimeEnum;
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
@Table(name = "deliveries")
public class Delivery {

    public Delivery(DeliveryDTO deliveryDTO) {
        this.preferredDate = deliveryDTO.getPreferredDate();
        this.address = deliveryDTO.getAddress();
        this.preferredTime = deliveryDTO.getPreferredTime();
        this.shipToId = deliveryDTO.getShipTo();
        this.deliveryInstructions = deliveryDTO.getDeliveryInstructions();
        this.shouldShipFullOrder = deliveryDTO.isShouldShipFullOrder();
        this.phoneNumber = deliveryDTO.getPhoneNumber();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "shipto_id")
    @Type(type = "pg-uuid")
    private UUID shipToId;

    @OneToOne
    @JoinColumn(name = "address_id")
    @Type(type = "pg-uuid")
    private Address address;

    @Column(name = "delivery_instructions")
    private String deliveryInstructions;

    @Column(name = "preferred_date")
    private Date preferredDate;

    @Column(name = "preferred_time")
    @Enumerated(EnumType.STRING)
    private PreferredTimeEnum preferredTime;

    @Column(name = "should_ship_full_order")
    private boolean shouldShipFullOrder;

    @Column(name = "phone_number")
    private String phoneNumber;
}
