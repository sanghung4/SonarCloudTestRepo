package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

@Data
@Getter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@EqualsAndHashCode
public class SearchRequest {

    private String query = "";
    private PageRequest page;
    private Map<String, List<Facet>> facets;
    private Map<String, Object> filters;

    @JsonProperty("search_fields")
    private Map<String, SearchField> searchFields;

    @JsonProperty("result_fields")
    private Map<String, ResultField> resultFields;

    public SearchRequest query(String query) {
        this.query = query;
        return this;
    }

    public SearchRequest page(PageRequest page) {
        this.page = page;
        return this;
    }

    public SearchRequest facet(String fieldKey, Facet facet) {
        if (facets == null) {
            facets = new HashMap<>();
        }

        if (facets.containsKey(fieldKey)) {
            facets.get(fieldKey).add(facet);
        } else {
            val facetList = new ArrayList<Facet>();
            facetList.add(facet);
            facets.put(fieldKey, facetList);
        }

        return this;
    }

    public SearchRequest filter(Filter.Combination combination) {
        if (filters == null) {
            filters = new HashMap<>();
        }

        filters.put(combination.getType().lowercase(), combination.getValues());

        return this;
    }

    public SearchRequest searchField(String fieldName, SearchField searchField) {
        if (searchFields == null) {
            searchFields = new HashMap<>();
        }

        searchFields.put(fieldName, searchField);

        return this;
    }

    public SearchRequest resultField(String fieldName, ResultField resultField) {
        if (resultFields == null) {
            resultFields = new HashMap<>();
        }

        resultFields.put(fieldName, resultField);

        return this;
    }
}
