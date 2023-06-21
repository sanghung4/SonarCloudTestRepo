package com.reece.platform.eclipse.model.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDTO extends ContactDTO {
    private String jobName;
    private Boolean taxable;
    private String lotNoAndTrack;
    private Float estimatedProjectAmount;
}
