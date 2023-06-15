package com.reece.platform.inventory.external.mincron;

import java.util.List;
import lombok.Data;

@Data
public class MincronPostCountWrapperDTO {

    private List<MincronPostCountDTO> updates;
}
