package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

}
