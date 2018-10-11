package com.company.config;

import com.company.formatter.BaseDateFormatter;
import com.company.formatter.BaseDateTimeFormatter;
import com.company.formatter.ZonedDateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.auditing.AuditableBeanWrapper;
import org.springframework.data.auditing.AuditableBeanWrapperFactory;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.core.StringToLdapNameConverter;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Optional;

public class RestConfig extends RepositoryRestMvcConfiguration {

//    @Autowired
//    private ConversionService conversionService;

    private ObjectFactory<ConversionService> conversionServiceObjectFactory;

    public RestConfig(ApplicationContext context, @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionServiceObjectFactory) {
        super(context, conversionServiceObjectFactory);
        this.conversionServiceObjectFactory = conversionServiceObjectFactory;
//        ObjectMapper jsonMapper = basicObjectMapper();
//        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
//        JavaTimeModule jtm = new JavaTimeModule();
//        jtm.addSerializer(LocalDateTime.class, new LocalDateTimeIso8601Serializer());
//        // TODO: add deserializers as well
//        //jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeIso8601Serializer());
//        jtm.addSerializer(ZonedDateTime.class, new ZonedDateTimeIso8601Serializer());
//        jsonMapper.registerModule(jtm);
//        jsonMapper.addMixIn(byte[].class, MixinClass.class);
//        jsonMapper.addMixIn(Byte[].class, MixinClass.class);
    }

    @Bean
    @Qualifier
    @Override
    public DefaultFormattingConversionService defaultConversionService() {

        DefaultFormattingConversionService conversionService = (DefaultFormattingConversionService) conversionServiceObjectFactory.getObject();
        // Add Spring Data Commons formatters
        conversionService.addConverter(uriToEntityConverter(conversionService));
        conversionService.addConverter(StringToLdapNameConverter.INSTANCE);

        conversionService.addFormatter(new BaseDateTimeFormatter());
        conversionService.addFormatter(new BaseDateFormatter());
        conversionService.addFormatter(new ZonedDateTimeFormatter());

        addFormatters(conversionService);

//        configurerDelegate.configureConversionService(conversionService);

        return conversionService;
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


        /**
         * Returns the {@link AuditableBeanWrapper} for the given source.
         *
         * @param source can be {@literal null}.
         * @return
         */
//        private Optional<AuditableBeanWrapper> getAuditableBeanWrapper(Object source) {
//            return auditableBeanWrapperFactory.getBeanWrapperFor(source);
//        }

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
