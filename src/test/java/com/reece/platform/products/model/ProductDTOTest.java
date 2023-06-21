package com.reece.platform.products.model;

import static org.junit.jupiter.api.Assertions.*;

import com.reece.platform.products.model.DTO.ProductDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class ProductDTOTest {

    private ProductDTO dto;

    @Test
    void setTechnicalDocument_addsBaseUrl() {
        ProductDTO productDto = new ProductDTO();
        List<TechDoc> techDocs = new ArrayList<TechDoc>();
        techDocs.add(new TechDoc("Test", "http://test1"));
        techDocs.add(new TechDoc("Test", "test2"));
        productDto.setTechnicalDocuments(techDocs);
        System.out.println(productDto.getTechnicalDocuments().get(1).getUrl());
        assertEquals(
            productDto
                .getTechnicalDocuments()
                .stream()
                .filter(doc -> doc.getUrl().contains("http://test1"))
                .collect(Collectors.toList())
                .size(),
            1
        );
        assertEquals(
            productDto
                .getTechnicalDocuments()
                .stream()
                .filter(doc -> doc.getUrl().contains("http://test2"))
                .collect(Collectors.toList())
                .size(),
            1
        );
    }
}
