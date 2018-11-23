package com.company.config;

import com.company.config.basic.JpaConfig;
import com.company.config.basic.JpaDataSourceConfig;
import com.company.config.basic.RestMvcConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@ComponentScan("com.company")
@PropertySources({
        @PropertySource(value = {"classpath:rest-mvc.properties", "classpath:rest-mvc-${env}.properties"}, ignoreResourceNotFound = true),
//        @PropertySource(value = {"classpath:mariadb-test.properties"})
        @PropertySource(value = {"classpath:h2-test.properties"})
})
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class,
        LiquibaseAutoConfiguration.class
})
@Import({JpaDataSourceConfig.class, JpaConfig.class, RestMvcConfig.class})
public class TestConfig extends AppConfig {

}
