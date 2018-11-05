package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import com.company.rest.projection.PersonNameConcatenated;
import com.company.rest.projection.PersonWithAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.TimeZone;

public class AppConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // use UTC timezone through out the application
    }

    @Autowired
    private DefaultFormattingConversionService defaultConversionService;

    @Autowired
    private DefaultFormattingConversionService mvcConversionService;

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;

//    @Bean
//    public ServletWebServerFactory servletWebServerFactory(){
//        return new TomcatServletWebServerFactory();
//    }

    @Bean
    public String addCustomFormatters() {

        defaultConversionService.addFormatter(new BaseDateTimeFormatter());
        defaultConversionService.addFormatter(new BaseDateFormatter());
        defaultConversionService.addFormatter(new ZonedDateTimeFormatter());

        mvcConversionService.addFormatter(new BaseDateTimeFormatter());
        mvcConversionService.addFormatter(new BaseDateFormatter());
        mvcConversionService.addFormatter(new ZonedDateTimeFormatter());

        return "Added custom formatters";
    }

    @Bean
    public String addCustomProjections() {

        repositoryRestConfiguration.exposeIdsFor(Person.class); // ensure that the id is marshalled as well
        repositoryRestConfiguration.exposeIdsFor(Address.class); // ensure that the id is marshalled as well
        repositoryRestConfiguration.getProjectionConfiguration().addProjection(PersonWithAddress.class);
        repositoryRestConfiguration.getProjectionConfiguration().addProjection(PersonNameConcatenated.class);

        return "Added custom projections";
    }

}
