package com.reece.punchoutcustomerbff.mapper;

import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerRegionMapperTest {

    @BeforeEach
    public void before() {
        new CustomerRegionMapper();
    }

    @Test
    public void whenToDTOsOk() {
        Set<CustomerRegionDao> inputs = TestUtils.getListCustomerRegionDao();
        List<CustomerRegionDto> result = CustomerRegionMapper.toDTOs(inputs);
        TestUtils.assertCustomerRegion(inputs, result);
    }

    @Test
    public void whenToDTOsNOk() {
        List<CustomerRegionDto> result = CustomerRegionMapper.toDTOs(null);
        TestUtils.assertCustomerRegionNotOk(null, result);
    }

    @Test
    public void whenToDTOsNullNOk() {
        Set<CustomerRegionDao> inputs = new HashSet<>();
        List<CustomerRegionDto> result = CustomerRegionMapper.toDTOs(inputs);
        TestUtils.assertCustomerRegion(inputs, result);
    }

    @Test
    public void whenToDTOOk() {
        CustomerRegionDao input = CustomerRegionDao
            .builder()
            .name("customer-region-1")
            .id(UUID.randomUUID())
            .customer(CustomerDao.builder().build())
            .build();
        CustomerRegionDto result = CustomerRegionMapper.toDTO(input);
        Assertions.assertEquals(input.getName(), result.getName());
    }

    @Test
    public void whenToDTONOk() {
        CustomerRegionDao input = CustomerRegionDao.builder().build();
        CustomerRegionDto result = CustomerRegionMapper.toDTO(input);
        Assertions.assertEquals(input.getName(), result.getName());
    }
}
