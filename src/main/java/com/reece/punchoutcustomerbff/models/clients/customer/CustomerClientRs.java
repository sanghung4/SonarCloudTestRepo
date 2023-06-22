package com.reece.punchoutcustomerbff.models.clients.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Model of response from AccountService.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class CustomerClientRs {
  private Boolean isBillTo;
  private String companyName;
  private String branchId;
  private String erpAccountId;
  private String erpName;
}
