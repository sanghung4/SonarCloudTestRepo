package com.reece.platform.products.pdw.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import com.reece.platform.products.service.SnowflakeQueries;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.JsonPathExpectationsHelper;

@JdbcTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({ "classpath:snowflake_schema.sql", "classpath:snowflake_testdata.sql" })
public class DataWarehouseRepositoryTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate snowflakeTemplate;

    private DataWarehouseRepository dataWarehouseRepository;

    @BeforeEach
    public void setUp() {
        dataWarehouseRepository = new DataWarehouseRepository(objectMapper, snowflakeTemplate);
    }

    @Test
    public void getAllBranches_success() {
        val branches = dataWarehouseRepository.getAllBranches();

        assertNotNull(branches);
        assertEquals(6, branches.size());
        assertEquals("3013", branches.get(0).getBranchId());
    }

    /**
     * Tests that `color/finish` in the database is rendered as `colorfinish`
     * in the output json.
     */
    @Test
    public void getAllProducts_colorSlashFinishMapping() {
        val products = getAllProducts();

        products
            .stream()
            .filter(product -> "MSC-104277".equals(product.getId()))
            .findFirst()
            .ifPresentOrElse(
                product -> {
                    val jsonString = TestUtils.jsonStringify(product);
                    val h = new JsonPathExpectationsHelper("$.colorfinish");
                    h.assertValue(jsonString, product.getColorSlashFinish());
                },
                Assertions::fail
            );
    }

    /**
     * Tests that the boolean mapping for low_lead_compliant_flag works. The
     * value in the databse is 'Yes', which should evaluate to `true`.
     */
    @Test
    public void getAllProducts_lowLeadCompliantFlag() {
        val products = getAllProducts();

        products
            .stream()
            .filter(product -> "MSC-104277".equals(product.getId()))
            .findFirst()
            .ifPresentOrElse(
                product -> {
                    val jsonString = TestUtils.jsonStringify(product);
                    val h = new JsonPathExpectationsHelper("$.low_lead_compliant_flag");
                    h.assertValue(jsonString, true);
                },
                Assertions::fail
            );
    }

    public List<ProductSearchDocument> getAllProducts() {
        return snowflakeTemplate.query(
            "SELECT PRODUCT_JSON FROM ODS.VW_PRODUCT_SEARCH",
            (rs, rowNum) -> {
                try {
                    return objectMapper.readValue(rs.getString(1), ProductSearchDocument.class);
                } catch (JsonProcessingException e) {
                    log.error("Exception caught parsing ProductSearchDocument", e);
                    return new ProductSearchDocument();
                }
            }
        );
    }
}
