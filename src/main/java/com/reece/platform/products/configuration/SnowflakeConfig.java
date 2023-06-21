package com.reece.platform.products.configuration;

import javax.sql.DataSource;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SnowflakeConfig {

    @Bean
    @ConfigurationProperties(prefix = "snowflake.datasource")
    public DataSourceProperties snowflakeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource snowflakeDataSource() {
        return snowflakeDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate snowflakeTemplate() {
        return new JdbcTemplate(snowflakeDataSource());
    }
}
