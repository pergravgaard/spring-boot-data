package com.company.repository.jpa;

import com.company.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // no transactional annotation needed here, but the implementation will be transactional
    Address findFirstByStreet(String street); // no implementation needed - Spring will implement it by looking at method name

    // no transactional annotation needed here, but the implementation will be transactional
    @Override
    Optional<Address> findById(Long id);

}
