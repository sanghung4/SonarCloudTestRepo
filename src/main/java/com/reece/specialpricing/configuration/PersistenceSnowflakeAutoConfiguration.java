package com.reece.specialpricing.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.reece.specialpricing.snowflake",
        entityManagerFactoryRef = "snowflakeEntityManager",
        transactionManagerRef = "snowflakeTransactionManager"
)
public class PersistenceSnowflakeAutoConfiguration {
    @Value("${spring.snowflake-datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${spring.snowflake-datasource.username}")
    private String username;

    @Value("${spring.snowflake-datasource.password}")
    private String password;

    @Value("${spring.snowflake-datasource.driverClassName}")
    private String driverClassName;

    @Autowired
    private Environment env;

    @Bean
    public DataSource snowflakeDataSource() {
            return DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .build();
    }

    @Bean (name= "snowflakeEntityManager")
    public LocalContainerEntityManagerFactoryBean snowflakeEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(snowflakeDataSource());
        em.setPackagesToScan("com.reece.specialpricing.snowflake");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager snowflakeTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                snowflakeEntityManager().getObject());
        return transactionManager;
    }
}
