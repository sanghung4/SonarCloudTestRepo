package com.reece.platform.eclipse.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetailsResponseDTO {
    private String invoiceNumber;
    private String status;
    private String errorCode;
    private String errorMessage;
    private List<String> shippingInstructions;
    private Boolean noBackorder;
    private Boolean noSort;
}
