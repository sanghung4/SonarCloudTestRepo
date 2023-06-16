package com.reece.platform.eclipse.dto.inventory;

import com.reece.platform.eclipse.dto.UpdateCountDTO;
import java.util.List;
import lombok.Data;

@Data
public class UpdateCountRequestDTO {

    private List<UpdateCountDTO> updates;
}
