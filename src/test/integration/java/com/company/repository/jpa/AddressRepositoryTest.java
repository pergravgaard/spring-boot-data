package com.company.repository.jpa;

import com.company.config.TestConfig;
import com.company.model.jpa.Address;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Before
    public void setUp() {

        Address address = new Address();
        address.setStreet("Hollywood Avenue");
        address.setNo("21C");
        address.setZipCode("90210");
        address.setCity("Los Angeles");
        address.setState("California");
        address.setCountry("USA");
        addressRepository.save(address);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFindFirstByStreet() {

        Address hollywoodAvenue = addressRepository.findFirstByStreet("Hollywood Avenue");

        assertNotNull(hollywoodAvenue);

    }
}