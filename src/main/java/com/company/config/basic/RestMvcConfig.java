package com.company.config.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.auditing.AuditableBeanWrapper;
import org.springframework.data.auditing.AuditableBeanWrapperFactory;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;

/**
 * Solves 1 issue:
 * - The lack of a converter from java.util.Date to ZonedDateTime - by not converting to Date
 */
public class RestMvcConfig extends RepositoryRestMvcConfiguration {

    private ObjectFactory<ConversionService> conversionServiceObjectFactory;
    private RepositoryRestConfiguration repositoryRestConfiguration;

    public RestMvcConfig(ApplicationContext context, ObjectFactory<ConversionService> conversionServiceObjectFactory) {
        super(context, conversionServiceObjectFactory);
        this.conversionServiceObjectFactory = conversionServiceObjectFactory;
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
        }
        return repositoryRestConfiguration;
    }

//    @Override
//    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
//    }

    @Bean
    @Override
    public HttpHeadersPreparer httpHeadersPreparer() {
        return new MyHttpHeadersPreparer(auditableBeanWrapperFactory());
    }

    @JsonIgnoreType
    private static class MixinClass {
        // empty class for json mapping
    }

    public class MyHttpHeadersPreparer extends HttpHeadersPreparer {

        private final @NonNull AuditableBeanWrapperFactory auditableBeanWrapperFactory;

        MyHttpHeadersPreparer(AuditableBeanWrapperFactory auditableBeanWrapperFactory) {
            super(auditableBeanWrapperFactory);
            this.auditableBeanWrapperFactory = auditableBeanWrapperFactory;
            ConfigurableConversionService conversionService = (ConfigurableConversionService) conversionServiceObjectFactory.getObject();
            Jsr310Converters.getConvertersToRegister().forEach(conversionService::addConverter);
        }


        /**
         * Returns the default headers to be returned for the given {@link PersistentEntityResource}. Will set {@link ETag}
         * and {@code Last-Modified} headers if applicable.
         *
         * @param resource can be {@literal null}.
         * @return
         */
        public HttpHeaders prepareHeaders(Optional<PersistentEntityResource> resource) {
            return resource.map(it -> prepareHeaders(it.getPersistentEntity(), it.getContent())).orElseGet(HttpHeaders::new);
        }

        /**
         * Returns the default headers to be returned for the given {@link PersistentEntity} and value. Will set {@link ETag}
         * and {@code Last-Modified} headers if applicable.
         *
         * @param entity must not be {@literal null}.
         * @param value must not be {@literal null}.
         * @return
         */
        public HttpHeaders prepareHeaders(PersistentEntity<?, ?> entity, Object value) {

            Assert.notNull(entity, "PersistentEntity must not be null!");
            Assert.notNull(value, "Entity value must not be null!");

            // Add ETag
            HttpHeaders headers = ETag.from(entity, value).addTo(new HttpHeaders());

            // Add Last-Modified
            getLastModifiedInMilliseconds(value).ifPresent(headers::setLastModified);

            return headers;
        }

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


        private Optional<Long> getLastModifiedInMilliseconds(Object object) {

            return auditableBeanWrapperFactory.getBeanWrapperFor(object)
//                    .flatMap(auditableBeanWrapper -> {
//                        System.out.println(auditableBeanWrapper);
//                        return auditableBeanWrapper.getLastModifiedDate();
//                    })
                    .flatMap(AuditableBeanWrapper::getLastModifiedDate)
                    //.map(it -> conversionService.convert(it, Date.class)) // this line is obsolete and actually causes problems
                    .map(it -> conversionServiceObjectFactory.getObject().convert(it, Instant.class))
                    .map(Instant::toEpochMilli);
        }

    }

}
