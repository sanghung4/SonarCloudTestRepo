package com.reece.platform.inventory.mapper;

import com.reece.platform.inventory.dto.WarehouseCloseTaskDTO;
import com.reece.platform.inventory.dto.WarehouseCloseTaskRequestDTO;
import com.reece.platform.inventory.dto.WarehouseCloseTaskTotes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseCloseTaskMapper {
    @Mapping(target = "totes", source = "warehouseCloseTaskRequestDTO", qualifiedByName = "getTotesList")
    public WarehouseCloseTaskDTO toWarehouseCloseTaskDTO(WarehouseCloseTaskRequestDTO warehouseCloseTaskRequestDTO);

    @Named("getTotesList")
    default List<WarehouseCloseTaskTotes> getTotesList(WarehouseCloseTaskRequestDTO warehouseCloseTaskRequestDTO) {
        return List.of(new WarehouseCloseTaskTotes(warehouseCloseTaskRequestDTO.getTote(), warehouseCloseTaskRequestDTO.getFinalLocation()));
    }
}
