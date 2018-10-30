package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

public class BaseConfig {

    // cannot be in same config class as LocalContainerEntityManagerFactoryBean bean (JpaConfig) - then dependency injection won't work
    @Bean
    public ConversionService conversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatter(new BaseDateTimeFormatter());
        conversionService.addFormatter(new BaseDateFormatter());
        conversionService.addFormatter(new ZonedDateTimeFormatter());
        return conversionService;
    }

}
