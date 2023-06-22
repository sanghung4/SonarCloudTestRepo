package com.reece.punchoutcustomerbff.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.service.CustomerService;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Customer endpoints test.
 * @author john.valentino
 * @see CustomerRest
 * @see CustomerService
 */
@ExtendWith(MockitoExtension.class)
public class CustomerRestTest {

    /**
     * Mock of customer service to test {@code CustomerRest}.
     */
    @Mock
    private CustomerService customerService;

    /**
     * {@code CustomerRest} where apply the mocks.
     */
    @InjectMocks
    private CustomerRest subject;

    @Test
    public void testListCustomers() {
        // given
        ListCustomersDto mockResult = new ListCustomersDto();
        when(customerService.retrieveAllCustomers()).thenReturn(mockResult);

        // when
        ListCustomersDto result = subject.listCustomers();

        // then
        assertThat(result, equalTo(mockResult));
    }

    @Test
    public void testRetrieveCustomerDetails() {
        // given
        String customerId = "b779032c-6fc0-4c71-bbd9-d12874090e16";

        // and:
        when(customerService.retrieveCustomerDetails(customerId))
            .thenReturn(CustomerMapper.toDTO(TestUtils.generateCustomerDao()));

        // when
        CustomerDto result = subject.retrieveCustomerDetails(customerId);

        // then
        TestUtils.assertDefaultCustomerDto(result);
    }

    /**
     * Happy path about save method.
     */
    @Test
    public void givenNewCustomerWhenSaveThenReturnOk() {
        CustomerDto customer = TestUtils.generateCustomerDto();

        when(customerService.save(customer)).thenReturn(customer);

        CustomerDto result = subject.save(customer);
        TestUtils.assertDefaultCustomerDto(result);
    }
}
