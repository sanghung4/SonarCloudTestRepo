package com.reece.platform.eclipse.dto.inventory;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class KourierUpdateCountRequestDTO {

    private List<KourierUpdateCountDTO> counts;

    public KourierUpdateCountRequestDTO(String countId, String locationId, UpdateCountRequestDTO updateCountDTO) {
        this.counts =
            updateCountDTO
                .getUpdates()
                .stream()
                .map(c -> new KourierUpdateCountDTO(countId, locationId, c))
                .collect(Collectors.toList());
    }
}
