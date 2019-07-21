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

/**
 * You can use swagger UI (/swagger-ui.html) to discover this REST endpoint and it's descendants or just visit /api or /api/persons and take advantage of HAL
 */
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

    @RestResource(rel = "find-all-by-lastname-containing", path="find-all-by-lastname-containing")
    // no transactional annotation needed here, but the implementation will be transactional
    List<Person> findAllByLastNameContaining(@Param("lastNamePart") String lastNamePart);

    /**
     * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
     * This method provides sorted filtering of fields firstName and lastName.
     * Try these URL's:
     * - /api/persons
     * - /api/persons/search
     * - /api/persons/search/find-all-by-firstname-containing-and-lastname-containing?firstNamePart=&lastNamePart=sen&sort=lastName,asc&sort=firstName,asc
     *
     * Note that the order of the sort parameters matters. Also note that sorting of æ, ø and å is not correct (TODO: Fix).
     * @param pageable
     * @param firstNamePart
     * @param lastNamePart
     * @return Returns a pageable wrapped around the persons found
     */
    @RestResource(rel = "find-all-by-firstname-containing-and-lastname-containing", path="find-all-by-firstname-containing-and-lastname-containing")
    // no transactional annotation needed here, but the implementation will be transactional
    Page<Person> findAllByFirstNameContainingAndLastNameContaining(Pageable pageable, @Param("firstNamePart") String firstNamePart, @Param("lastNamePart") String lastNamePart);

    // See https://www.thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
    @SuppressWarnings("unchecked")
    // won't be exported by Rest by default due to not following naming convention
    @Transactional(readOnly = true) // the transactional annotation is needed when writing the implementation here
    default Person getPersonWithAddressById(Long id) {

//        Person one = getOne(id);
//        one.getAddress().getStreet(); // works, but is not optimal for performance (N + 1 problem)
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
