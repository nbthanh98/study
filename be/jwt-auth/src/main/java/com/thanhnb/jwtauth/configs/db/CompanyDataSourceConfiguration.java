package com.thanhnb.jwtauth.configs.db;

import com.zaxxer.hikari.HikariDataSource;
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

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.thanhnb.jwtauth.repository.company",
        entityManagerFactoryRef = "companyEntityManagerFactory",
        transactionManagerRef = "companyTransactionManager"
)
@ConfigurationProperties(prefix = "spring.datasource.company")
public class CompanyDataSourceConfiguration {

    /**
     * Here it will get url, username, password and driver-class-name
     * which we have defined in application properties file for company.
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.company")
    public DataSourceProperties companyDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Create the datasource using companyDataSourceProperties
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.company.configuration")
    public DataSource companyDataSource() {
        return companyDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    /**
     * EntityManager will find Entity classes inside this company package
     * (i.e com.techgeeknext.entities.company.Company).
     * @param builder
     * @return
     */
    @Bean(name = "companyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean companyEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(companyDataSource())
                .packages("com.thanhnb.jwtauth.models.company")
                .build();
    }

    @Bean
    public PlatformTransactionManager companyTransactionManager(
            final @Qualifier("companyEntityManagerFactory") LocalContainerEntityManagerFactoryBean companyEntityManagerFactory) {
        return new JpaTransactionManager(companyEntityManagerFactory.getObject());
    }

}
