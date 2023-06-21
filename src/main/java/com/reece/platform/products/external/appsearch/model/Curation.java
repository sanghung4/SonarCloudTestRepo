package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Curation {

    private String id;
    private List<String> queries = Collections.emptyList();
    private List<String> promoted = Collections.emptyList();
    private List<String> hidden = Collections.emptyList();
}
