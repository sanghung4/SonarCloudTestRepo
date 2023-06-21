package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.DeliveryMethodErrorDTO;

import java.util.HashMap;

public class DeliveryMethodErrorDE extends BaseDataExtension {
    public DeliveryMethodErrorDE(DeliveryMethodErrorDTO deliveryMethodErrorDTO, String supportEmail) {
        super.setTo(supportEmail);
        HashMap<String, String> attr = new HashMap<>();
        attr.put("billTo", deliveryMethodErrorDTO.getBillToEntityId());
        attr.put("shipTo", deliveryMethodErrorDTO.getShipToEntityId());
        attr.put("orderedBy", deliveryMethodErrorDTO.getOrderedBy());
        attr.put("branchId", deliveryMethodErrorDTO.getBranchId());
        attr.put("deliveryMethod", deliveryMethodErrorDTO.getIsDelivery() ? "OT OUR TRUCK": "WILL CALL");
        attr.put("email", deliveryMethodErrorDTO.getUserEmail());
        super.setAttr(attr);
    }
}
