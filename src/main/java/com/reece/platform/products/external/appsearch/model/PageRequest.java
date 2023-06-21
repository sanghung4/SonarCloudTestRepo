package com.reece.platform.products.external.appsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class PageRequest {

    Integer size;
    Integer current;
}
