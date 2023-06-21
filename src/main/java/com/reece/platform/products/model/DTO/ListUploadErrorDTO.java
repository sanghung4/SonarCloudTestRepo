package com.reece.platform.products.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListUploadErrorDTO {

    private String partNumber;
    private String description;
    private String manufacturerName;
    private Integer quantity;
}
