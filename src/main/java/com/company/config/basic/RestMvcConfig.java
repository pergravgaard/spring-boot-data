package com.company.config.basic;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.format.FormatterRegistry;

public class RestMvcConfig extends RepositoryRestMvcConfiguration {

    @Autowired
    private Environment env;

    private RepositoryRestConfiguration repositoryRestConfiguration;

    public RestMvcConfig(ApplicationContext context, ObjectFactory<ConversionService> conversionServiceObjectFactory) {
        super(context, conversionServiceObjectFactory);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(new BaseDateTimeFormatter());
        registry.addFormatter(new BaseDateFormatter());
        registry.addFormatter(new ZonedDateTimeFormatter());
    }

    /**
     * Main configuration for the REST exporter.
     * Always return the same instance within the same application context
     */
    @Bean
    @Primary
    public synchronized RepositoryRestConfiguration repositoryRestConfiguration() {
        if (repositoryRestConfiguration == null) {
            repositoryRestConfiguration = super.repositoryRestConfiguration();
            repositoryRestConfiguration.setBasePath(env.getProperty("spring.data.rest.basePath", ""));
        }
        return repositoryRestConfiguration;
    }

    @JsonIgnoreType
    private static class MixinClass {
        // empty class for json mapping
    }

}
