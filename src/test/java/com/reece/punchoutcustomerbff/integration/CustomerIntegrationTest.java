package com.reece.punchoutcustomerbff.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerRegionDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
public class CustomerIntegrationTest extends BaseIntegrationTest {
	private static final String URL_CUSTOMER_LIST = "/customer/list";
	private static final String URL_CUSTOMER_DETAILS = "/customer/detail/{customerId}";

	/**
	 * Get Customer with two regions ok.
	 *
	 * @throws Exception For mock trying call to endpoint.
	 */
	@Test
	public void whenTryGetCustomerListThenReturnOneCustomerWithTwoRegionsOk() throws Exception {
		this.mockAdminLoggedIn();

		CustomerDao customer = CustomerDao.builder().customerId("VMC-2131").branchId("234")
				.branchName("Santa Monica").contactName("Peter").contactPhone("1233243")
				.erpId("123").erpName("ECLIPSE").isBillTo(Boolean.TRUE).name("Morsco filled")
				.lastUpdate(Timestamp.from(Instant.now()))
				.regions(Set.of(CustomerRegionDao.builder().name("region-1").build(),
						CustomerRegionDao.builder().name("region-2").build()))
				.build();

		this.entityManager.persist(customer);

		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.get(URL_CUSTOMER_LIST)
				.header("Origin", "*").header("X-Auth-Token",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		ListCustomersDto listCustomers = new ObjectMapper().readValue(TestUtils.getResponse(rs),
				ListCustomersDto.class);

		Assertions.assertEquals(2, listCustomers.getCustomers().get(0).getRegions().size(),
				"Customer list returned equals the amount of customer in the db.");
		Assertions.assertEquals(customer.getCustomerId(),
				listCustomers.getCustomers().get(0).getCustomerId(),
				"customerId is the same in database and response.");
		Assertions.assertEquals(customer.getBranchName(),
				listCustomers.getCustomers().get(0).getBranchName(),
				"branchName is the same in database and response.");
		Assertions.assertEquals(customer.getId(), listCustomers.getCustomers().get(0).getId(),
				"id is the same in database and response.");
	}

	/**
	 * Get Customer with two regions ok and one Catalog.
	 *
	 * @throws Exception For mock trying call to endpoint.
	 */
	@Test
	public void whenTryRetrieveCustomerInfoThenReturnCustomerWithTwoRegionsACatalogWithTwoProductsOk()
			throws Exception {
		this.mockAdminLoggedIn();

		CustomerDao customer = CustomerDao.builder().customerId("VMC-2131").branchId("234")
				.branchName("Santa Monica").contactName("Peter").contactPhone("1233243")
				.erpId("123").erpName("ECLIPSE").isBillTo(Boolean.TRUE).name("Morsco filled")
				.lastUpdate(Timestamp.from(Instant.now()))
				.regions(Set.of(CustomerRegionDao.builder().name("region-1").build(),
						CustomerRegionDao.builder().name("region-2").build()))
				.build();

		this.entityManager.persist(customer);

		CatalogDao catalog = CatalogDao.builder()
				.status("ACTIVE")
				.fileName("19may23-sadasd21.csv")
				.name("Active Catalog")
				.lastUpdate(Timestamp.from(Instant.now()))
				.dateArchived(Timestamp.from(Instant.now()))
				.customer(customer)
				.id(UUID.randomUUID())
				.build();

		this.entityManager.persist(catalog);

		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.get(URL_CUSTOMER_DETAILS, customer.getId())
				.header("Origin", "*").header("X-Auth-Token",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		CustomerDto customerRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CustomerDto.class);

		Assertions.assertEquals(2, customerRs.getRegions().size(), "Amount of regions.");
		Assertions.assertNotNull(customerRs.getCatalogs(), "Customer has catalogs");
		Assertions.assertEquals(1, customerRs.getCatalogs().size(), "Amount of catalogs.");
		Assertions.assertEquals(customer.getCustomerId(), customerRs.getCustomerId(),
				"CustomerId is the same in database and response.");
		Assertions.assertEquals(customer.getBranchName(), customerRs.getBranchName(),
				"BranchName is the same in database and response.");
		Assertions.assertEquals(customer.getId(), customerRs.getId(),
				"Id is the same in database and response.");
	}

	/**
	 * Get Customer with two catalogs. 1 Catalog in ACTIVE status and another in
	 * SUBMITTED status. Only return catalog in SUBMITTED status
	 *
	 * @throws Exception For mock trying call to endpoint.
	 */
	@Test
	public void whenTryRetrieveCustomerInfoWithTwoCatalogsThenReturnSubmittedCatalog()
			throws Exception {
		this.mockAdminLoggedIn();

		CustomerDao customer = CustomerDao.builder().customerId("VMC-2131").branchId("234")
				.branchName("Santa Monica").contactName("Peter").contactPhone("1233243")
				.erpId("123").erpName("ECLIPSE").isBillTo(Boolean.TRUE).name("Morsco filled")
				.lastUpdate(Timestamp.from(Instant.now()))
				.regions(Set.of(CustomerRegionDao.builder().name("region-1").build(),
						CustomerRegionDao.builder().name("region-2").build()))
				.build();

		this.entityManager.persist(customer);

		CatalogDao activeCatalog = CatalogDao.builder().id(UUID.randomUUID()).status("ACTIVE").fileName("19may23-sadasd22.csv")
				.name("Active Catalog").lastUpdate(Timestamp.from(Instant.now()))
				.dateArchived(Timestamp.from(Instant.now())).customer(customer).build();

		CatalogDao submittedCatalog = CatalogDao.builder().id(UUID.randomUUID()).status("SUBMITTED").fileName("19may23-sadasd21.csv")
				.name("Submitted Catalog").lastUpdate(Timestamp.from(Instant.now()))
				.dateArchived(Timestamp.from(Instant.now())).customer(customer).build();

		this.entityManager.persist(activeCatalog);
		this.entityManager.persist(submittedCatalog);

		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.get(URL_CUSTOMER_DETAILS, customer.getId())
				.header("Origin", "*").header("X-Auth-Token",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		CustomerDto customerRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CustomerDto.class);

		Assertions.assertNotNull(customerRs.getCatalogs(), "Customer has a catalog");
		Assertions.assertEquals(1, customerRs.getCatalogs().size(), "Customer has only 1 catalog");
		Assertions.assertEquals("SUBMITTED", customerRs.getCatalogs().get(0).getStatus(),
				"Customer catalog is in submitted status");
	}


	/**
	 * Get Customer with two catalogs. 1 Catalog in ACTIVE status and another in
	 * DRAFT status. Only return catalog in ACTIVE status
	 *
	 * @throws Exception For mock trying call to endpoint.
	 */
	@Test
	public void whenTryRetrieveCustomerInfoWithTwoCatalogsThenReturnActiveCatalog()
			throws Exception {
		this.mockAdminLoggedIn();

		CustomerDao customer = CustomerDao.builder().customerId("VMC-2131").branchId("234")
				.branchName("Santa Monica").contactName("Peter").contactPhone("1233243")
				.erpId("123").erpName("ECLIPSE").isBillTo(Boolean.TRUE).name("Morsco filled")
				.lastUpdate(Timestamp.from(Instant.now()))
				.regions(Set.of(CustomerRegionDao.builder().name("region-1").build(),
						CustomerRegionDao.builder().name("region-2").build()))
				.build();

		this.entityManager.persist(customer);

		CatalogDao draftCatalog = CatalogDao.builder().id(UUID.randomUUID()).status("DRAFT").fileName("19may23-sadasd21.csv")
				.name("Draft Catalog").lastUpdate(Timestamp.from(Instant.now()))
				.dateArchived(Timestamp.from(Instant.now())).customer(customer).build();

		CatalogDao activeCatalog = CatalogDao.builder().id(UUID.randomUUID()).status("ACTIVE").fileName("19may23-sadasd22.csv")
				.name("Active Catalog").lastUpdate(Timestamp.from(Instant.now()))
				.dateArchived(Timestamp.from(Instant.now())).customer(customer).build();

		this.entityManager.persist(activeCatalog);
		this.entityManager.persist(draftCatalog);

		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.get(URL_CUSTOMER_DETAILS, customer.getId())
				.header("Origin", "*").header("X-Auth-Token",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		CustomerDto customerRs = new ObjectMapper().readValue(TestUtils.getResponse(rs), CustomerDto.class);

		Assertions.assertNotNull(customerRs.getCatalogs(), "Customer has a catalog");
		Assertions.assertEquals(1, customerRs.getCatalogs().size(), "Customer has only 1 catalog");
		Assertions.assertEquals("DRAFT", customerRs.getCatalogs().get(0).getStatus(),
				"Customer catalog is in active status");
	}
}
