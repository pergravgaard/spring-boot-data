package com.company.rest.resource;

import com.company.model.jpa.Person;
import com.company.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/people")
public class PeopleResource {

    @Autowired
    private PersonRepository personRepository;

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Person getPerson(@PathParam("id") long id) {
        return personRepository.getPersonWithAddressById(id);
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    public Page<Person> getPersons() {
        return personRepository.findAllWithAddress(PageRequest.of(0, 20));
    }

}
