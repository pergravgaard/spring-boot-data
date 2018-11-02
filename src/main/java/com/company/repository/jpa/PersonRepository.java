package com.company.repository.jpa;


import com.company.model.jpa.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel="person", path="person")
@Repository
//@RepositoryRestResource
//@RepositoryRestResource(excerptProjection = PersonWithAddress.class)
public interface PersonRepository extends GenericJpaRepository<Person, Long> {

    @RestResource(rel = "find-by-last", path="find-by-last")
    // no transactional annotation needed here, but the implementation will be transactional
    List<Person> findAllByLastName(@Param("lastName") String lastName); // no implementation needed - Spring will implement it by looking at method name

    @RestResource(rel = "find-by-first-and-last", path="find-by-first-and-last")
    // no transactional annotation needed here, but the implementation will be transactional
    Person findFirstByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName); // no implementation needed - Spring will implement it by looking at method name

    // See https://www.thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
    @SuppressWarnings("unchecked")
    // won't be exported by Rest by default due to not following naming convention
    @Transactional(readOnly = true) // the transactional annotation is needed when writing the implementation here
    default Person getPersonWithAddressById(Long id) {

//        Person one = getOne(id);
//        one.getAddress().getStreet(); // works, but is not optimal for performance
//        return one;

        EntityGraph<Person> graph = getEntityManager().createEntityGraph(Person.class);
        graph.addSubgraph("address"); // tell JPA to fetch the address

        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        return getEntityManager().find(Person.class, id, hints);
    }

    // won't be exported by Rest due to not following naming convention
    @Transactional(readOnly = true)
    default Page<Person> findAllWithAddress(Pageable pageable) {
        return findAllWithHints(pageable, Person.class, "address");
    }

    @Override
    @Transactional(readOnly = true)
    default Page<Person> findAll(Pageable pageable) {
        return findAllWithAddress(pageable);
//        Page<Person> page = findAllWithAddress(pageable);
//        page.getContent().forEach(person -> {
//            System.out.println(person.getAddress().getStreet());
//        });
//        return page;
    }

    @Override
    @Transactional(readOnly = true)
    default Optional<Person> findById(Long id) {
        return Optional.of(getPersonWithAddressById(id));
    }

}
