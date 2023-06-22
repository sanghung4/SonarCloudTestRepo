package com.reece.platform.mincron.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MincronJobListDTO {
    private final String RETURN_TABLE = "returnTable";
    private final String CUSTOMER_JOBS = "customerJobs";

    private List<MincronJobListJobDTO> shipTos;

    public MincronJobListDTO (String mincronCustomerJobListResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.shipTos = mapper.readValue(mapper.readTree(mincronCustomerJobListResponse).get(RETURN_TABLE).get(CUSTOMER_JOBS).toString(), new TypeReference<List<MincronJobListJobDTO>>() {});
    }
}

