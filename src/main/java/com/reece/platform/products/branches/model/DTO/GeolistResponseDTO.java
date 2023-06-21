package com.reece.platform.products.branches.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeolistResponseDTO {

    Float latitude;
    Float longitude;
    List<BranchResponseDTO> branches;
}
