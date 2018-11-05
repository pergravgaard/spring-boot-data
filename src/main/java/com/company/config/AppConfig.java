package com.company.config;

import java.util.TimeZone;

public class AppConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // use UTC timezone through out the application
    }

}
