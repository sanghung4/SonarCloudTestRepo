package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.model.pojo.ErrorDetails;
import com.reece.specialpricing.model.pojo.ErrorResponse;
import com.reece.specialpricing.model.pojo.SearchResult;
import com.reece.specialpricing.model.pojo.TypeaheadSearchRequest;
import com.reece.specialpricing.service.TypeaheadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/typeahead")
public class TypeaheadController {
    @Autowired
    private TypeaheadService typeaheadService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity queryEntities(
            @Valid PaginationContext paginationContext,
            @Valid TypeaheadSearchRequest searchRequest
    ){
        var errors = paginationContext.validate(false);
        errors.addAll(searchRequest.validate());
        var responseErrors = errors.stream().map(error -> new ErrorDetails(error.getErrorField(), error.getMessage(), error.getExceptionType()))
                .collect(Collectors.toList());
        if(!responseErrors.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorResponse(responseErrors.size(), responseErrors));
        }

        searchRequest.cleanUserInputData();

        if(searchRequest.getQuery().length() < 2){
            var metadata = new HashMap<String, Object>();
            metadata.put("page", 1);
            metadata.put("nextPage", null);
            metadata.put("pageCount", 1);
            metadata.put("pageSize", paginationContext.getPageSize());
            metadata.put("resultCount", 0);
            metadata.put("orderBy", paginationContext.getOrderBy());
            metadata.put("orderDirection", paginationContext.getOrderDirection());
            metadata.put("entity", searchRequest.getEntity());
            metadata.put("query", searchRequest.getQuery());

            return ResponseEntity.ok(
                    new SearchResult(
                            metadata,
                            List.of()
                    )
            );
        }

        var result = typeaheadService.get(
                searchRequest.getEntity(),
                searchRequest.getQuery(),
                paginationContext
        );

        var metadata = new HashMap<String, Object>();
        metadata.put("page", paginationContext.getPage());
        metadata.put("nextPage", result.getNextPage());
        metadata.put("pageCount", result.getPageCount());
        metadata.put("pageSize", paginationContext.getPageSize());
        metadata.put("resultCount", result.getResultCount());
        metadata.put("orderBy", paginationContext.getOrderBy());
        metadata.put("orderDirection", paginationContext.getOrderDirection());
        metadata.put("entity", searchRequest.getEntity());
        metadata.put("query", searchRequest.getQuery());

        return ResponseEntity.ok(
                new SearchResult(
                        metadata,
                        result.getResults()
                )
        );
    }
}
