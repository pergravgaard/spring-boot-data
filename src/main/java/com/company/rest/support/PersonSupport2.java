package com.company.rest.support;

import com.company.model.jpa.Person;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class PersonSupport2 extends ResourceSupport {

    @JsonProperty
    private final String firstName;

    @JsonProperty
    private final String lastName;

    @JsonCreator
    public PersonSupport2(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

}
