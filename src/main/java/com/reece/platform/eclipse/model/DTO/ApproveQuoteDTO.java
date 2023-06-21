package com.reece.platform.eclipse.model.DTO;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ApproveQuoteDTO extends SalesOrderDTO {
    @Valid
    @NotBlank(message = "quoteId is required")
    private String quoteId;
}
