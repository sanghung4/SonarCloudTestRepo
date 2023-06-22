package com.reece.punchoutcustomerbff.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import com.reece.punchoutcustomerbff.models.repositories.CatalogRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRegionRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.rest.CustomerRest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test about {@code CustomerService}
 *
 * @author john.valentino
 * @see CustomerRest
 * @see CustomerService
 * @see CustomerMapper
 * @see CustomerDto
 * @see CustomerDao
 * @see CustomerRegionDto
 * @see CustomerRegionDao
 * @see CustomerRegionRepository
 * @see CustomerRepository
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private CustomerRegionRepository customerRegionRepository;

    @InjectMocks
    private CustomerService subject;

    @Test
    public void testRetrieveAllCustomers() {
        // given
        List<CustomerDao> customers = List.of(TestUtils.generateCustomerDao());
        when(customerRepository.retrieveAllCustomers()).thenReturn(customers);

        // when
        ListCustomersDto result = subject.retrieveAllCustomers();

        // then
        assertThat(result.getCustomers().size(), equalTo(1));

        // and:
        CustomerDto customer = result.getCustomers().get(0);
        TestUtils.assertDefaultCustomerDto(customer);
    }

    @Test
    public void testDetermineEligibleCatalog() {
        // when: we have one status of each
        CatalogDao one = subject.determineEligibleCatalog(
            List.of(
                CatalogDao.builder().status("SUBMITTED").build(),
                CatalogDao.builder().status("DRAFT").build(),
                CatalogDao.builder().status("ACTIVE").build(),
                CatalogDao.builder().status("ARCHIVED").build()
            )
        );

        // then: SUBMITTED is the first
        assertThat(one.getStatus(), equalTo("SUBMITTED"));

        // when: we have one status of DRAFT and ACTIVE
        CatalogDao two = subject.determineEligibleCatalog(
            List.of(
                CatalogDao.builder().status("DRAFT").build(),
                CatalogDao.builder().status("ACTIVE").build(),
                CatalogDao.builder().status("ARCHIVED").build()
            )
        );

        // then: ACTIVE is the first
        assertThat(two.getStatus(), equalTo("DRAFT"));

        // when: we have one status of ACTIVE
        CatalogDao three = subject.determineEligibleCatalog(
            List.of(CatalogDao.builder().status("ACTIVE").build(), CatalogDao.builder().status("ARCHIVED").build())
        );

        // then: ACTIVE is the first
        assertThat(three.getStatus(), equalTo("ACTIVE"));

        // when: we have one status of ARCHIVED
        CatalogDao four = subject.determineEligibleCatalog(List.of(CatalogDao.builder().status("ARCHIVED").build()));

        // then: no catalog is returned
        assertThat(four, equalTo(null));
    }

    @Test
    public void testRetrieveCustomerDetailsWhenNoCustomers() {
        // given
        String customerId = "b779032c-6fc0-4c71-bbd9-d12874090e16";

        // and: the customer does not exist
        when(customerRepository.findCustomerByIdWithRegions(UUID.fromString(customerId))).thenReturn(List.of());

        // when
        CustomerDto result = subject.retrieveCustomerDetails(customerId);

        // then: there is no customer
        assertThat(result, equalTo(null));
    }

    @Test
    public void testRetrieveCustomerDetailsWhenNoCatalog() {
        // given
        String customerId = "b779032c-6fc0-4c71-bbd9-d12874090e16";

        // and: the customer exists
        CustomerDao customer = TestUtils.generateCustomerDao();
        when(customerRepository.findCustomerByIdWithRegions(UUID.fromString(customerId))).thenReturn(List.of(customer));

        // and: the customer has no eligible catalogs
        when(catalogRepository.retrieveSyncEligibleCatalogs(UUID.fromString(customerId))).thenReturn(List.of());

        // when
        CustomerDto result = subject.retrieveCustomerDetails(customerId);

        // then: there is a customer with no catalog
        TestUtils.assertDefaultCustomerDto(result);
        assertThat(result.getCatalogs(), equalTo(null));
    }

    @Test
    public void testRetrieveCustomerDetailsWhenCatalog() {
        // given
        String customerId = "b779032c-6fc0-4c71-bbd9-d12874090e16";

        // and: the customer exists
        CustomerDao customer = TestUtils.generateCustomerDao();
        when(customerRepository.findCustomerByIdWithRegions(UUID.fromString(customerId))).thenReturn(List.of(customer));

        // and: the customer has eligible catalogs
        CatalogDao catalog = TestUtils.generateCatalog();
        when(catalogRepository.retrieveSyncEligibleCatalogs(UUID.fromString(customerId))).thenReturn(List.of(catalog));

        // when
        CustomerDto result = subject.retrieveCustomerDetails(customerId);

        // then: there is a customer with a catalog
        TestUtils.assertDefaultCustomerDto(result);
        TestUtils.assertDefaultCatalogDto(result.getCatalogs().get(0));
    }

    /**
     * Happy path save a new customer without regions.
     */
    @Test
    public void givenNewCustomerWithoutRegionsWhenSaveThenSaveOk() {
        //given: customer without region.
        CustomerDto customerDto = TestUtils.generateCustomerDto();
        customerDto.setRegions(new ArrayList<>());
        customerDto.setLastUpdate(Timestamp.from(Instant.now()).toString());

        //when save.
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(CustomerMapper.toDao(customerDto));

        //then ok.
        CustomerDto result = subject.save(customerDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(customerDto.getCustomerId(), result.getCustomerId());
        Assertions.assertEquals(customerDto.getRegions(), result.getRegions());
        Assertions.assertEquals(customerDto.getIsBillTo(), result.getIsBillTo());
    }

    /**
     * Happy path save a new customer with regions.
     */
    @Test
    public void givenNewCustomerWithRegionsWhenSaveThenSaveOk() {
        //given: customer with region.
        CustomerDto customerDto = TestUtils.generateCustomerDto();
        customerDto.setLastUpdate(Timestamp.from(Instant.now()).toString());

        //when save.
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(CustomerMapper.toDao(customerDto));
        Mockito.when(customerRegionRepository.save(Mockito.any())).thenReturn(CustomerRegionDao.builder().build());

        //then ok.
        CustomerDto result = subject.save(customerDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(customerDto.getCustomerId(), result.getCustomerId());
        Assertions.assertEquals(customerDto.getRegions(), result.getRegions());
        Assertions.assertEquals(customerDto.getRegions().size(), result.getRegions().size());
        Assertions.assertEquals(customerDto.getRegions().get(0).getName(), result.getRegions().get(0).getName());
        Assertions.assertEquals(
            customerDto.getRegions().get(0).getCustomerId(),
            result.getRegions().get(0).getCustomerId()
        );
        Assertions.assertEquals(customerDto.getId(), result.getRegions().get(0).getCustomerId());
        Assertions.assertEquals(customerDto.getIsBillTo(), result.getIsBillTo());
    }
}
