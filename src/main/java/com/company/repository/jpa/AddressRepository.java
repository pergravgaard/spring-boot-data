package com.company.repository.jpa;

import com.company.model.jpa.Address;
import io.swagger.annotations.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

//@RepositoryRestResource(path="addresses")
//@RepositoryRestResource(collectionResourceRel="addresses", path="addresses")
@Api(tags = "Address Entity")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // add a endpoint

    // /addresses/search/find-by-street?street=...
    @RestResource(rel = "find-by-street", path="find-by-street")
    @Transactional(readOnly = true)
    Address findFirstByStreet(@RequestParam String street); // no implementation needed - Spring will implement it by looking at method name

    // hide a endpoint

    @Override
    @RestResource(exported = false) // does not prevent link creation though
    @Transactional(readOnly = true)
    Optional<Address> findById(@PathVariable Long id);

}
