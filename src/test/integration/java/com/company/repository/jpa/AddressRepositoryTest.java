package com.company.repository.jpa;

import com.company.bootstrap.Bootstrap;
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
        Bootstrap.createArbitraryAddress(addressRepository);
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