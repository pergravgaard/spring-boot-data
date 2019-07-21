package com.company.rest;

import com.company.bootstrap.Bootstrap;
import com.company.config.TestConfig;
import com.company.model.jpa.Address;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;


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
        ResponseEntity<String> response = template.getForEntity("/persons/search/find-by-first-and-last?firstName=Axel&lastName=Andersen", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testGetPersonsContaining() throws IOException {
        ResponseEntity<String> response = template.getForEntity("/persons/search/find-all-by-firstname-containing-and-lastname-containing?firstNamePart=&lastNamePart=sen&sort=lastName,asc&sort=firstName,asc", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        ObjectMapper objectMapper = new ObjectMapper();
        Map json = objectMapper.readValue(response.getBody(), Map.class);
        Map embedded = (Map) json.get("_embedded");
        List persons = (List) embedded.get("persons");
        assertEquals(18, persons.size());
    }

}
