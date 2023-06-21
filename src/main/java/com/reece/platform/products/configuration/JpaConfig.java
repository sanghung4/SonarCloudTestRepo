package com.reece.platform.products.configuration;

import com.reece.platform.products.branches.model.entity.Branch;
import com.reece.platform.products.model.entity.Address;
import com.reece.platform.products.utilities.DecodedToken;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.reece.platform.products.model.repository", "com.reece.platform.products.branches.model.repository",
    }
)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource()).packages(Address.class, Branch.class).build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
        @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new MaxAuditorAware();
    }

    static class MaxAuditorAware implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            var editor = "SYSTEM";

            // TODO: This provides the username to Spring Data so that the `created_at` and `updated_at` values can be
            //  automatically set. Once we implement Spring Security, this code will no longer be necessary.
            val requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                val authorizationHeader = requestAttributes.getRequest().getHeader("authorization");

                if (authorizationHeader != null && !authorizationHeader.isBlank()) {
                    val token = DecodedToken.getDecodedHeader(authorizationHeader);
                    editor = token.getSub();
                }
            }

            return Optional.of(editor);
        }
    }
}
