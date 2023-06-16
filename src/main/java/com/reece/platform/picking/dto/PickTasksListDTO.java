package com.reece.platform.picking.dto;

import lombok.Data;

import java.util.List;

@Data
public class PickTasksListDTO {
    private List<PickingTaskDTO> warehousePickTasksList;
    private List<PickingTaskWarningDTO> warehousePickTasksWarnings;
}
