package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.HashSet;
import java.util.Set;

//@Configuration
public class ConversionServiceProvider {

    @Bean
    @Primary
    public ConversionService defaultConversionService() {
        FormattingConversionServiceFactoryBean bean = new FormattingConversionServiceFactoryBean();
        bean.setConverters(getConverters());
        bean.afterPropertiesSet();
        FormattingConversionService object = bean.getObject();
        object.addFormatter(new BaseDateTimeFormatter());
        object.addFormatter(new BaseDateFormatter());
        object.addFormatter(new ZonedDateTimeFormatter());
        return object;
    }

    private Set<Converter<?, ?>> getConverters() {
        Set<Converter<?, ?>> converters = new HashSet<>();

        // add more custom converters, either as spring bean references or directly instantiated

        return converters;
    }
}
