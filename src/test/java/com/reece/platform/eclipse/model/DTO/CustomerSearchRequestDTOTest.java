package com.reece.platform.eclipse.model.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerSearchRequestDTOTest {

    @Test
    public void validateCustomerSearchRequest_success() {
        CustomerSearchRequestDTO request = new CustomerSearchRequestDTO();
        CustomerSearchRequestDTO.validate(request);
        assertEquals(request.getPageSize(), 25);
        assertEquals(request.getStartIndex(), 1);
    }

    @Test
    public void validateCustomerSearchRequest_exceedMax() {
        CustomerSearchRequestDTO request = new CustomerSearchRequestDTO();
        request.setPageSize(100);
        CustomerSearchRequestDTO.validate(request);
        assertEquals(request.getPageSize(), 25);
        assertEquals(request.getStartIndex(), 1);
    }

    @Test
    public void validateCustomerSearchRequest_belowMin() {
        CustomerSearchRequestDTO request = new CustomerSearchRequestDTO();
        request.setPageSize(-1);
        CustomerSearchRequestDTO.validate(request);
        assertEquals(request.getPageSize(), 25);
        assertEquals(request.getStartIndex(), 1);
    }
}
