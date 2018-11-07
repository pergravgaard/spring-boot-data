package com.company.config.basic;

import com.company.repository.jpa.GenericJpaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Properties;

/**
 * Generic JPA configuration (except for the condition that packages must begin with 'com')
 */
@EnableJpaRepositories(basePackages = "com.**.repository.jpa", repositoryBaseClass = GenericJpaRepositoryImpl.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableTransactionManagement
public class JpaConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    private LocalValidatorFactoryBean validator;

    // necessary for Spring Boot 2.0 to support ZonedDateTime for auditing
    @Bean(name = "dateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    public synchronized LocalValidatorFactoryBean validator() {
        if (validator == null) {
            LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//            MessageSource messageSource = applicationContext.getBean("messageSource")
//            bean.setValidationMessageSource(messageSource)
            // the application context must be set for Validator Factory to be set by call to afterPropertiesSet
            bean.setApplicationContext(applicationContext);
            bean.afterPropertiesSet();
            validator = bean;
        }
        return validator;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.valueOf(env.getProperty("hibernate.hbm2ddl.generate")));
        vendorAdapter.setShowSql(Boolean.valueOf(env.getProperty("hibernate.show_sql")));
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.**.model.jpa");
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql", "false"));
        jpaProperties.put("javax.persistence.validation.mode", "none"); // disable Hibernator validation - otherwise bean validators will be called twice
        jpaProperties.put("javax.persistence.validation.factory", validator()); // this is necessary for Spring Dependency Injection to work in custom bean validators
//        jpaProperties.put("hibernate.integrator_provider", (IntegratorProvider) { // cast Closure instance to IntegrationProvider instance - in Groovy use closure instead of lambda expression
//            Collections.singletonList(RootAwareEventListenerIntegrator.INSTANCE)
//        })
        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

}
