package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpecialPriceChangeSuccessfulUpload {
    private String uploadedPath;
    private String uploadedName;
}
