package com.reece.platform.eclipse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EclipseRestSessionDTO implements Serializable {

    @JsonProperty("sessionToken")
    private String sessionToken;
}
