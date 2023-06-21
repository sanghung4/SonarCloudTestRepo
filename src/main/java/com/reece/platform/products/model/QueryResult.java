package com.reece.platform.products.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import lombok.Data;

@Data
public class QueryResult {

    private String filteredResult;
    private HashMap<String, JsonNode> partialAggResults;

    public QueryResult() {}

    public QueryResult(String filteredResult, HashMap<String, JsonNode> partialAggResults) {
        this.filteredResult = filteredResult;
        this.partialAggResults = partialAggResults;
    }
}
