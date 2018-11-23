package com.company.formatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseDateFormatterTest {

    private BaseDateFormatter baseDateFormatter;

    @Before
    public void setUp() {
        baseDateFormatter = new BaseDateFormatter();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testParse() {
        Locale locale = null;
        assertNotNull(baseDateFormatter.parse("3-1-2016", locale));
//        assertEquals(LocalDateTime.of(2016, Month.JANUARY, 3, 5, 9), baseDateTimeFormatter.parse("3-1-2016", locale));
        assertEquals(LocalDate.of(2016, Month.JANUARY, 3), baseDateFormatter.parse("3-1-2016", locale));
        assertEquals(LocalDate.of(2016, Month.JANUARY, 3), baseDateFormatter.parse("3-1-2016", locale));
        assertEquals(LocalDate.of(2016, Month.JANUARY, 3), baseDateFormatter.parse("03-01-2016", locale));
        assertEquals(LocalDate.of(1996, Month.JANUARY, 3), baseDateFormatter.parse("3-1-1996", locale));
        assertEquals(LocalDate.of(1926, Month.JANUARY, 3), baseDateFormatter.parse("3-1-1926", locale));
        assertEquals(LocalDate.of(2096, Month.JANUARY, 3), baseDateFormatter.parse("3-1-2096", locale));
    }

//    @Test
//    public void testPrint() throws Exception {
//
//    }

}