package com.company.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@ComponentScan("com.company")
@PropertySources({
        @PropertySource(value = {"classpath:mariadb-test.properties"})
//        @PropertySource(value = {"classpath:h2.properties", "classpath:h2-${env}.properties"})
})
@Import({JpaDataSourceConfig.class, JpaConfig.class})
public class TestConfig {

}
