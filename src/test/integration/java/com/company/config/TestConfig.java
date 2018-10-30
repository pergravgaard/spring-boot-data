package com.company.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.TimeZone;

@ComponentScan("com.company")
@PropertySources({
//        @PropertySource(value = {"classpath:mariadb-test.properties"})
        @PropertySource(value = {"classpath:h2-test.properties"})
})
@Import({JpaDataSourceConfig.class, JpaConfig.class, BaseConfig.class})
public class TestConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


}
