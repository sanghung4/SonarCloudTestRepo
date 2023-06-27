package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class SearchResult {
    private Map<String, Object> meta;
    private List<Object> results;
}
