package com.reece.platform.inventory.dto.kourier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetailsResponseDTO {
    private List<ShippingTextResponseDTO> shippingtext;
}
