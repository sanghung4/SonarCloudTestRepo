package com.reece.platform.mincron.dto;

import com.reece.platform.mincron.dto.kerridge.CountLocationResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDTO {

    private String locationId;
    private Integer totalQuantity;
    private Integer itemCount;
    private List<LocationItemDTO> items;
    private boolean additionalItemsExist; // not used

    public LocationDTO(CountLocationResponseDTO responseDTO, String locationId) {
        this.locationId = locationId;
        this.totalQuantity = responseDTO.getTotalqty();
        this.itemCount = responseDTO.getItemcount();
        this.items = new ArrayList<>();
        for (var item : responseDTO.getResults()) {
            this.items.add(new LocationItemDTO(item));
        }
    }
}
