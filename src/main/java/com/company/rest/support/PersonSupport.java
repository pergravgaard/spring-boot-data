package com.company.rest.support;

import com.company.model.jpa.Person;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class PersonSupport extends ResourceSupport {

    @JsonProperty
    @JsonIgnoreProperties("address")
    private final Person person;

    @JsonCreator
    public PersonSupport(Person person) {
        this.person = person;
    }

//    public Person getPerson() {
//        return person;
//    }

}
