package com.company.rest.controller;

import com.company.model.jpa.Person;
import com.company.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

//@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/students/{id}")
    public Person retrieveStudent(@PathVariable long id) {
        Optional<Person> person = personRepository.findById(id);

//        if (!person.isPresent())
//            throw new StudentNotFoundException("id-" + id);

        return person.get();
    }
//    @RequestMapping
//    public HttpEntity<Person> person() {
//        PersonSupport personSupport = new PersonSupport();
//        personSupport.add(linkTo(methodOn(PersonController.class).greeting(name)).withSelfRel());
//
//        return new ResponseEntity<>(greeting, HttpStatus.OK);
//    }
}
