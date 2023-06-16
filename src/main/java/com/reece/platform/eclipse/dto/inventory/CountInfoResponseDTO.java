package com.reece.platform.eclipse.dto.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountInfoResponseDTO {

    private List<CountInfoDTO> counts;

    public CountInfoResponseDTO(KourierCountInfoResponseDTO responseDTO) {
        if (responseDTO != null) {
            counts = responseDTO.getCountinfo().stream().map(CountInfoDTO::new).collect(Collectors.toList());
        } else {
            counts = new ArrayList<>();
        }
    }
}
