package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.HashSet;
import java.util.Set;

public class BaseConfig {

    @Autowired
    private ApplicationContext applicationContext;

    // cannot be in same config class as LocalContainerEntityManagerFactoryBean bean (JpaConfig) - then dependency injection won't work
    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public ConversionService defaultConversionService() {
        final String mvcConversionServiceBeanName = "mvcConversionService";
        FormattingConversionService formattingConversionService = null;
        if (applicationContext != null && applicationContext.containsBean(mvcConversionServiceBeanName) && applicationContext.getBean(mvcConversionServiceBeanName) instanceof ObjectFactory) {
            ObjectFactory<ConversionService> factory = (ObjectFactory<ConversionService>) applicationContext.getBean(mvcConversionServiceBeanName);
            if (factory.getObject() instanceof DefaultFormattingConversionService) {
                formattingConversionService = (DefaultFormattingConversionService) factory.getObject();
            }
        }
        if (formattingConversionService == null) {
            FormattingConversionServiceFactoryBean bean = new FormattingConversionServiceFactoryBean();
            bean.afterPropertiesSet();
            formattingConversionService = bean.getObject();
        }
        for (Converter<?, ?> converter : getConverters()) {
            formattingConversionService.addConverter(converter);
        }
        formattingConversionService.addFormatter(new BaseDateTimeFormatter());
        formattingConversionService.addFormatter(new BaseDateFormatter());
        formattingConversionService.addFormatter(new ZonedDateTimeFormatter());
        return formattingConversionService;
    }

    private Set<Converter<?, ?>> getConverters() {
        Set<Converter<?, ?>> converters = new HashSet<>();

        // add more custom converters, either as spring bean references or directly instantiated

        return converters;
    }

}
