package com.reece.punchoutcustomersync.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.util.CatalogStatusUtil;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerService subject;

  @Test
  public void testRetrieveCustomersWithEligibleCatalogsWhenNoCustomers() {
    // given
    new CustomerService();

    // and: there are not any customers
    when(customerRepository.retrieveAllCustomersWithCatalogs()).thenReturn(List.of());

    // when
    ListCustomersDto result = subject.retrieveCustomersWithEligibleCatalogs();

    // then: there are no customers
    assertThat(result.getCustomers().size(), equalTo(0));
  }

  @Test
  public void testRetrieveCustomersWithEligibleCatalogsWhenNullCatalogs() {
    // given: there is a customer with null catalogs
    CustomerDao customer = TestUtils.generateCustomer();
    customer.setCatalogs(null);
    when(customerRepository.retrieveAllCustomersWithCatalogs()).thenReturn(List.of(customer));

    // when
    ListCustomersDto result = subject.retrieveCustomersWithEligibleCatalogs();

    // then: there are no customers
    assertThat(result.getCustomers().size(), equalTo(0));
  }

  @Test
  public void testRetrieveCustomersWithEligibleCatalogsWhenNoCatalogs() {
    // given: there is a customer with empty catalogs
    CustomerDao customer = TestUtils.generateCustomer();
    customer.setCatalogs(Set.of());
    when(customerRepository.retrieveAllCustomersWithCatalogs()).thenReturn(List.of(customer));

    // when
    ListCustomersDto result = subject.retrieveCustomersWithEligibleCatalogs();

    // then: there are no customers
    assertThat(result.getCustomers().size(), equalTo(0));
  }

  @Test
  public void testRetrieveCustomersWithEligibleCatalogsWhenNoEligibleCatalogs() {
    // given: there is a customer with an ineligible catalog
    CustomerDao customer = TestUtils.generateCustomer();

    CatalogDao catalog = TestUtils.generateCatalog();
    catalog.setStatus(CatalogStatusUtil.ARCHIVED);
    customer.setCatalogs(Set.of(catalog));
    when(customerRepository.retrieveAllCustomersWithCatalogs()).thenReturn(List.of(customer));

    // when
    ListCustomersDto result = subject.retrieveCustomersWithEligibleCatalogs();

    // then: there are no customers
    assertThat(result.getCustomers().size(), equalTo(0));
  }

  @Test
  public void testRetrieveCustomersWithEligibleCatalogsWhenEligibleCatalogs() {
    // given: there is a customer with a catalog
    CustomerDao customer = TestUtils.generateCustomer();

    CatalogDao catalog = TestUtils.generateCatalog();
    customer.setCatalogs(Set.of(catalog));
    when(customerRepository.retrieveAllCustomersWithCatalogs()).thenReturn(List.of(customer));

    // when
    ListCustomersDto result = subject.retrieveCustomersWithEligibleCatalogs();

    // then: there are no customers
    assertThat(result.getCustomers().size(), equalTo(1));
    TestUtils.assertDefaultCustomerDto(result.getCustomers().get(0));
    TestUtils.assertDefaultCatalogDto(result.getCustomers().get(0).getCatalogs().get(0));
  }

  @Test
  public void testDetermineEligibleCatalog() {

    // when: we have one status of each
    CatalogDao one = subject.determineEligibleCatalog(List.of(
        CatalogDao.builder().status(CatalogStatusUtil.SUBMITTED).build(),
        CatalogDao.builder().status(CatalogStatusUtil.DRAFT).build(),
        CatalogDao.builder().status(CatalogStatusUtil.ACTIVE).build(),
        CatalogDao.builder().status(CatalogStatusUtil.ARCHIVED).build()));

    // then: SUBMITTED is the first
    assertThat(one.getStatus(), equalTo(CatalogStatusUtil.SUBMITTED));

    // when: we have one status of DRAFT and ACTIVE
    CatalogDao two = subject.determineEligibleCatalog(List.of(
        CatalogDao.builder().status(CatalogStatusUtil.DRAFT).build(),
        CatalogDao.builder().status(CatalogStatusUtil.ACTIVE).build(),
        CatalogDao.builder().status(CatalogStatusUtil.ARCHIVED).build()));

    // then: ACTIVE is the first
    assertThat(two.getStatus(), equalTo(CatalogStatusUtil.DRAFT));

    // when: we have one status of ACTIVE
    CatalogDao three = subject.determineEligibleCatalog(List.of(
        CatalogDao.builder().status(CatalogStatusUtil.ACTIVE).build(),
        CatalogDao.builder().status(CatalogStatusUtil.ARCHIVED).build()));

    // then: ACTIVE is the first
    assertThat(three.getStatus(), equalTo(CatalogStatusUtil.ACTIVE));

    // when: we have one status of ARCHIVED
    CatalogDao four = subject.determineEligibleCatalog(List.of(
        CatalogDao.builder().status(CatalogStatusUtil.ARCHIVED).build()));

    // then: no catalog is returned
    assertThat(four, equalTo(null));

  }

}
