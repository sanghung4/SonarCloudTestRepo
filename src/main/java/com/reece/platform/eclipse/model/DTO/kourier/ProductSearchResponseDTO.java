package com.reece.platform.eclipse.model.DTO.kourier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponseDTO {
    public ArrayList<ProductSearchResponse> prodSearch;
}