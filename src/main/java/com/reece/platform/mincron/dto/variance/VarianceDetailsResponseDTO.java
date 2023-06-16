package com.reece.platform.mincron.dto.variance;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VarianceDetailsResponseDTO {
    @JsonAlias("VarInfo")
    private List<VarianceDetails> counts;
}
