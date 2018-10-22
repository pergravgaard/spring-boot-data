package com.company.formatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BaseDateTimeFormatterTest {

    private BaseDateTimeFormatter baseDateTimeFormatter;

    @Before
    public void setUp() throws Exception {
        baseDateTimeFormatter = new BaseDateTimeFormatter();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParse() throws Exception {
        Locale locale = null;
//        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-2016", locale));
        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 0, 0, 0, 0), baseDateTimeFormatter.parse("3-1-2016", locale));
        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-2016 5:09", locale));
        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-2016 05:09", locale));
        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("03-01-2016 05:09", locale));
        assertEquals(LocalDateTime.of(1996, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-1996 5:09", locale));
        assertEquals(LocalDateTime.of(1926, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-1926 5:09", locale));
        assertEquals(LocalDateTime.of(2096, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-2096 5:09", locale));
    }

//    @Test
//    public void testPrint() throws Exception {
//
//    }

}