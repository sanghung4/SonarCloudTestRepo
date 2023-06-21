package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.generated.WarehousePickComplete;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseTaskUserPicksDTO {
    List<WarehousePickComplete> results;
}
