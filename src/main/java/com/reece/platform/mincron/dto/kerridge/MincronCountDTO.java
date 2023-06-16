package com.reece.platform.mincron.dto.kerridge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MincronCountDTO {

    @NotBlank(message = "Invalid Parameter: 'location' is blank, which is not valid")
    private String location;
    private String tag;
    private Integer qty;

}