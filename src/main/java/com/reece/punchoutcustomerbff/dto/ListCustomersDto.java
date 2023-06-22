package com.reece.punchoutcustomerbff.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a generic endpoint response in which the result is a list of customers.
 * @author john.valentino
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ListCustomersDto {

  private List<CustomerDto> customers = new ArrayList<>();

}
