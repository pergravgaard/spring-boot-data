package com.company.rest.projection;

import com.company.model.jpa.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "nameConcatenated", types = { Person.class })
public interface PersonNameConcatenated {

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getName();

}
