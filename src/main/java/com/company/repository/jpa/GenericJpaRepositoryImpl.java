package com.company.repository.jpa;

import com.company.model.jpa.Persistable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class GenericJpaRepositoryImpl<T extends Persistable, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericJpaRepository<T, ID> {

    private final EntityManager entityManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

//    @Override
//    @Transactional
//    public void delete(T entity) {
//        EntityManager em = getEntityManager();
//        if (em.contains(entity)) {
//            em.remove(entity);
//        } else {
//            // must merge and refresh for successful removal
//            T managedEntity = em.merge(entity);
//            em.refresh(managedEntity);
//            em.remove(managedEntity);
//        }
//    }
//
//    @Override
//    @Transactional
//    public <S extends T> S save(S entity) {
//        EntityManager em = getEntityManager();
//        if (em.contains(entity)) {
//            return super.save(entity);
//        }
//        // must merge and refresh for successful save
//        S managedEntity = em.merge(entity);
//        em.refresh(managedEntity);
//        return super.save(managedEntity);
//    }

}
