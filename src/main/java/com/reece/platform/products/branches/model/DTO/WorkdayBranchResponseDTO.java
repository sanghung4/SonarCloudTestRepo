package com.reece.platform.products.branches.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WorkdayBranchResponseDTO {

    @JsonProperty("Report_Entry")
    private WorkdayBranchDTO[] branches;
}
