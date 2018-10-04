package com.company.repository.jpa;

import com.company.model.jpa.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
public interface GenericJpaRepository<T extends Persistable, ID extends Serializable> extends JpaRepository<T, ID> {

    EntityManager getEntityManager();

}
