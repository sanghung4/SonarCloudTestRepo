package com.reece.punchoutcustomerbff.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.dto.UploadOutputDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import com.reece.punchoutcustomerbff.rest.UploadRest;
import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
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
import org.springframework.web.util.NestedServletException;

/**
 * <h1> Integration test for Upload CSV services. </h1>
 * <p>
 * The all integration test about database with all possible cases to test.
 * @author luis.bolivar
 * @see BaseIntegrationTest
 * @see UploadRest
 */

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
public class UploadIntegrationTest extends BaseIntegrationTest {

	/**
	 * Constant with url to update CSV by customerId.
	 */
	private static final String URL_UPLOAD_CSV_BY_CUSTOMER = "/upload/{customerId}";

	/**
	 * Constant with url to delete CSV by id.
	 */
	private static final String URL_DELETE_CSV_BY_ID = "/upload/{uploadId}";


	/**
	 * <h1> Upload CSV with exist product.</h1>
	 * <p>
	 * The POST endpoint of {@code /upload/{customer_id}`}`
	 * is invoked with “Customer 1”'s id
	 * with the above CSV Upload request object sent as the body.
	 *
	 * @author luis.bolivar
	 * @throws Exception test throw exception.
	 */
	@Test
	public void whenTryUploadCsvWithExistProductThenReturnOk () throws Exception {

		// Preparing data.
		this.mockAdminLoggedIn();
		final String partNumber = "1234";
		final String customerId = "269296";
		final String filename = "some_file.csv";
		final String branchId = "3008";

		final String encoded =   Base64.getEncoder().encodeToString(
			("Product ID,Branch,Customer ID\n" + partNumber + "," + branchId + "," + customerId).getBytes());

		final AuthorizedUserDao user = AuthorizedUserDao.builder().email("1").admin(Boolean.TRUE).build();

		final CustomerDao customer = CustomerDao.builder().erpName("ECLIPSE")
			.isBillTo(Boolean.TRUE).customerId(customerId).lastUpdate(Timestamp.from(Instant.now()))
			.erpId("1").contactPhone("213123231").contactName("peter").branchId(branchId)
			.uploads(new HashSet<>()).catalogs(new HashSet<>()).name("peter").regions(new HashSet<>())
			.build();

		final ProductDao product = ProductDao.builder().id(UUID.randomUUID()).partNumber(partNumber).build();
		this.entityManager.persist(user);
		this.entityManager.persist(customer);
		this.entityManager.persist(product);

		// Call the endpoint.
		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.post(URL_UPLOAD_CSV_BY_CUSTOMER,
					customer.getId().toString())
				.header("Origin", "*").header("X-Auth-Token",
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON).content(
					TestUtils.getUploadInputDto(filename, encoded)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		final UploadOutputDto response = new ObjectMapper().readValue(TestUtils.getResponse(rs),
			UploadOutputDto.class);

		Assertions.assertNotNull(response, "is not null response.");
		Assertions.assertNotNull(response.getUploadId(), "Id is not null.");
		Assertions.assertFalse(response.getProducts().isEmpty(),
			"The list products is not empty.");
		Assertions.assertEquals(1, response.getProducts().size(),
			"The size of products is 1.");
		Assertions.assertEquals(partNumber, response.getProducts().get(0).getPartNumber(),
			"The product exist, is not null.");
	}

	/**
	 * <h1> Upload CSV without exist product. </h1>
	 * <p>
	 * The POST endpoint of {@code /upload/{customer_id}`}`
	 * is invoked with “Customer 1”'s id
	 * with the above CSV Upload request object sent as the body.
	 *
	 * @author luis.bolivar
	 * @throws Exception test throw exception.
	 */
	@Test
	public void whenTryUploadCsvWithoutExistProductThenReturnOk () throws Exception {

		// Preparing data.
		this.mockAdminLoggedIn();
		final String partNumber = "1234";
		final String customerId = "269296";
		final String filename = "some_file.csv";
		final String branchId = "3008";

		final String encoded =   Base64.getEncoder().encodeToString(
			("Product ID,Branch,Customer ID\n" + partNumber + "," + branchId + "," + customerId).getBytes());

		final AuthorizedUserDao user = AuthorizedUserDao.builder().email("1").admin(Boolean.TRUE).build();

		final CustomerDao customer = CustomerDao.builder().erpName("ECLIPSE")
			.isBillTo(Boolean.TRUE).customerId(customerId).lastUpdate(Timestamp.from(Instant.now()))
			.erpId("1").contactPhone("213123231").contactName("peter").branchId(branchId)
			.uploads(new HashSet<>()).catalogs(new HashSet<>()).name("peter").regions(new HashSet<>())
			.build();

		this.entityManager.persist(user);
		this.entityManager.persist(customer);

		// Call the endpoint.
		MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.post(URL_UPLOAD_CSV_BY_CUSTOMER,
					customer.getId().toString())
				.header("Origin", "*").header("X-Auth-Token",
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType(MediaType.APPLICATION_JSON).content(
					TestUtils.getUploadInputDto(filename, encoded)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		final UploadOutputDto response = new ObjectMapper().readValue(TestUtils.getResponse(rs),
			UploadOutputDto.class);

		Assertions.assertNotNull(response, "is not null response.");
		Assertions.assertNotNull(response.getUploadId(), "Id is not null.");
		Assertions.assertFalse(response.getProducts().isEmpty(),
			"The list products is not empty.");
		Assertions.assertEquals(1, response.getProducts().size(),
			"The size of products is 1.");
		Assertions.assertEquals(partNumber, response.getProducts().get(0).getPartNumber(),
			"has the part number.");
		Assertions.assertNull(response.getProducts().get(0).getProduct(),
			"the product is not found, is null.");
	}

	/**
	 * <h1> Delete existing CSV. </h1>
	 * <p>
	 *
	 * The DELETE endpoint of {@code /upload/{upload_id}`} is invoked with “Upload A”'s id.
	 *
	 * @author luis.bolivar
	 * @throws Exception test throw exception.
	 */
	@Test
	public void whenTryDeleteCsvExistingThenReturnOk () throws Exception {
		// Preparing data.
		this.mockAdminLoggedIn();
		final UploadDao upload = UploadDao.builder().fileName("some_file.csv").id(UUID.randomUUID())
			.build();
		this.entityManager.persist(upload);

		// Call the endpoint.
		MvcResult call = mockMvc.perform(MockMvcRequestBuilders.delete(URL_DELETE_CSV_BY_ID,
					upload.getId().toString()).header(TestUtils.H_CORS_NAME, TestUtils.H_CORS_VALUE)
				.header(TestUtils.H_AUTH_NAME, SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal())).andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		final UploadDao deletedUpload = this.entityManager.find(UploadDao.class, upload.getId());
		final ResultDto rs = new ObjectMapper().readValue(TestUtils.getResponse(call), ResultDto.class);

		Assertions.assertEquals(200, call.getResponse().getStatus());
		Assertions.assertNotNull(rs, "is not null rs.");
		Assertions.assertTrue(rs.isSuccess(), "The csv was delete correctly.");
		Assertions.assertNull(deletedUpload, "is null, It was deleted.");
	}

	/**
	 * <h1> Delete not existing CSV. </h1>
	 * <p>
	 *
	 * The DELETE endpoint of {@code /upload/{upload_id}`} is invoked with “Upload A”'s id.
	 *
	 * @author luis.bolivar
	 * @throws Exception test throw exception.
	 */
	@Test
	public void whenTryDeleteCsvNotExistingThenReturnOk () throws Exception {
		// Preparing data.
		this.mockAdminLoggedIn();
		MvcResult call = null;
		NestedServletException ex = new NestedServletException("");
		UUID randomId = UUID.randomUUID();

		try{
			// Call the endpoint.
			call = mockMvc.perform(MockMvcRequestBuilders.delete(URL_DELETE_CSV_BY_ID,
						randomId.toString()).header(TestUtils.H_CORS_NAME, TestUtils.H_CORS_VALUE)
					.header(TestUtils.H_AUTH_NAME, SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal()))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andReturn();
		} catch(NestedServletException e) {
			ex = e;
		}

		Assertions.assertNull(call, "The call is null, because throw exception.");
		Assertions.assertNotNull(ex, "is not null the exception.");
		Assertions.assertTrue(ex.getCause().getMessage().contains(randomId.toString()), "The exception contains the id.");
		Assertions.assertTrue(ex.getCause().getMessage()
			.contains("No class com.reece.punchoutcustomerbff.models.daos.UploadDao entity with id"),
			"The exception specify the doesn't exist");
	}
}
