package com.reece.platform.products.configuration;

import com.reece.platform.products.insite.entity.Customer;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = { "com.reece.platform.products.insite.repository" },
    entityManagerFactoryRef = "insiteEntityManager",
    transactionManagerRef = "insiteTransactionManager"
)
public class InsiteConfig {

    @Bean
    @ConfigurationProperties(prefix = "insite.datasource")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource insiteDataSource() {
        return legacyDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "insiteEntityManager")
    public LocalContainerEntityManagerFactoryBean legacyEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(insiteDataSource()).packages(Customer.class).build();
    }

    @Bean(name = "insiteTransactionManager")
    public PlatformTransactionManager legacyTransactionManager(
        @Qualifier("insiteEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
    ) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }
}
