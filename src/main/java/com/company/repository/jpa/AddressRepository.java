package com.company.repository.jpa;

import com.company.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

//@Api(tags = "Address Entity")
@Repository
//@RepositoryRestResource
public interface AddressRepository extends JpaRepository<Address, Long> {

    // add a endpoint

    // /addresses/search/find-by-street?street=...
    @RestResource(rel = "find-by-street", path="find-by-street")
    // no transactional annotation needed here, but the implementation will be transactional
    Address findFirstByStreet(@Param(value = "street") String street); // no implementation needed - Spring will implement it by looking at method name

    // hide a endpoint

    @Override
    @RestResource(exported = false) // does not prevent link creation though
    // no transactional annotation needed here, but the implementation will be transactional
    Optional<Address> findById(@PathVariable Long id);

}
