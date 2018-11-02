package com.company.config;

import com.company.config.basic.JpaDataSourceConfig;
import com.company.config.basic.RestMvcConfig;
import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@ComponentScan(value = {
        "com.company",
        "springfox.documentation.spring.data.rest"
}) // tell Spring which packages to scan for components
@PropertySources({
        @PropertySource(value = {"classpath:mariadb.properties", "classpath:mariadb-${env}.properties"}, ignoreResourceNotFound = true)
//        @PropertySource(value = {"classpath:h2.properties", "classpath:h2-${env}.properties"}, ignoreResourceNotFound = true)
})
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class
})
@Import({JpaDataSourceConfig.class, MyJpaConfig.class, RestMvcConfig.class, SwaggerConfig.class})
public class AppConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // use UTC timezone through out the application
    }

//    @Bean
//    public ServletWebServerFactory servletWebServerFactory(){
//        return new TomcatServletWebServerFactory();
//    }

	@Bean
    @Profile("development")
	public CommandLineRunner bootstrap(AddressRepository addressRepository, PersonRepository personRepository) {
	    return run -> {
            Address address = new Address();
            address.setStreet("Hollywood Avenue");
            address.setNo("21C");
            address.setZipCode("90210");
            address.setCity("Los Angeles");
            address.setState("California");
            address.setCountry("USA");
            addressRepository.save(address);

            ZonedDateTime now = ZonedDateTime.now();

            Person jack = new Person();
            jack.setFirstName("Jack");
            jack.setLastName("Bauer");
            jack.setBirthDateTime(now.withNano(0).withSecond(0).minusYears(40));
            jack.setAddress(address);
            personRepository.save(jack);

            Person kim = new Person();
            kim.setFirstName("Kim");
            kim.setLastName("Bauer");
            kim.setBirthDateTime(now.withNano(0).withSecond(0).minusYears(20));
            kim.setAddress(address);
            personRepository.save(kim);

            LocalDateTime localDateTime = LocalDateTime.of(1971, 12, 10, 14, 15);
            ZonedDateTime utcPointInTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);

            List<String> lastNames = Arrays.asList("Andersen", "Christensen", "Damgaard", "Espersen", "Frederiksen", "Gaardbo", "Hansen", "Ibsen", "Jakobsen", "Karlsen", "Ladefoged", "Mortensen", "Nielsen", "Olsen", "Petersen", "Quist", "Rasmussen", "Sørensen", "Thomsen", "Udsen", "Villadsen", "Westergaard");
            List<Person> persons = lastNames.stream().map(s -> {
                Person p = new Person();
                p.setFirstName("Firstname");
                p.setLastName(s);
                p.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).plusYears(25).withZoneSameLocal(ZoneOffset.ofHours(-7)));
                p.setAddress(address);
                return p;
            }).collect(Collectors.toList());
            personRepository.saveAll(persons);


        };
    }

}
