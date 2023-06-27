package com.reece.specialpricing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PagedSearchResults {
    private int pageCount;
    private int resultCount;
    private Integer nextPage;
    private List<Object> results;
}
