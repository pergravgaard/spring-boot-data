package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.HashSet;
import java.util.Set;

public class BaseConfig {


    // cannot be in same config class as LocalContainerEntityManagerFactoryBean bean (JpaConfig) - then dependency injection won't work
//    @Bean
//    public ConversionService conversionService() {
//        FormattingConversionService conversionService = new DefaultFormattingConversionService();
//        //ConversionService object = new FormattingConversionServiceFactoryBean().getObject();
//        conversionService.addFormatter(new BaseDateTimeFormatter());
//        conversionService.addFormatter(new BaseDateFormatter());
//        conversionService.addFormatter(new ZonedDateTimeFormatter());
//        return conversionService;
//    }

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

    @Bean
    @Primary
    public ObjectFactory<ConversionService> mvcConversionService() {
        return new ObjectFactory<ConversionService>() {
            @Override
            public ConversionService getObject() throws BeansException {
                return null;
            }
        };
    }

    private Set<Converter<?, ?>> getConverters() {
        Set<Converter<?, ?>> converters = new HashSet<>();

        // add more custom converters, either as spring bean references or directly instantiated

        return converters;
    }


//    @Bean("mailSender")
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSender mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("username");
//        mailSender.setPassword("password");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//        return mailSender;
//    }

//    @Bean
//    LocalValidatorFactoryBean validator(AutowireCapableBeanFactory autowireCapableBeanFactory) {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean()
//        bean.setConstraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
//        return bean
//    }

//    @Bean
//    javax.validation.Validator validator(AutowireCapableBeanFactory autowireCapableBeanFactory) {
//        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
//                .configure()
//                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
//                .buildValidatorFactory()
//        return validatorFactory.getValidator()
//    }

}
