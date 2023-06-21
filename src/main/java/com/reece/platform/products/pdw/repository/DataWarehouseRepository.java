package com.reece.platform.products.pdw.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.branches.model.entity.Branch;
import com.reece.platform.products.model.DTO.ProductResponseDTO;
import com.reece.platform.products.pdw.model.ProductSearchDocument;
import com.reece.platform.products.service.SnowflakeQueries;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DataWarehouseRepository {

    private final ObjectMapper objectMapper;
    private final JdbcTemplate snowflakeTemplate;

    public List<ProductSearchDocument> getAllProducts() {
        setJDBCQueryResultSetFormat();
        return snowflakeTemplate.query(
            SnowflakeQueries.Search_All_Products,
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

    public List<ProductSearchDocument> getChangedProducts(LocalDateTime lastUpdateTime) {
        val lastUpdateDate = buildSnowflakeTimestamp(lastUpdateTime);
        setJDBCQueryResultSetFormat();
        return snowflakeTemplate.query(
            SnowflakeQueries.Search_All_Changed_Products,
            (rs, rowNum) -> {
                try {
                    return objectMapper.readValue(rs.getString(1), ProductSearchDocument.class);
                } catch (JsonProcessingException e) {
                    log.error("Exception caught parsing ProductSearchDocument", e);
                    return new ProductSearchDocument();
                }
            },
            lastUpdateDate
        );
    }

    public List<Branch> getAllBranches() {
        return snowflakeTemplate.query(
            "SELECT * FROM MORSCO_EDW.DIALEXA.BRANCH",
            (resultSet, i) -> {
                var branch = new Branch(
                    resultSet.getString("BRANCHID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("ENTITYID"),
                    resultSet.getString("ADDRESS1"),
                    resultSet.getString("ADDRESS2"),
                    resultSet.getString("CITY"),
                    resultSet.getString("STATE"),
                    resultSet.getString("ZIP"),
                    resultSet.getString("PHONE"),
                    resultSet.getString("WEBSITE"),
                    resultSet.getString("WORKDAYID"),
                    resultSet.getString("WORKDAYLOCATIONNAME"),
                    resultSet.getString("RVP"),
                    resultSet.getBoolean("PLUMBING"),
                    resultSet.getBoolean("WATERWORKS"),
                    resultSet.getBoolean("HVAC"),
                    resultSet.getBoolean("BANDK"),
                    resultSet.getString("ACTINGBRANCHMANAGER"),
                    resultSet.getString("ACTINGBRANCHMANAGERPHONE"),
                    resultSet.getString("ACTINGBRANCHMANAGEREMAIL"),
                    resultSet.getString("BUSINESSHOURS")
                );
                return branch;
            }
        );
    }

    public List<ProductResponseDTO> getAllProductsData() {
        setJDBCQueryResultSetFormat();
        return snowflakeTemplate.query(
            SnowflakeQueries.Fetch_All_Products,
            (rs, rowNum) -> {
                try {
                    return objectMapper.readValue(rs.getString(1), ProductResponseDTO.class);
                } catch (JsonProcessingException e) {
                    log.error("Exception caught parsing ProductSearchDocument", e);
                    return new ProductResponseDTO();
                }
            }
        );
    }

    private static Date buildSnowflakeTimestamp(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("America/Los_Angeles")).toInstant());
    }

    public void setJDBCQueryResultSetFormat() {
        snowflakeTemplate.execute("ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON'");
    }
}
