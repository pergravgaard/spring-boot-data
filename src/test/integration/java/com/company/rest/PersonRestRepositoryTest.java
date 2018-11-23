package com.company.rest;

import com.company.bootstrap.Bootstrap;
import com.company.config.TestConfig;
import com.company.model.jpa.Address;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;
import org.junit.After;
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

    static {
        System.setProperty("env", "test");
        System.setProperty("spring.profiles.active", "testing");
    }

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Before
    public void before() {
        Address address = Bootstrap.createArbitraryAddress(addressRepository);
        Bootstrap.createPersons(personRepository, address);
    }

    @After
    public void after() {
        personRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testFindAllPersons() {
        ResponseEntity<String> response = template.getForEntity("/persons", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testGetPersonByFirstNameAndLastName() {
        ResponseEntity<String> response = template.getForEntity("/persons/search/find-by-first-and-last?firstName=Firstname&lastName=Andersen", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}
