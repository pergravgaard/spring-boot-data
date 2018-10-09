package com.company.rest.controller;

import com.company.model.jpa.Person;
import com.company.repository.jpa.GenericJpaRepository;
import com.company.repository.jpa.PersonRepository;
import com.company.rest.support.PersonSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@ExposesResourceFor(Person.class)
public class PersonController extends AbstractRestController<PersonSupport, Person, Long> {

    @Autowired
    private PersonRepository personRepository;

    @Override
    protected GenericJpaRepository<Person, Long> getRepository() {
        return personRepository;
    }

    @Override
    protected PersonSupport getResourceSupportInstance(Person entity) {
        return new PersonSupport(entity);
    }

    @Override
    protected Class getControllerClass() {
        return getClass();
    }

//    @GetMapping("/students/{id}")
//    public Person retrieveStudent(@PathVariable long id) {
//        Optional<Person> person = personRepository.findById(id);
//
////        if (!person.isPresent())
////            throw new StudentNotFoundException("id-" + id);
//
//        return person.get();
//    }
//    @RequestMapping
//    public HttpEntity<Person> person() {
//        PersonSupport personSupport = new PersonSupport();
//        personSupport.add(linkTo(methodOn(PersonController.class).greeting(name)).withSelfRel());
//
//        return new ResponseEntity<>(greeting, HttpStatus.OK);
//    }
}
