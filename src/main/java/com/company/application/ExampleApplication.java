package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class ExampleApplication /* extends SpringBootServletInitializer */ {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		SpringApplication.run(ExampleApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(ExampleApplication.class);
//	}

}
