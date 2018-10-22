package com.company.repository.jpa;

import com.company.model.jpa.Persistable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Interface for getting a reference to the entity manager via the constructor
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface GenericJpaRepository<T extends Persistable, ID extends Serializable> extends JpaRepository<T, ID> {

    EntityManager getEntityManager();

    /**
     * Generic implementation for paginating all lazy-loaded relations via hints for the entity graph
     * @param pageable
     * @param entityClass
     * @param subGraphAttributes
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    default Page<T> findAllWithHints(Pageable pageable, Class<T> entityClass, String... subGraphAttributes) {

        EntityGraph<T> graph = getEntityManager().createEntityGraph(entityClass);
        for (String attr : subGraphAttributes) {
            graph.addSubgraph(attr); // tell JPA to fetch this attribute now despite it may be marked as lazy-loaded in the entity class
        }

        Query countQuery = getEntityManager().createQuery("select count(*) from " + entityClass.getSimpleName());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);

        if (pageable.getSort() != null) {
            List<Order> orders = QueryUtils.toOrders(pageable.getSort(), from, cb);
            criteriaQuery.orderBy(orders);
        }

        TypedQuery<T> typedQuery = getEntityManager()
                                        .createQuery(criteriaQuery)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .setHint("javax.persistence.fetchgraph", graph);
        return new PageImpl(typedQuery.getResultList(), pageable, (long) countQuery.getSingleResult());
    }


}
