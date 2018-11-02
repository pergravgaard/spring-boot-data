package com.company.config;

import com.company.config.basic.DefaultSwaggerConfig;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig extends DefaultSwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.company"))
//                .apis(or(
//                        RequestHandlerSelectors.withClassAnnotation(RepositoryRestResource.class),
//                        RequestHandlerSelectors.withClassAnnotation(RepositoryRestController.class)
//                ))
//                .apis(RequestHandlerSelectors.any())
//                .paths(or(regex("/persons.*"), regex("/addresses.*")))
                .paths(PathSelectors.any())
                .build();
    }

}