package com.reece.platform.products.external.appsearch.model;

import java.util.List;
import lombok.Data;

@Data
public class IndexDocumentsResponse {

    private String id;
    private List<String> errors;
}
