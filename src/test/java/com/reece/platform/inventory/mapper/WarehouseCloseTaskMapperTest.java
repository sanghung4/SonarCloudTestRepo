package com.reece.platform.inventory.mapper;

import com.reece.platform.inventory.dto.WarehouseCloseTaskDTO;
import com.reece.platform.inventory.dto.WarehouseCloseTaskRequestDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarehouseCloseTaskMapperTest {

    private final WarehouseCloseTaskMapper warehouseCloseTaskMapper = Mappers.getMapper(WarehouseCloseTaskMapper.class);

    @Test
    void givenToWarehouseCloseTaskDTO_whenMaps_thenCorrect() {

        WarehouseCloseTaskRequestDTO closeTaskRequestDTO = new WarehouseCloseTaskRequestDTO("INV123", "BR123", "LOC123", "TOT123", true, false, false);

        WarehouseCloseTaskDTO closeTaskDTO = warehouseCloseTaskMapper.toWarehouseCloseTaskDTO(closeTaskRequestDTO);

        assertEquals(closeTaskRequestDTO.getInvoiceNumber(), closeTaskDTO.getInvoiceNumber());
        assertEquals(closeTaskRequestDTO.getBranchId(), closeTaskDTO.getBranchId());
        assertEquals(closeTaskRequestDTO.getFinalLocation(), closeTaskDTO.getFinalLocation());
    }
}
