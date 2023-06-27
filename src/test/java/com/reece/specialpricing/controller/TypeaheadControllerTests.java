package com.reece.specialpricing.controller;

import com.reece.specialpricing.model.PagedSearchResults;
import com.reece.specialpricing.postgres.Customer;
import com.reece.specialpricing.service.TypeaheadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TypeaheadControllerTests {
    private MockMvc mockMvc;

    @Mock
    private TypeaheadService typeaheadService;

    @InjectMocks
    private TypeaheadController controller;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new CustomExceptionHandler()).build();
    }

    @BeforeEach
    public void setUp(){
        reset(typeaheadService);
    }

    @Test
    public void GET_getTypeaheadShouldSucceedWithNoPagingParams() throws Exception {
        var returnedType = new Customer("1", "abc");
        var result = new PagedSearchResults(10, 967, 5, List.of(returnedType));
        when(typeaheadService.get(any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "abc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.query").value("abc"))
                .andExpect(jsonPath("$.meta.entity").value("customer"))
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].id").value(returnedType.getId()))
                .andExpect(jsonPath("$.results[0].displayName").value(returnedType.getDisplayName()));

        verify(typeaheadService, times(1)).get(eq("customer"), eq("abc"), any());
    }

    @Test
    public void GET_getTypeaheadShouldSucceedWithEmptyResponseWhenQueryTooShort() throws Exception {
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "a"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.nextPage").doesNotExist())
                .andExpect(jsonPath("$.meta.pageCount").value(1))
                .andExpect(jsonPath("$.meta.resultCount").value(0))
                .andExpect(jsonPath("$.meta.query").value("a"))
                .andExpect(jsonPath("$.meta.entity").value("customer"))
                .andExpect(jsonPath("$.results.size()").value(0));

        verifyNoInteractions(typeaheadService);
    }

    @Test
    public void GET_getTypeaheadShouldSucceedWithPagingParams() throws Exception {
        var returnedType = new Customer("1", "abc");
        var result = new PagedSearchResults(10, 967, 5, List.of(returnedType));
        when(typeaheadService.get(any(), any(), any())).thenReturn(result);
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "abc")
                        .param("page", "4")
                        .param("pageSize", "100")
                        .param("orderBy", "id")
                        .param("orderDirection", "desc"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.meta.page").value(4))
                .andExpect(jsonPath("$.meta.query").value("abc"))
                .andExpect(jsonPath("$.results.size()").value(1))
                .andExpect(jsonPath("$.results[0].displayName").value(returnedType.getDisplayName()))
                .andExpect(jsonPath("$.results[0].id").value(returnedType.getId()));

        verify(typeaheadService, times(1)).get(eq("customer"), eq("abc"), any());
    }

    @Test
    public void GET_getTypeaheadShouldFailWithInvalidJavaxParameter() throws Exception {
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "abc")
                        .param("page", "0")
                        .param("pageSize", "100")
                        .param("orderBy", "something")
                        .param("orderDirection", "desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("page"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'page' is 0 or negative, which is not valid"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(typeaheadService);
    }

    @Test
    public void GET_getTypeaheadShouldFailWithInvalidManualPaginationParameter() throws Exception {
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "abc")
                        .param("pageSize", "100")
                        .param("orderBy", "MANUFACTURER")
                        .param("orderDirection", "desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("orderBy"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'orderBy' not in value array: ['id', 'displayName']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(typeaheadService);
    }

    @Test
    public void GET_getTypeaheadShouldFailWithMultipleInvalidManualPaginationParameter() throws Exception {
        this.mockMvc.perform(get("/v1/typeahead")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("entity", "customer")
                        .param("query", "abc")
                        .param("pageSize", "100")
                        .param("orderBy", "MANUFACTURER")
                        .param("orderDirection", "asc,desc"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(2))
                .andExpect(jsonPath("$.errors.size()").value(2))
                .andExpect(jsonPath("$.errors[0].field").value("orderBy"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'orderBy' not in value array: ['id', 'displayName']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"))
                .andExpect(jsonPath("$.errors[1].field").value("orderDirection"))
                .andExpect(jsonPath("$.errors[1].errorMessage").value("Invalid parameter: 'orderDirection' not in value array: ['asc', 'desc']"))
                .andExpect(jsonPath("$.errors[1].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(typeaheadService);
    }

    @Test
    public void GET_getTypeaheadShouldFailWithInvalidManualQueryParameter() throws Exception {
        this.mockMvc.perform(get("/v1/typeahead")
                        .param("entity", "customer, product")
                        .param("query", "abc")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCount").value(1))
                .andExpect(jsonPath("$.errors.size()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("entity"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Invalid parameter: 'entity' not in value array: ['customer', 'product']"))
                .andExpect(jsonPath("$.errors[0].exceptionType").value("InvalidParameter"));

        verifyNoInteractions(typeaheadService);
    }
}
