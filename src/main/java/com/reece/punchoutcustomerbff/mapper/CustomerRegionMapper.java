package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Persistence;

/**
 * Used for mapping to and from CustomerRegion entities and DTOs
 * @author john.valentino
 */
public class CustomerRegionMapper {

    public static List<CustomerRegionDto> toDTOs(Set<CustomerRegionDao> inputs) {
        if (inputs == null || !Persistence.getPersistenceUtil().isLoaded(inputs)) {
            return new ArrayList<>();
        }
        List<CustomerRegionDto> outputs = new ArrayList<>();
        for (CustomerRegionDao input : inputs) {
            outputs.add(CustomerRegionMapper.toDTO(input));
        }
        return outputs;
    }

    public static CustomerRegionDto toDTO(CustomerRegionDao input) {
        UUID customerId = input.getCustomer() != null ? input.getCustomer().getId() : null;
        return CustomerRegionDto.builder().id(input.getId()).name(input.getName()).customerId(customerId).build();
    }

    /**
     * From {@code CustomerRegionDto} to {@code CustomerRegionDao}
     * @param dto instance of {@code CustomerRegionDto} to map to {@code CustomerRegionDao}
     * @return {@code CustomerRegionDao} mapped.
     */
    public static CustomerRegionDao toDao(CustomerRegionDto dto, CustomerDao customerDao) {
        return CustomerRegionDao.builder().id(dto.getId()).customer(customerDao).name(dto.getName()).build();
    }

    public static Set<CustomerRegionDao> toDao(List<CustomerRegionDto> regions, CustomerDao customerDao) {
        return regions.stream().map(region -> toDao(region, customerDao)).collect(Collectors.toSet());
    }
}
