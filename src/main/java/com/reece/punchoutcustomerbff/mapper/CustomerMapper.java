package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.util.DateUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 * A class used for entity and DTO mappings
 * @author john.valentino
 * @see  CustomerDto
 * @see CustomerDao
 */
public class CustomerMapper {

    public static List<CustomerDto> toDTOs(List<CustomerDao> inputs) {
        if (inputs == null || !Persistence.getPersistenceUtil().isLoaded(inputs)) {
            return new ArrayList<>();
        }
        List<CustomerDto> outputs = new ArrayList<>();
        for (CustomerDao input : inputs) {
            outputs.add(CustomerMapper.toDTO(input));
        }
        return outputs;
    }

    public static CustomerDto toDTO(CustomerDao input) {
        return CustomerDto
            .builder()
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
    }

    /**
     * From {@code CustomerDto} to {@code CustomerDao}
     * @param dto, {@code CustomerDto} to map to {@code CustomerDao}
     * @return {@code CustomerDao}, to use in db.
     * @see CustomerDto
     * @see CustomerDao
     */
    public static CustomerDao toDao(CustomerDto dto) {
        return CustomerDao
            .builder()
            .id(dto.getId())
            .erpId(dto.getErpId())
            .name(dto.getName())
            .branchName(dto.getBranchName())
            .contactName(dto.getContactName())
            .contactPhone(dto.getContactPhone())
            .erpName(dto.getErpName())
            .customerId(dto.getCustomerId())
            .isBillTo(dto.getIsBillTo())
            .lastUpdate(Timestamp.valueOf(dto.getLastUpdate()))
            .branchId(dto.getBranchId())
            .customerId(dto.getCustomerId())
            .build();
    }
}
