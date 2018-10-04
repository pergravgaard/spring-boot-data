package com.company.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeIso8601Serializer extends JsonSerializer<LocalDateTime> {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    protected String format(LocalDateTime value) {
        return value.format(dtf) + "Z";
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(format(value));
    }

}
