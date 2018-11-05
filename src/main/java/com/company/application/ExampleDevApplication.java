package com.company.application;

import com.company.config.RunConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(RunConfig.class)
public class ExampleDevApplication {

	public static void main(String[] args) {
		System.setProperty("env", "development");
        System.setProperty("spring.profiles.active", "development");
		SpringApplication.run(ExampleDevApplication.class, args);
	}

}
