package com.reece.platform.eclipse.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EclipseRestSessionDTO implements Serializable {
    @JsonProperty("sessionToken")
    private String sessionToken;
}
