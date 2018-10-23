package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("default")
@Import(AppConfig.class)
public class ExampleApplication {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		SpringApplication.run(ExampleApplication.class, args);
    }

}
