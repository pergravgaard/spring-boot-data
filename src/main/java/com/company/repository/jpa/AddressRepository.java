package com.company.repository.jpa;

import com.company.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel="address", path="address")
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Transactional(readOnly = true)
    Address findFirstByStreet(String street); // no implementation needed - Spring will implement it by looking at method name

}
