package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response of doing a CSV upload.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UploadOutputDto {

    private String uploadId;
    private String error;
    private List<CatalogProductDto> products = new ArrayList<>();
}
