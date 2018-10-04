package com.company.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeIso8601Serializer extends JsonSerializer<ZonedDateTime> {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    protected String format(ZonedDateTime value) {
        return value.format(dtf);
    }

    @Override
    public Class<ZonedDateTime> handledType() {
        return ZonedDateTime.class;
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(format(value));
    }

}
