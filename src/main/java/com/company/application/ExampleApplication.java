package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

@Import(AppConfig.class)
public class ExampleApplication /* extends SpringBootServletInitializer */ {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleApplication.class, args);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Map<String, Object> map = new HashMap<>();
        for (String bName : beanNames) {
        	if (bName.contains("onversionService")) {

            	System.out.println("bean: " + bName + ", class: " + applicationContext.getBean(bName).getClass());
            	map.put(bName, applicationContext.getBean(bName));

			}
        }
        System.out.println("********* "  + (map.get("defaultConversionService") == map.get("mvcConversionService")));
    }

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(ExampleApplication.class);
//	}

}
