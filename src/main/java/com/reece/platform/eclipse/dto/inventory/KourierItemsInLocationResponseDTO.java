package com.reece.platform.eclipse.dto.inventory;

import java.util.List;
import lombok.Data;

@Data
public class KourierItemsInLocationResponseDTO {

    private List<KourierLocationDTO> itemsInLocation;
}
