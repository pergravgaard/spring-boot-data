package com.company.config;

import com.company.serializer.LocalDateTimeIso8601Serializer;
import com.company.serializer.ZonedDateTimeIso8601Serializer;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class JacksonConfig {

    @JsonIgnoreType
    private static class MixinClass {
        // empty class for json mapping
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(LocalDateTime.class, new LocalDateTimeIso8601Serializer());
        jtm.addSerializer(ZonedDateTime.class, new ZonedDateTimeIso8601Serializer());
        jsonMapper.registerModule(jtm);
        jsonMapper.addMixIn(byte[].class, MixinClass.class);
        jsonMapper.addMixIn(Byte[].class, MixinClass.class);
        return jsonMapper;
    }
}