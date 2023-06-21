package com.reece.platform.products.model.entity;

import com.reece.platform.products.model.DTO.WillCallDTO;
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
@Table(name = "will_calls")
public class WillCall {

    public WillCall(WillCallDTO willCallDTO) {
        this.preferredDate = willCallDTO.getPreferredDate();
        this.preferredTime = willCallDTO.getPreferredTime();
        this.branchId = willCallDTO.getBranchId();
        this.pickupInstructions = willCallDTO.getPickupInstructions();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "preferred_date")
    private Date preferredDate;

    @Column(name = "preferred_time")
    @Enumerated(EnumType.STRING)
    private PreferredTimeEnum preferredTime;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "pickup_instructions")
    private String pickupInstructions;
}
