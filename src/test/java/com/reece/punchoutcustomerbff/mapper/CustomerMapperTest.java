package com.reece.punchoutcustomerbff.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.List;
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
    CustomerDao input = TestUtils.generateCustomer();

    List<CustomerDao> inputs = List.of(input);

    // when
    List<CustomerDto> results = CustomerMapper.toDTOs(inputs);

    // then
    assertThat(results.size(), equalTo(1));

    // and
    CustomerDto output = results.get(0);
    TestUtils.assertDefaultCustomerDto(output);

  }

}
