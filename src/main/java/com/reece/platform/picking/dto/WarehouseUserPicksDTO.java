package com.reece.platform.picking.dto;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseUserPicksDTO {
    List<WarehouseUserPickDTO> results;
}
