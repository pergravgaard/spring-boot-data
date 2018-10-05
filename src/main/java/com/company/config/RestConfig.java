package com.company.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.auditing.AuditableBeanWrapperFactory;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RestConfig extends RepositoryRestMvcConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    public RestConfig(ApplicationContext context, @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);

    }

    @Bean
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);
        converters.add(jsonConverter);
        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }

    @Bean
    @Override
    public HttpHeadersPreparer httpHeadersPreparer() {
        return new HttpHeadersPreparer(auditableBeanWrapperFactory());
    }


    public class MyHttpHeadersPreparer extends HttpHeadersPreparer {

        public MyHttpHeadersPreparer(AuditableBeanWrapperFactory auditableBeanWrapperFactory) {
            super(auditableBeanWrapperFactory);
        }

        private Optional<Long> getLastModifiedInMilliseconds(Object object) {

            return auditableBeanWrapperFactory.getBeanWrapperFor(object)//
                    .flatMap(it -> it.getLastModifiedDate())//
                    .map(it -> conversionService.convert(it, Date.class))//
                    .map(it -> conversionService.convert(it, Instant.class))//
                    .map(it -> it.toEpochMilli());
        }

    }

}
