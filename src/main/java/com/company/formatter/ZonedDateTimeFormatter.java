package com.company.formatter;

import org.springframework.format.Formatter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class ZonedDateTimeFormatter implements Formatter<ZonedDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public ZonedDateTime parse(String text, Locale locale) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return ZonedDateTime.parse(text, dateTimeFormatter);
        }
        catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Could not parse zoned date time: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String print(ZonedDateTime object, Locale locale) {
        return object != null ? object.format(dateTimeFormatter).replaceAll("[\\[]{1}[\\/a-zA-Z]+[\\]]{1}", "") : "";
    }

}
