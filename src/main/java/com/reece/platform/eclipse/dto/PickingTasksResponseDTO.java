package com.reece.platform.eclipse.dto;

import lombok.Data;

import java.util.List;

@Data
public class PickingTasksResponseDTO {
    List<WarehousePickTask> results;
}
