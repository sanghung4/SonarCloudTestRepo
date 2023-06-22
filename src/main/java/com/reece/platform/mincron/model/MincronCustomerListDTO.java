package com.reece.platform.mincron.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model for Mincron customer list
 */
public class MincronCustomerListDTO {
    final String RETURN_TABLE = "returnTable";
    final String CUSTOMERS = "customers";

    public List<MincronCustomerListCustomerDTO> customers;

    public MincronCustomerListDTO() {
        this.customers = new ArrayList<>();
    }

    public MincronCustomerListDTO (String mincronCustomerListResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.customers = mapper.readValue(mapper.readTree(mincronCustomerListResponse).get(RETURN_TABLE).get(CUSTOMERS).toString(), new TypeReference<List<MincronCustomerListCustomerDTO>>() {});
    }

    public List<MincronCustomerListCustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<MincronCustomerListCustomerDTO> customers) {
        this.customers = customers;
    }
}
