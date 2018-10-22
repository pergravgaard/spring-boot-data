package com.company.formatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class ZonedDateTimeFormatterTest {

    private ZonedDateTimeFormatter zonedDateTimeFormatter;

    @Before
    public void setUp() {
        zonedDateTimeFormatter = new ZonedDateTimeFormatter();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testParseWithoutMilliseconds() {
        ZonedDateTime expected = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 0, ZoneId.of("+01:00"));
        ZonedDateTime expected2 = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 0, ZoneId.of("Europe/Paris"));
        ZonedDateTime actual = zonedDateTimeFormatter.parse("2017-11-24T22:15:33+01:00", null);
        assertEquals(expected, actual);
        assertNotEquals(expected2, actual); // same offset in minutes, but not equal
        assertEquals(expected2.toLocalDateTime(), actual.toLocalDateTime());
        assertEquals(3600, expected.getOffset().getTotalSeconds());
        assertEquals(3600, expected2.getOffset().getTotalSeconds());
        assertEquals(3600, actual.getOffset().getTotalSeconds());
    }

    @Test
    public void testParseWithMilliseconds() {
//        DateTimeFormatter dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME;
//        System.out.println("dtf:" + dtf.toString());
//        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2017-11-24T22:15:33.567+01:00", dtf);
//        System.out.println(zonedDateTime.format(dtf));
//        assertNotNull(zonedDateTime);
        ZonedDateTime expected = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 567 * 1000 * 1000, ZoneId.of("+01:00"));
        ZonedDateTime expected2 = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 567 * 1000 * 1000, ZoneId.of("Europe/Paris"));
        ZonedDateTime actual = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567+01:00", null);
//        System.out.println(expected.getZone());
//        System.out.println(actual.getZone());
//        System.out.println(actual.getOffset().getTotalSeconds());
        assertEquals(expected, actual);
        assertNotEquals(expected2, actual); // same offset, but not equal
        assertEquals(expected2.toLocalDateTime(), actual.toLocalDateTime());
        assertEquals(3600, expected.getOffset().getTotalSeconds());
        assertEquals(3600, expected2.getOffset().getTotalSeconds());
        assertEquals(3600, actual.getOffset().getTotalSeconds());
    }

    @Test
    public void testParseUTCWithMilliseconds() {
        ZonedDateTime expected = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 567 * 1000 * 1000, ZoneId.of("+00:00"));
        ZonedDateTime expected2 = ZonedDateTime.of(2017, Month.NOVEMBER.getValue(), 24, 22, 15, 33, 567 * 1000 * 1000, ZoneId.of("Europe/London"));
        ZonedDateTime actual = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567+00:00", null);
        ZonedDateTime actual2 = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567Z", null);
        assertEquals(expected, actual);
        assertNotEquals(expected2, actual); // same offset, but not equal
        assertEquals(expected, actual2);
        assertEquals(expected2.toLocalDateTime(), actual.toLocalDateTime());
        assertEquals(0, expected.getOffset().getTotalSeconds());
        assertEquals(0, expected2.getOffset().getTotalSeconds());
        assertEquals(0, actual.getOffset().getTotalSeconds());
        assertEquals(0, actual2.getOffset().getTotalSeconds());
    }

    @Test
    public void testPrint() {
        ZonedDateTime actual = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567+00:00", null);
        ZonedDateTime actual2 = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567Z", null);
        ZonedDateTime actual3 = zonedDateTimeFormatter.parse("2017-11-24T22:15:33.567+01:00", null);
        assertEquals("2017-11-24T22:15:33.567Z", zonedDateTimeFormatter.print(actual, null));
        assertEquals("2017-11-24T22:15:33.567Z", zonedDateTimeFormatter.print(actual2, null));
        assertEquals("2017-11-24T22:15:33.567+01:00", zonedDateTimeFormatter.print(actual3, null));
    }

    @Test
    public void testPrintZoneId() {
        ZonedDateTime now = ZonedDateTime.now();

        String actual = zonedDateTimeFormatter.print(now, null);
        System.out.println("ACTUAL: " + actual);
        assertTrue(actual.endsWith(":00"));
    }

}