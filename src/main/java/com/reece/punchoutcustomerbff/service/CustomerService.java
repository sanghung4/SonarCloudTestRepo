package com.reece.punchoutcustomerbff.service;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.CustomerRegionDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.mapper.CatalogMapper;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.mapper.CustomerRegionMapper;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import com.reece.punchoutcustomerbff.models.repositories.CatalogRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRegionRepository;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.rest.CustomerRest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service used for general customer related operations.
 *
 * @author john.valentino
 */
@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CustomerRegionRepository customerRegionRepository;

    /**
     * Returns a list of all current customers in alphabetical order by name, with
     * their
     * territories.
     *
     * @return ListCustomersDto A list of customers.
     */
    public ListCustomersDto retrieveAllCustomers() {
        ListCustomersDto result = new ListCustomersDto();

        List<CustomerDao> customers = customerRepository.retrieveAllCustomers();
        result.setCustomers(CustomerMapper.toDTOs(customers));

        return result;
    }

    /**
     * Returns the details of a customer, which includes their appropriate single
     * catalog
     * if it exists. That means the the most recent catalog in the status of
     * Submitted, then Draft,
     * then Active.
     *
     * @param customerId The ID of the customer.
     * @return CustomerDto Customer details that include region and a single
     *         catalog.
     */
    public CustomerDto retrieveCustomerDetails(String customerId) {
        List<CustomerDao> customers = customerRepository.findCustomerByIdWithRegions(UUID.fromString(customerId));

        if (customers.size() == 0) {
            return null;
        }

        CustomerDto result = CustomerMapper.toDTO(customers.get(0));

        // pull all the eligible catalogs for the customer
        List<CatalogDao> catalogs = catalogRepository.retrieveSyncEligibleCatalogs(UUID.fromString(customerId));

        CatalogDao catalog = this.determineEligibleCatalog(catalogs);

        if (catalog == null) {
            return result;
        }

        result.setCatalogs(List.of(CatalogMapper.toDTO(catalog)));

        return result;
    }

    /**
     * Given a list of catalogs in order by the last time they were updated, from
     * most recent
     * to oldest, return the first one in that list that is in the status of
     * SUBMITTED, ACTIVE, and then
     * finally DRAFT.
     *
     * @param catalogs The list of catalogs.
     * @return CatalogDao the selected catalog, or null if one cannot be found.
     */
    protected CatalogDao determineEligibleCatalog(List<CatalogDao> catalogs) {
        // try to find the first occurence of a catalog in "SUBMITTED" status and return
        // if found.
        Optional<CatalogDao> submittedCatalog = catalogs
            .stream()
            .filter(c -> c.getStatus().toUpperCase().equals("SUBMITTED"))
            .findFirst();
        if (submittedCatalog.isPresent()) {
            return submittedCatalog.get();
        }

        // try to find the first occurence of a catalog in "ACTIVE" status and return if
        // found.
        Optional<CatalogDao> activeCatalog = catalogs
            .stream()
            .filter(c -> c.getStatus().toUpperCase().equals("DRAFT"))
            .findFirst();
        if (activeCatalog.isPresent()) {
            return activeCatalog.get();
        }

        // try to find the first occurence of a catalog in "DRAFT" status and return if
        // found.
        Optional<CatalogDao> draftCatalog = catalogs
            .stream()
            .filter(c -> c.getStatus().toUpperCase().equals("ACTIVE"))
            .findFirst();
        if (draftCatalog.isPresent()) {
            return draftCatalog.get();
        }

        // otherwise, return null
        return null;
    }

    /**
     * Service that save a new customer.
     *
     * @see CustomerRest
     * @see CustomerDto
     * @see CustomerRegionDto
     * @param customer request input.
     * @return CustomerDto, information saved about new customer.
     */
    public CustomerDto save(CustomerDto customer) {
        //save customer.
        customer.setLastUpdate(Timestamp.from(Instant.now()).toString());
        CustomerDao customerDao = CustomerMapper.toDao(customer);
        customerRepository.save(customerDao);

        //save regions.
        customer
            .getRegions()
            .forEach(region -> {
                CustomerRegionDao customerRegionDao = CustomerRegionMapper.toDao(region, customerDao);
                customerRegionRepository.save(customerRegionDao);
                region.setId(customerRegionDao.getId());
                region.setCustomerId(customerDao.getId());
            });

        customerDao.setRegions(CustomerRegionMapper.toDao(customer.getRegions(), customerDao));
        return CustomerMapper.toDTO(customerDao);
    }
}
