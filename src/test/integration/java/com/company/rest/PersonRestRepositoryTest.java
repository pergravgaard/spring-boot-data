package com.company.rest;

import com.company.config.RestTestConfig;
import com.company.model.jpa.Person;
import com.company.repository.jpa.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = RestTestConfig.class)
public class PersonRestRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testGetPerson() {
        webClient.get().uri("/persons/search/find-by-first-and-last?firstName=Jack&lastName=Bauer").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .isEqualTo(personRepository.findFirstByFirstNameAndLastName("Jack", "Bauer"));
    }

}
