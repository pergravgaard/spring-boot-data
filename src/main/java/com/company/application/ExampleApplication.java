package com.company.application;

import com.company.config.RunConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(RunConfig.class)
public class ExampleApplication {

	public static void main(String[] args) {
		System.setProperty("env", "production");
		System.setProperty("spring.profiles.active", "production");
		SpringApplication.run(ExampleApplication.class, args);
	}

}
