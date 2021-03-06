package com.company.config;

import com.company.bootstrap.Bootstrap;
import com.company.config.basic.JpaConfig;
import com.company.config.basic.JpaDataSourceConfig;
import com.company.config.basic.RestMvcConfig;
import com.company.model.jpa.Address;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;

import java.time.ZonedDateTime;

@ComponentScan(value = {
        "com.company",
        "springfox.documentation.spring.data.rest"
}) // tell Spring which packages to scan for components
@PropertySources({
        @PropertySource(value = {"classpath:mariadb.properties", "classpath:mariadb-${env}.properties"}, ignoreResourceNotFound = true),
//        @PropertySource(value = {"classpath:h2.properties", "classpath:h2-${env}.properties"}, ignoreResourceNotFound = true)
        @PropertySource(value = {"classpath:rest-mvc.properties", "classpath:rest-mvc-${env}.properties"}, ignoreResourceNotFound = true)
})
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class
})
@Import({JpaDataSourceConfig.class, JpaConfig.class, JerseyRestConfig.class, WebMvcConfig.class, RestMvcConfig.class, SwaggerConfig.class})
public class RunConfig extends AppConfig {

	@Bean
    @Profile("development")
	public CommandLineRunner bootstrap(AddressRepository addressRepository, PersonRepository personRepository) {
	    return run -> {
	            personRepository.deleteAll();
                addressRepository.deleteAll();
	            Address address = Bootstrap.createArbitraryAddress(addressRepository);
                ZonedDateTime now = ZonedDateTime.now();

                Bootstrap.createTheBauers(personRepository, address, now);

                Bootstrap.createPersons(personRepository, address);

        };
    }

}
