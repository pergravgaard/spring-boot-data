package com.company.rest.projection;

import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;

@Projection(name = "withAddress", types = { Person.class })
public interface PersonWithAddress {
    String getFirstName();
    String getLastName();
    ZonedDateTime getCreatedDateTime();
    ZonedDateTime getLastModifiedDateTime();
    ZonedDateTime getBirthDateTime();
    Address getAddress();
}
