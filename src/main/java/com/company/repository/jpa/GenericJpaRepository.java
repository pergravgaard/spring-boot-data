package com.company.repository.jpa;

import com.company.model.jpa.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Interface for getting a reference to the entity manager via the constructor
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface GenericJpaRepository<T extends Persistable, ID extends Serializable> extends JpaRepository<T, ID> {

    EntityManager getEntityManager();

}
