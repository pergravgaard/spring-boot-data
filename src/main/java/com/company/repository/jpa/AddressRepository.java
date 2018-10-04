package com.company.repository.jpa;


import com.company.model.jpa.Address;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends GenericJpaRepository<Address, Long> {

    @Transactional(readOnly = true)
    Address findFirstByStreet(String street); // no implementation needed - Spring will implement it by looking at method name

}
