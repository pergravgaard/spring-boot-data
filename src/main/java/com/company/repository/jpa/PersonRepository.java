package com.company.repository.jpa;


import com.company.model.jpa.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@RepositoryRestResource(collectionResourceRel="person", path="person")
@Repository
public interface PersonRepository extends GenericJpaRepository<Person, Long> {

    // no transactional annotation needed here, but the implementation will be transactional
    List<Person> findAllByLastName(String lastName); // no implementation needed - Spring will implement it by looking at method name

    // no transactional annotation needed here, but the implementation will be transactional
    Person findFirstByFirstNameAndLastName(String firstName, String lastName); // no implementation needed - Spring will implement it by looking at method name

    // See https://www.thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true) // the transactional annotation is needed when writing the implementation here
    default Person getPersonWithAddressById(Long id) {

//        Person one = getOne(id);
//        one.getAddress().getStreet(); // works, but is not optimal for performance
//        return one;

        EntityGraph<Person> graph = getEntityManager().createEntityGraph(Person.class);
        graph.addSubgraph("address"); // tell JPA to fetch the address

        Map hints = new HashMap();
        hints.put("javax.persistence.loadgraph", graph);
        return getEntityManager().find(Person.class, id, hints);

    }
}
