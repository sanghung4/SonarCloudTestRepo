package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used for entity and DTO mappings
 * @author john.valentino
 */
public class CustomerMapper {

  public static List<CustomerDto> toDTOs(List<CustomerDao> inputs) {
    List<CustomerDto> outputs = new ArrayList<>();
    for (CustomerDao input : inputs) {
      outputs.add(CustomerMapper.toDTO(input));
    }
    return outputs;
  }

  public static CustomerDto toDTO(CustomerDao input) {
    CustomerDto output = CustomerDto.builder()
        .id(input.getId())
        .erpId(input.getErpId())
        .name(input.getName())
        .branchName(input.getBranchName())
        .contactName(input.getContactName())
        .contactPhone(input.getContactPhone())
        .erpName(input.getErpName())
        .customerId(input.getCustomerId())
        .isBillTo(input.getIsBillTo())
        .lastUpdate(DateUtil.fromDate(input.getLastUpdate()))
        .branchId(input.getBranchId())
        .customerId(input.getCustomerId())
        .regions(CustomerRegionMapper.toDTOs(input.getRegions()))
        .build();
    return output;
  }
}
