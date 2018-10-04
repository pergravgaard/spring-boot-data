package com.company.formatter;

import org.springframework.format.Formatter;

import java.time.temporal.TemporalAccessor;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TemporalAccessorFormatter implements Formatter<TemporalAccessor> {

    private final String formatPattern;
    private final List<String> parsePatterns;
    private final DateTimeFormatter dateTimeFormatter;

    public TemporalAccessorFormatter() {
        parsePatterns = Arrays.asList("d-M-yyyy", "d.M.yyyy", "d-M-yyyy H:mm", "d.M.yyyy H:mm");//, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatPattern = "dd-MM-yyyy HH:mm";
        dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
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
    public TemporalAccessor parse(String text, Locale locale) {
        RuntimeException ex = null;
        if (parsePatterns != null && !parsePatterns.isEmpty()) {
            for (String pattern : parsePatterns) {
                try {
                    DateTimeFormatter dateTimeFormatter = buildDateTimeFormatter(pattern);
                    return dateTimeFormatter.parse(text);
                }
                catch (RuntimeException pex) {
                    System.out.println("Couldn't parse date from " + text + " with pattern " + pattern);
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
    public String print(TemporalAccessor object, Locale locale) {
        return object != null ? dateTimeFormatter.format(object) : "";
    }

}
