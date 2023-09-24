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
        basePackages = "com.thanhnb.jwtauth.repository.jwt",
        entityManagerFactoryRef = "jwtAuthEntityManagerFactory",
        transactionManagerRef = "jwtAuthTransactionManager"
)
public class JwtAuthDataSourceConfiguration {

    /**
     * Here it will get url, username, password and driver-class-name
     * which we have defined in application properties file for company.
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.jwt")
    public DataSourceProperties jwtAuthDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Create the datasource using companyDataSourceProperties
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.jwt.configuration")
    public DataSource jwtAuthDataSource() {
        return jwtAuthDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * EntityManager will find Entity classes inside this company package
     * (i.e com.techgeeknext.entities.company.Company).
     * @param builder
     * @return
     */
    @Bean(name = "jwtAuthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jwtAuthEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(jwtAuthDataSource())
                .packages("com.thanhnb.jwtauth.models.jwt")
                .build();
    }

    @Bean
    public PlatformTransactionManager jwtAuthTransactionManager(
            final @Qualifier("jwtAuthEntityManagerFactory") LocalContainerEntityManagerFactoryBean jwtAuthEntityManagerFactory) {
        return new JpaTransactionManager(jwtAuthEntityManagerFactory.getObject());
    }
}
