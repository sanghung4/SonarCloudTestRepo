package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.model.DTO.SearchSuggestionResponseDTO;
import com.reece.platform.products.search.SearchService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchSuggestionsController.class)
public class SearchSuggestionsControllerTest {

    private static final SearchSuggestionResponseDTO TEST_SUGGESTION_RESPONSE = TestUtils.loadResponseJson(
        "search-suggestion-response.json",
        SearchSuggestionResponseDTO.class
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        when(searchService.getSuggestedSearch(anyString(), anyString(), anyInt(), any()))
            .thenReturn(TEST_SUGGESTION_RESPONSE);
    }

    @Test
    public void testGetSuggestedSearch_success() throws Exception {
        mockMvc
            .perform(get("/search-suggestions").queryParam("searchTerm", "c").queryParam("engine", "plumbing_hvac"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.suggestions[0]").value("charcoal"))
            .andExpect(jsonPath("$.topCategories[0].value").value("Faucets, Fixtures & Appliances"))
            .andExpect(jsonPath("$.topProducts[0].id").value("MSC-103687"));
    }

    @Test
    public void testGetSuggestedSearch_noSizeParam() throws Exception {
        mockMvc
            .perform(get("/search-suggestions").queryParam("searchTerm", "c").queryParam("engine", "plumbing_hvac"))
            .andExpect(status().isOk());

        val sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(searchService, times(1)).getSuggestedSearch(anyString(), anyString(), sizeCaptor.capture(), any());

        assertEquals(7, sizeCaptor.getValue());
    }

    @Test
    public void testGetSuggestedSearch_noSearchTerm() throws Exception {
        mockMvc
            .perform(get("/search-suggestions").queryParam("engine", "plumbing_hvac"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("getSuggestedSearch.searchTerm"))
            .andExpect(jsonPath("$.violations[0].message").value("must not be blank"));
    }

    @Test
    public void testGetSuggestedSearch_sizeTooLow() throws Exception {
        mockMvc
            .perform(
                get("/search-suggestions")
                    .queryParam("size", "-1")
                    .queryParam("searchTerm", "c")
                    .queryParam("engine", "plumbing_hvac")
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("getSuggestedSearch.size"))
            .andExpect(jsonPath("$.violations[0].message").value("must be greater than or equal to 0"));
    }

    @Test
    public void testGetSuggestedSearch_sizeTooHigh() throws Exception {
        mockMvc
            .perform(
                get("/search-suggestions")
                    .queryParam("size", "21")
                    .queryParam("searchTerm", "c")
                    .queryParam("engine", "plumbing_hvac")
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("getSuggestedSearch.size"))
            .andExpect(jsonPath("$.violations[0].message").value("must be less than or equal to 20"));
    }

    @Test
    public void testGetSuggestedSearch_noEngine() throws Exception {
        mockMvc
            .perform(get("/search-suggestions").queryParam("searchTerm", "c"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Constraint Violation"))
            .andExpect(jsonPath("$.violations[0].field").value("getSuggestedSearch.engine"))
            .andExpect(jsonPath("$.violations[0].message").value("must not be blank"));
    }
}
