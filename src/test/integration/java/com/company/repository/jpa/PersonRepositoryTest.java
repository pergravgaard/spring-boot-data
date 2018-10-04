package com.company.repository.jpa;

import com.company.config.TestConfig;
import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

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

        LocalDateTime now = LocalDateTime.now();

        Person jack = new Person();
        jack.setFirstName("Jack");
        jack.setLastName("Bauer");
        jack.setBirthDateTime(now.withNano(0).withSecond(0).minusYears(40));
        jack.setAddress(address);
        personRepository.save(jack);

        Person kim = new Person();
        kim.setFirstName("Kim");
        kim.setLastName("Bauer");
        kim.setBirthDateTime(now.withNano(0).withSecond(0).minusYears(21));
        kim.setAddress(address);
        personRepository.save(kim);

    }

    @After
    public void tearDown() {
        personRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testFindAllByLastName() {

        List<Person> theBauers = personRepository.findAllByLastName("Bauer");

        assertNotNull(theBauers);
        assertEquals(2, theBauers.size());
    }

    @Test(expected = LazyInitializationException.class)
    public void testLazyLoadingExceptionForFindAll() {

        List<Person> persons = personRepository.findAllByLastName("Bauer");

        // now transaction is terminated, but you can still access the proxy object
        assertNotNull(persons.get(0).getAddress());

        // and you can access the id of proxy object
        assertNotNull(persons.get(0).getAddress().getId());

        // but you cannot access any other field
        persons.get(0).getAddress().getStreet();

    }

    // See https://vladmihalcea.com/how-does-a-jpa-proxy-work-and-how-to-unproxy-it-with-hibernate/
    @Test(expected = LazyInitializationException.class)
    public void testLazyLoadingExceptionForGetOne() {

        List<Person> theBauers = personRepository.findAllByLastName("Bauer");

        assertNotNull(theBauers.get(0).getAddress()); // as above

        Person aBauer = personRepository.getOne(theBauers.get(0).getId());
        // now transaction is terminated and JPA can't initialize the proxy object
        aBauer.getAddress();

    }

    @Test
    public void testFetchOfLazyLoadedRelation() {
        List<Person> persons = personRepository.findAllByLastName("Bauer");
        Person personWithAddress = personRepository.getPersonWithAddressById(persons.get(0).getId());
        assertNotNull(personWithAddress.getAddress().getStreet());
    }

}