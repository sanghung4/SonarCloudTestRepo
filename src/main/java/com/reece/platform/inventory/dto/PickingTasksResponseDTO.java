package com.reece.platform.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class PickingTasksResponseDTO {
    private List<PickingTaskDTO> results;
}
