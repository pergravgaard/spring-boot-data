package com.company.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class LocalDateTimeIso8601SerializerTest {

    private LocalDateTimeIso8601Serializer localDateTimeSerializer;

    @Before
    public void setUp() {
        localDateTimeSerializer = new LocalDateTimeIso8601Serializer();
    }

    @Test
    public void testSerialize() {
        LocalDateTime localDateTime = LocalDateTime.of(2016, Month.JANUARY, 6, 20, 9, 7, 975 * 1000000);
//        ZonedDateTime utc = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
//        System.out.println(utc);
        assertEquals("2016-01-06T20:09:07.975Z", localDateTimeSerializer.format(localDateTime));
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(LocalDateTime.class, new LocalDateTimeIso8601Serializer());
        mapper.registerModule(jtm);
        LocalDateTime dateTime = LocalDateTime.of(2016, Month.MAY, 31, 13, 45, 24);
        Assert.assertEquals("\"2016-05-31T13:45:24.000Z\"", mapper.writeValueAsString(dateTime));
//        System.out.println(date);
//        System.out.println(mapper.readValue(date, ZonedDateTime.class));
    }

}