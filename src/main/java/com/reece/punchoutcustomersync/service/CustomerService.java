package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.mapper.CatalogMapper;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.repositories.CatalogRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.reece.punchoutcustomerbff.util.CatalogStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service used for general customer related operations.
 * @author john.valentino
 */
@Service
@Slf4j
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  /**
   * This endpoint returns all customers that have a catalog that is eligible for sync,
   * with that single catalog. If a customer has no catalogs that are eligible, that customer
   * is not returned.
   * @return ListCustomersDto A list of customers with their single eligible catalog.
   */
  public ListCustomersDto retrieveCustomersWithEligibleCatalogs() {
    ListCustomersDto result = new ListCustomersDto();

    List<CustomerDao> customers = customerRepository.retrieveAllCustomersWithCatalogs();

    // for each customer...
    for (CustomerDao customer : customers) {
      if (customer.getCatalogs() == null) {
        continue;
      }
      // find the single catalog which is eligible for them
      List<CatalogDao> catalogs = new ArrayList<>();
      catalogs.addAll(customer.getCatalogs());

      CatalogDao catalog = this.determineEligibleCatalog(catalogs);

      if (catalog != null) {
        CustomerDto customerDto = CustomerMapper.toDTO(customer);
        customerDto.setCatalogs(new ArrayList<>());
        customerDto.getCatalogs().add(CatalogMapper.toDTO(catalog));

        result.getCustomers().add(customerDto);
      }
    }

    return result;
  }

  /**
   * Given a list of catalogs in order by the last time they were updated, from most recent
   * to oldest, return the first one in that list that is in the status of SUBMITTED, DRAFT, and then
   * finally ACTIVE.
   * @param catalogs The list of catalogs.
   * @return CatalogDao the selected catalog, or null if one cannot be found.
   */
  protected CatalogDao determineEligibleCatalog(List<CatalogDao> catalogs) {
    // try to find the first occurrence of a catalog in "SUBMITTED" status and return
    // if found.
    Optional<CatalogDao> submittedCatalog = catalogs.stream()
            .filter(c -> c.getStatus().toUpperCase().equals(CatalogStatusUtil.SUBMITTED))
            .findFirst();
    if (submittedCatalog.isPresent()) {
      return submittedCatalog.get();
    }

    // try to find the first occurrence of a catalog in "ACTIVE" status and return if
    // found.
    Optional<CatalogDao> draftCatalog = catalogs.stream()
            .filter(c -> c.getStatus().toUpperCase().equals(CatalogStatusUtil.DRAFT))
            .findFirst();
    if (draftCatalog.isPresent()) {
      return draftCatalog.get();
    }

    // try to find the first occurrence of a catalog in "DRAFT" status and return if
    // found.
    Optional<CatalogDao> activeCatalog = catalogs.stream()
            .filter(c -> c.getStatus().toUpperCase().equals(CatalogStatusUtil.ACTIVE))
            .findFirst();
    if (activeCatalog.isPresent()) {
      return activeCatalog.get();
    }

    // otherwise, return null
    return null;
}

}
