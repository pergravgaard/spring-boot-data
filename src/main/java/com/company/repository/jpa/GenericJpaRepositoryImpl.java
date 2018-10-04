package com.company.repository.jpa;

import com.company.model.jpa.Persistable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

}
