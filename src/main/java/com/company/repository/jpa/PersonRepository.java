package com.company.repository.jpa;


import com.company.model.jpa.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        hints.put("javax.persistence.fetchgraph", graph);
        return getEntityManager().find(Person.class, id, hints);
    }

//    @QueryHints(value = {
//            @QueryHint(name = "javax.persistence.fetchgraph", value = "address")
//    }, forCounting = false)
//    @org.springframework.data.jpa.repository.EntityGraph(
//
//            attributePaths = { "address" },
//            type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH
//    )
//    Page<Person> findAllWithAddress(Pageable pageable);

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    default Page<Person> findAllWithAddress(Pageable pageable) {
        EntityGraph<Person> graph = getEntityManager().createEntityGraph(Person.class);
        graph.addSubgraph("address"); // tell JPA to fetch the address

        Query countQuery = getEntityManager().createQuery("select count(*) from " + Person.class.getSimpleName());
        String hql = "select p from " + Person.class.getSimpleName() + " p";
        if (pageable.getSort() != null) {
            hql += " order by ";
            while (pageable.getSort().iterator().hasNext()) {
                hql += " " + pageable.getSort().iterator().next().getProperty();
            }
        }
        CriteriaQuery<Person> query = getEntityManager().getCriteriaBuilder().createQuery(Person.class);
        if (pageable.getSort() != null) {
//            List<Order> orders = new ArrayList<>();
//            pageable.getSort().getOrderFor(pageable.getSort().)
//            pageable.getSort().iterator().forEachRemaining(order -> orders.add(order.get));
            query.orderBy(pageable.getSort().stream().collect(Collectors.toList()));
        }
        TypedQuery<Person> typedQuery = getEntityManager()
                                            .createQuery(query)
                                            .setFirstResult((int) pageable.getOffset())
                                            .setMaxResults(pageable.getPageSize())
                                            .setHint("javax.persistence.fetchgraph", graph);
        return new PageImpl(typedQuery.getResultList(), pageable, (long) countQuery.getSingleResult());
    }

}
