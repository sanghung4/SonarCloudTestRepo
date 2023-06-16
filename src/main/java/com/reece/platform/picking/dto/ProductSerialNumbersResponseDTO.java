package com.reece.platform.picking.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductSerialNumbersResponseDTO {
    private List<ProductSerialNumberDTO> results;
}
