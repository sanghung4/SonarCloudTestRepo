package com.reece.platform.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MincronJobListJobDTO {
    private String customerJobNumber;
    private AddressDTO address;
    private BranchDTO branch;


    public MincronJobListJobDTO() {};

    public MincronJobListJobDTO (String mincronCustomerJobResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.address = mapper.readValue(mapper.readTree(mincronCustomerJobResponse).toString(), new TypeReference<AddressDTO>() {});
        this.branch = mapper.readValue(mapper.readTree(mincronCustomerJobResponse).toString(), new TypeReference<BranchDTO>() {});
    }
}

