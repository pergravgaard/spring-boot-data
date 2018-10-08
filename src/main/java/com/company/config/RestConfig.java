package com.company.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.auditing.AuditableBeanWrapper;
import org.springframework.data.auditing.AuditableBeanWrapperFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;

public class RestConfig extends RepositoryRestMvcConfiguration {

//    @Autowired
//    private ConversionService conversionService;

    private ObjectFactory<ConversionService> conversionServiceObjectFactory;

    public RestConfig(ApplicationContext context, @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
        this.conversionServiceObjectFactory = conversionService;
    }

    @Bean
    @Override
    public HttpHeadersPreparer httpHeadersPreparer() {
        return new MyHttpHeadersPreparer(auditableBeanWrapperFactory());
    }

    public class MyHttpHeadersPreparer extends HttpHeadersPreparer {

        private final AuditableBeanWrapperFactory auditableBeanWrapperFactory;

        MyHttpHeadersPreparer(AuditableBeanWrapperFactory auditableBeanWrapperFactory) {
            super(auditableBeanWrapperFactory);
            this.auditableBeanWrapperFactory = auditableBeanWrapperFactory;
        }

        @Override
        public boolean isObjectStillValid(Object source, HttpHeaders headers) {

            Assert.notNull(source, "Source object must not be null!");
            Assert.notNull(headers, "HttpHeaders must not be null!");

            if (headers.getIfModifiedSince() == -1) {
                return false;
            }

            return getLastModifiedInMilliseconds(source)//
                    .map(it -> it / 1000 * 1000 <= headers.getIfModifiedSince())//
                    .orElse(true);
        }

        @Override
        public HttpHeaders prepareHeaders(PersistentEntity<?, ?> entity, Object value) {

            Assert.notNull(entity, "PersistentEntity must not be null!");
            Assert.notNull(value, "Entity value must not be null!");

            // Add ETag
            HttpHeaders headers = ETag.from(entity, value).addTo(new HttpHeaders());

            // Add Last-Modified
            getLastModifiedInMilliseconds(value).ifPresent(headers::setLastModified);

            return headers;
        }

        private Optional<Long> getLastModifiedInMilliseconds(Object object) {

            return auditableBeanWrapperFactory.getBeanWrapperFor(object)
                    .flatMap(AuditableBeanWrapper::getLastModifiedDate)
                    //.map(it -> conversionService.convert(it, Date.class)) // this line is obsolete and actually causes problems
                    .map(it -> conversionServiceObjectFactory.getObject().convert(it, Instant.class))
                    .map(Instant::toEpochMilli);
        }

    }

}
