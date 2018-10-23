package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class ExampleDevApplication {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		ConfigurableApplicationContext cac = SpringApplication.run(ExampleDevApplication.class, args);
        cac.getEnvironment().setActiveProfiles("development");
	}

}
