package com.company.repository.jpa;

import com.company.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Transactional(readOnly = true)
    Address findFirstByStreet(String street); // no implementation needed - Spring will implement it by looking at method name

    @Override
    @Transactional(readOnly = true)
    Optional<Address> findById(Long id);

}
