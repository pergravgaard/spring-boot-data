package com.company.rest.support;

import com.company.model.jpa.Person;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, PersonSupport> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public PersonResourceAssembler(Class<?> controllerClass, Class<PersonSupport> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public PersonSupport toResource(Person entity) {

        return new PersonSupport(entity);
    }

}
