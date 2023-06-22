package com.reece.punchoutcustomerbff.models.repositories;

import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Connection of Customer with the Database.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerDao, UUID> {

  @Query("select distinct c from CustomerDao c left join fetch c.regions order by c.name")
  List<CustomerDao> retrieveAllCustomers();

  /**
   * This is used to lookup a customer by their ID, and include their regions in the result.
   * @param customerId The ID of the customer
   * @return List<CustomerDao> Either one or zero results.
   */
  @Query("select distinct c from CustomerDao c left join fetch c.regions where c.id = ?1 order by c.name")
  List<CustomerDao> findCustomerByIdWithRegions(UUID customerId);

}
