package com.company.application;

import com.company.config.RunConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@Import(RunConfig.class)
public class ExampleWarApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("env", "production");
		System.setProperty("spring.profiles.active", "production");
		SpringApplication.run(ExampleWarApplication.class, args);
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ExampleWarApplication.class);
	}

}
