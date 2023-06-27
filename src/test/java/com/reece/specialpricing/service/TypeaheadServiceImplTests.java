package com.reece.specialpricing.service;

import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.postgres.Customer;
import com.reece.specialpricing.postgres.Product;
import com.reece.specialpricing.postgres.CustomerDataService;
import com.reece.specialpricing.postgres.ProductDataService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TypeaheadServiceImplTests {
    @Mock
    private CustomerDataService customerDataService;

    @Mock
    private ProductDataService productDataService;

    @InjectMocks
    private TypeaheadServiceImpl typeaheadService;

    @BeforeEach
    public void setUp(){
        reset(customerDataService, productDataService);
    }

    @Test
    public void get_shouldSortCustomerResultsAccordingToOrderDirection(){
        var customer1 = new Customer("first", "second");
        var customer2 = new Customer("second", "first");

        when(customerDataService.findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc")).thenReturn(List.of(customer1, customer2));

        var paginationContext = new PaginationContext();
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("asc");
        var result = typeaheadService.get("customer", "abc", paginationContext);

        assert result.getResults().size() == 2;
        assert ((Customer)result.getResults().get(0)).getId().equals("second");
        assert ((Customer)result.getResults().get(1)).getId().equals("first");
        verify(customerDataService, times(1)).findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc");
        verifyNoInteractions(productDataService);

        reset(customerDataService);

        when(customerDataService.findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc")).thenReturn(List.of(customer1, customer2));
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("desc");
        result = typeaheadService.get("customer", "abc", paginationContext);

        assert result.getResults().size() == 2;
        assert ((Customer)result.getResults().get(0)).getId().equals("first");
        assert ((Customer)result.getResults().get(1)).getId().equals("second");
        verify(customerDataService, times(1)).findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc");
        verifyNoInteractions(productDataService);
    }

    @Test
    public void get_shouldSortProductResultsAccordingToOrderDirection(){
        var product1 = new Product("first", "second");
        var product2 = new Product("second", "first");

        when(productDataService.findByIdContainingIgnoreCase("abc")).thenReturn(List.of(product1, product2));

        var paginationContext = new PaginationContext();
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("asc");
        var result = typeaheadService.get("product", "abc", paginationContext);

        assert result.getResults().size() == 2;
        assert ((Product)result.getResults().get(0)).getId().equals("second");
        assert ((Product)result.getResults().get(1)).getId().equals("first");
        verify(productDataService, times(1)).findByIdContainingIgnoreCase("abc");
        verifyNoInteractions(customerDataService);

        reset(productDataService);

        when(productDataService.findByIdContainingIgnoreCase("abc")).thenReturn(List.of(product1, product2));
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("desc");
        result = typeaheadService.get("product", "abc", paginationContext);

        assert result.getResults().size() == 2;
        assert ((Product)result.getResults().get(0)).getId().equals("first");
        assert ((Product)result.getResults().get(1)).getId().equals("second");
        verify(productDataService, times(1)).findByIdContainingIgnoreCase("abc");
        verifyNoInteractions(customerDataService);
    }

    @Test
    public void getPrices_shouldPageResultsAfterOrdering(){
        var customer1 = new Customer("third", "first");
        var customer2 = new Customer("second", "second");
        var customer3 = new Customer("first", "third");

        when(customerDataService.findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc")).thenReturn(List.of(customer1, customer2, customer3));
        var paginationContext = new PaginationContext(1, 2, "id", "asc");

        var result = typeaheadService.get("customer", "abc", paginationContext);
        assert result.getResults().size() == 2;
        assert ((Customer)result.getResults().get(0)).getDisplayName().equals("third");
        assert ((Customer)result.getResults().get(1)).getDisplayName().equals("second");
        assert result.getNextPage() == 2;
        verify(customerDataService, times(1)).findByDisplayNameContainingIgnoreCaseOrIdContainingIgnoreCase("abc", "abc");
        verifyNoInteractions(productDataService);
    }
}
