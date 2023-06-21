package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.PreferredTimeEnum;
import com.reece.platform.products.model.entity.Address;
import com.reece.platform.products.model.entity.Delivery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDTO {

    private UUID id;
    private Address address;
    private UUID shipTo;
    private Date preferredDate;
    private PreferredTimeEnum preferredTime;
    private String deliveryInstructions;
    private boolean shouldShipFullOrder;
    private String phoneNumber;

    public DeliveryDTO(GetOrderResponseDTO orderResponseDTO, UUID shipToId) throws ParseException {
        this.id = UUID.randomUUID();
        this.address =
            new Address(
                UUID.randomUUID(),
                orderResponseDTO.getShipToName(),
                orderResponseDTO.getShipAddress().getStreetLineOne(),
                orderResponseDTO.getShipAddress().getStreetLineTwo(),
                orderResponseDTO.getShipAddress().getCity(),
                orderResponseDTO.getShipAddress().getState(),
                orderResponseDTO.getShipAddress().getPostalCode(),
                orderResponseDTO.getShipAddress().getCountry(),
                false
            );
        this.shipTo = shipToId;
    }

    public DeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.address = delivery.getAddress();
        this.shipTo = delivery.getShipToId();
        this.preferredDate = delivery.getPreferredDate();
        this.preferredTime = delivery.getPreferredTime();
        this.deliveryInstructions = delivery.getDeliveryInstructions();
        this.shouldShipFullOrder = delivery.isShouldShipFullOrder();
        this.phoneNumber = delivery.getPhoneNumber();
    }
}
