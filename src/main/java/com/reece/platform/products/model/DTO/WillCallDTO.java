package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.PreferredTimeEnum;
import com.reece.platform.products.model.entity.WillCall;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WillCallDTO {

    private UUID id;
    private String branchId;
    private Date preferredDate;
    private PreferredTimeEnum preferredTime;
    private String pickupInstructions;

    public WillCallDTO(String branchId) {
        this.id = UUID.randomUUID();
        this.branchId = branchId;
    }

    public WillCallDTO(WillCall willCall) {
        this.id = willCall.getId();
        this.branchId = willCall.getBranchId();
        this.preferredDate = willCall.getPreferredDate();
        this.preferredTime = willCall.getPreferredTime();
        this.pickupInstructions = willCall.getPickupInstructions();
    }
}
