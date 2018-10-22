package com.company.rest.projection;

import com.company.model.jpa.Address;
import com.company.model.jpa.Auditable;
import com.company.model.jpa.Person;
import org.springframework.data.rest.core.config.Projection;

import java.time.ZonedDateTime;

@Projection(name = "withAddress", types = { Person.class })
public interface PersonWithAddress extends Auditable {
    String getFirstName();
    String getLastName();
    ZonedDateTime getBirthDateTime();
    Address getAddress();
}
