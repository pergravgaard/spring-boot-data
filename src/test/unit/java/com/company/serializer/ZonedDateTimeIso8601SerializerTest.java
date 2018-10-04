package com.company.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class ZonedDateTimeIso8601SerializerTest {

    private ZonedDateTimeIso8601Serializer zonedDateTimeSerializer;

    @Before
    public void setUp() {
        zonedDateTimeSerializer = new ZonedDateTimeIso8601Serializer();
    }

    @Test
    public void testSerialize() {
        ZonedDateTime localDateTime = ZonedDateTime.of(2016, Month.JANUARY.getValue(), 6, 20, 9, 7, 975 * 1000000, ZoneId.systemDefault());
//        ZonedDateTime utc = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
//        System.out.println(utc);
        assertEquals("2016-01-06T20:09:07.975+0100", zonedDateTimeSerializer.format(localDateTime));
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(ZonedDateTime.class, new ZonedDateTimeIso8601Serializer());
        mapper.registerModule(jtm);
        ZonedDateTime dateTime = ZonedDateTime.of(2016, Month.MAY.getValue(), 31, 13, 45, 24, 975 * 1000000, ZoneId.systemDefault());
        Assert.assertEquals("\"2016-05-31T13:45:24.975+0200\"", mapper.writeValueAsString(dateTime));
//        System.out.println(date);
//        System.out.println(mapper.readValue(date, ZonedDateTime.class));
    }

}