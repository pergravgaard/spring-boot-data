package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
public class ExampleApplication /* extends SpringBootServletInitializer */ {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleApplication.class, args);
//        String[] beanNames = applicationContext.getBeanDefinitionNames();
//        for (String bName : beanNames) {
//            System.out.println("bean: " + bName + ", class: " + applicationContext.getBean(bName));
//        }
    }

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(ExampleApplication.class);
//	}

}
