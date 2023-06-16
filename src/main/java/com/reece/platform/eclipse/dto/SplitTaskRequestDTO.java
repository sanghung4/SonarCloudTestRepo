package com.reece.platform.eclipse.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitTaskRequestDTO {

    List<SplitTaskRequest> splitTask;
}
