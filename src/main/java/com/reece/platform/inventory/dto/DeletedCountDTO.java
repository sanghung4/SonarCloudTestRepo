package com.reece.platform.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedCountDTO {

    private Integer countLocations;
    private Integer countLocationItems;
    private Integer countLocationItemQuantities;
    private Integer varianceCountLocationItemQuantities;
    private Integer writeIns;
}
