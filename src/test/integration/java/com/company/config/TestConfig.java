package com.company.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@ComponentScan("com.company")
@PropertySources({
//        @PropertySource(value = {"classpath:mariadb.properties", "classpath:mariadb-${env}.properties"}, ignoreResourceNotFound = true)
        @PropertySource(value = {"classpath:h2.properties", "classpath:h2-${env}.properties"}, ignoreResourceNotFound = true)
})
@Import({JpaDataSourceConfig.class, JpaConfig.class, BaseConfig.class})
public class TestConfig {

}
