package com.reece.platform.eclipse.dto.productsearch;

import com.reece.platform.eclipse.dto.EclipseAddCountDTO;
import java.util.List;
import lombok.Data;

@Data
public class EclipseAddCountDTORequest {

    List<EclipseAddCountDTO> counts;
}
