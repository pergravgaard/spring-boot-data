package com.company.formatter;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BaseDateFormatter implements Formatter<LocalDate> {

    private final String formatPattern;
    private final List<String> parsePatterns;
    private final DateTimeFormatter dateFormatter;

    public BaseDateFormatter() {
        parsePatterns = Arrays.asList("d-M-yyyy", "d.M.yyyy");
        formatPattern = "dd-MM-yyyy";
        dateFormatter = DateTimeFormatter.ofPattern(formatPattern);
    }

    protected DateTimeFormatter buildDateTimeFormatter(String pattern) {
//        String[] split = "d-M H:m".split("[y]{2}");
//        System.out.println("split:" + Arrays.toString(split) + " - " + split.length);
//        int yyIndex = pattern.indexOf("yy");
//        if (yyIndex > -1 && (yyIndex + 2 >= pattern.length() || pattern.charAt(yyIndex + 2) != 'y')) {
//            return new DateTimeFormatterBuilder()
//                            .appendPattern(pattern)
//                            .appendValueReduced(ChronoField.YEAR, 2, 2, Year.now().getValue() - 80)
//                            .toFormatter();
//        }
        return DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate parse(String text, Locale locale) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        RuntimeException ex = null;
        if (parsePatterns != null && !parsePatterns.isEmpty()) {
            for (String pattern : parsePatterns) {
                try {
                    DateTimeFormatter dateTimeFormatter = buildDateTimeFormatter(pattern);
                    return LocalDate.parse(text, dateTimeFormatter);
                }
                catch (DateTimeParseException pex) {
//                    System.out.println("Couldn't parse date from " + text + " with pattern " + pattern);
                    ex = pex;
                }
            }
        }
        if (ex != null) {
            throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object != null ? object.format(dateFormatter) : "";
    }

}
