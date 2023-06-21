package com.reece.platform.eclipse.model.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GeneralContractorDTO extends ContactDTO {
    private String generalContractor;
}
