package com.company.rest.controller;

import com.company.model.jpa.Persistable;
import com.company.repository.jpa.GenericJpaRepository;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRestController<R extends ResourceSupport, E extends Persistable, ID extends Serializable> {

    protected abstract GenericJpaRepository<E, ID> getRepository();

    protected abstract R getResourceSupportInstance(E entity);

    protected abstract Class getControllerClass();

    @RequestMapping(path = "/{id}", produces = {"application/hal+json", "application/json"}, method = RequestMethod.GET)
    public HttpEntity<R> findById(@PathVariable ID id) {
        R resourceSupportInstance = getResourceSupportInstance(restFindById(id));
        //resourceSupportInstance.add(linkTo(methodOn(getControllerClass()).findById(id)).withSelfRel());
        return new ResponseEntity<>(resourceSupportInstance, HttpStatus.OK);
    }

//    public HttpEntity<Page<R>> findAll() {
//        PageRequest pageRequest = PageRequest.of(0, 3);
//        Page<E> all = getRepository().findAll(pageRequest);
//        Page<R> list = all.set;
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

    @RequestMapping(path = "", produces = {"application/hal+json", "application/json"}, method = RequestMethod.GET)
    public HttpEntity<List<R>> list() {
        List<R> list = getRepository().findAll().stream().map(this::getResourceSupportInstance).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    protected E restFindById(ID id) {
        return getRepository().findById(id).get();
    }

}
