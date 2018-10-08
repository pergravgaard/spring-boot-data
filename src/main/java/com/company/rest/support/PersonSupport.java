package com.company.rest.support;

import com.company.model.jpa.Person;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class PersonSupport extends ResourceSupport {

    private final Person person;

    @JsonCreator
    public PersonSupport(@JsonProperty("person") Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

}
