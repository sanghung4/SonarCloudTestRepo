package com.reece.platform.picking.dto;

import lombok.Data;

import java.util.List;

@Data
public class PickingTasksResponseDTO {
    private List<PickingTaskDTO> results;
}
