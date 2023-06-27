package com.reece.specialpricing.snowflake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SnowflakeCustomerDataService extends JpaRepository<SnowflakeCustomer, String>, QuerydslPredicateExecutor<SnowflakeCustomer> {

    @Query(value="ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON';", nativeQuery = true)
    void alterSessionformat();
}
