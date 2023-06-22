package com.reece.punchoutcustomerbff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a record obtained for a row within an input csv file.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InputCsvRecord {

    private String productId;
    private String branch;
    private String customerId;
    private long lineNumber;
}
