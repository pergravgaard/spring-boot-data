package com.company.rest;

import com.company.bootstrap.Bootstrap;
import com.company.config.TestConfig;
import com.company.model.jpa.Address;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonRestRepositoryTest {


    @Autowired
    private TestRestTemplate template;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Before
    public void before() {
        Address address = new Address();
        address.setStreet("Hollywood Avenue");
        address.setNo("21C");
        address.setZipCode("90210");
        address.setCity("Los Angeles");
        address.setState("California");
        address.setCountry("USA");
        addressRepository.save(address);
        Bootstrap.createPersons(personRepository, address);
//        Person person = new Person();
//        person.setFirstName("John");
//        person.setLastName("Doe");
//        person.setBirthDateTime(ZonedDateTime.now());
//        person.setAddress(address);
//        personRepository.save(person);
    }

    @Test
    public void testFindAllPersons() {
        ResponseEntity<String> response = template.getForEntity("/persons", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testGetPersonByFirstNameAndLastName() {
        ResponseEntity<String> response = template.getForEntity("/persons/search/find-by-first-and-last?firstName=John&lastName=Doe", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        //        webClient.get().uri("/persons/search/find-by-first-and-last?firstName=Jack&lastName=Bauer").accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Person.class)
//                .isEqualTo(personRepository.findFirstByFirstNameAndLastName("Jack", "Bauer"));
    }

//    @Test
//    public void testGetPerson() {
//        webClient.get().uri("/persons/1").accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Person.class)
//                .isEqualTo(personRepository.findFirstByFirstNameAndLastName("Jack", "Bauer"));
//    }

}
