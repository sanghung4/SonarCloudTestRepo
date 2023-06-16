package com.reece.platform.eclipse.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KourierShippingDetailsResponseDTO {
    private List<ShippingDetailsResponseDTO> shippingtext;
}
