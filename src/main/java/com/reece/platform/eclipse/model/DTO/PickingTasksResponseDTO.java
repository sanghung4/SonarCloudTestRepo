package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.generated.WarehousePickTask;
import lombok.Data;

import java.util.List;

@Data
public class PickingTasksResponseDTO {
    List<WarehousePickTask> results;
}
