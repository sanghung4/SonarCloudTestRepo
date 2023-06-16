package com.reece.platform.eclipse.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountDTO {

    private String branchNumber;
    private String countId;
    private String branchName;
}
