package com.company.model.jpa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "nameProjection", types = { Person.class })
public interface PersonNameProjection extends Auditable {

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getName();

}
