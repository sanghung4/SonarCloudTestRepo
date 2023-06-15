package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductSerialNumbersResponseDTO {
    private List<ProductSerialNumberDTO> results;
}
