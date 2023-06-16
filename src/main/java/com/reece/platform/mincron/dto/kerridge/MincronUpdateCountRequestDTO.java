package com.reece.platform.mincron.dto.kerridge;

import java.util.List;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class MincronUpdateCountRequestDTO {
    @Valid
    @NotEmpty(message = "Invalid Request Body: 'updates' are required in request body")
    private List<MincronCountDTO> updates;
}