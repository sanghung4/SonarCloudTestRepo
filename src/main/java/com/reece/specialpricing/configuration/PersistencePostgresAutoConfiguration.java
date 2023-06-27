package com.reece.specialpricing.configuration;

import liquibase.integration.spring.SpringLiquibase;
import net.lbruun.springboot.preliquibase.PreLiquibase;
import net.lbruun.springboot.preliquibase.PreLiquibaseAutoConfiguration;
import net.lbruun.springboot.preliquibase.PreLiquibaseDataSourceProvider;
import net.lbruun.springboot.preliquibase.PreLiquibaseProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
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
        basePackages = "com.reece.specialpricing.postgres",
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
public class PersistencePostgresAutoConfiguration {
    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appContext;

    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresDataSource());
        em.setPackagesToScan(
                "com.reece.specialpricing.postgres");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager postgresTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                postgresEntityManager().getObject());
        return transactionManager;
    }

    @Primary
    @Bean
    public DataSourceProperties postgresDataSourceProperties() {

        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public PreLiquibaseProperties postgresPreLiquibaseProperties (){
        return new PreLiquibaseProperties();
    }

    @Primary
    @Bean
    @DependsOn("postgresDataSource")
    public PreLiquibase postgresPreLiquibase (){
        var x = new PreLiquibaseAutoConfiguration();
        var dataSourceProvider = x.preLiquibaseDataSourceProvider(
                postgresLiquibaseProperties(),
                postgresDataSourceProperties(),
                appContext.getBeanProvider(DataSource.class),
                appContext.getBeanProvider(DataSource.class)
        );
        return x.preLiquibase(env, postgresPreLiquibaseProperties(), dataSourceProvider, appContext);
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties postgresLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Primary
    @Bean
    @DependsOn("postgresPreLiquibase")
    public SpringLiquibase springLiquibase() {
        return springLiquibase(postgresDataSource(), postgresLiquibaseProperties());
    }


    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }
}
