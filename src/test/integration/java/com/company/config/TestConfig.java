package com.company.config;

import com.company.config.basic.JpaConfig;
import com.company.config.basic.JpaDataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@ComponentScan("com.company")
@PropertySources({
//        @PropertySource(value = {"classpath:mariadb-test.properties"})
        @PropertySource(value = {"classpath:h2-test.properties"})
})
@Import({JpaDataSourceConfig.class, JpaConfig.class, ConversionConfig.class})
public class TestConfig extends AppConfig {

}
