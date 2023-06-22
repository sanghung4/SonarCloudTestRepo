package com.reece.punchoutcustomerbff.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerMapperTest {

    @BeforeEach
    public void before() {
        new CustomerMapper();
    }

    @Test
    public void testToDTOs() {
        // given
        CustomerDao input = TestUtils.generateCustomerDao();

        List<CustomerDao> inputs = List.of(input);

        // when
        List<CustomerDto> results = CustomerMapper.toDTOs(inputs);

        // then
        assertThat(results.size(), equalTo(1));

        // and
        CustomerDto output = results.get(0);
        TestUtils.assertDefaultCustomerDto(output);
    }

    @Test
    public void testToNullDTOs() {
        List<CustomerDto> results = CustomerMapper.toDTOs(null);
        Assertions.assertEquals(results.size(), 0);
        Assertions.assertTrue(results.isEmpty());
    }

    /**
     * Happy path to map from {@code CustomerDto} to {@code CustomerDao}
     *
     * @see CustomerDto
     * @see CustomerDao
     */
    @Test
    public void whenMapToDaoThenOk() {
        CustomerDto input = CustomerDto
            .builder()
            .customerId("12345")
            .branchId("123")
            .id(UUID.randomUUID())
            .isBillTo(Boolean.TRUE)
            .lastUpdate(Timestamp.from(Instant.now()).toString())
            .build();
        CustomerDao result = CustomerMapper.toDao(input);
        Assertions.assertEquals(input.getCustomerId(), result.getCustomerId());
        Assertions.assertEquals(input.getBranchId(), result.getBranchId());
        Assertions.assertEquals(input.getId(), result.getId());
        Assertions.assertEquals(Timestamp.valueOf(input.getLastUpdate()), result.getLastUpdate());
        Assertions.assertTrue(result.getIsBillTo());
    }
}
