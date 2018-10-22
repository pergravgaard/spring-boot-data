package com.company.application;

import com.company.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("default")
@Import(AppConfig.class)
public class ExampleApplication {

	public static void main(String[] args) {
		System.setProperty("env", "development");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleApplication.class, args);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Map<String, Object> map = new HashMap<>();
        for (String bName : beanNames) {
            	System.out.println("bean: " + bName + ", class: " + applicationContext.getBean(bName).getClass());
        	if (bName.contains("onversionService")) {
            	map.put(bName, applicationContext.getBean(bName));
			}
        }
        System.out.println("********* "  + (map.get("defaultConversionService") == map.get("mvcConversionService")));
    }

}
