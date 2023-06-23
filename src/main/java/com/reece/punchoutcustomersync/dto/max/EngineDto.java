package com.reece.punchoutcustomersync.dto.max;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EngineDto {

    private String name;

    private String type;

    @SerializedName("source_engines")
    private List<String> sourceEngines;

    @SerializedName("document_count")
    private Integer documentCount;
}
