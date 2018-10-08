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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PersonRepositoryTest {

    private LocalDateTime localDateTime;
    private ZonedDateTime utcPointInTime;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Before
    public void setUp() {
        localDateTime = LocalDateTime.of(1971, 12, 10, 14, 15);
        utcPointInTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);

        Address address = new Address();
        address.setStreet("Hollywood Avenue");
        address.setNo("21C");
        address.setZipCode("90210");
        address.setCity("Los Angeles");
        address.setState("California");
        address.setCountry("USA");

        addressRepository.save(address);

//        System.out.println("local utcPointInTime: " + localDateTime);
//        System.out.println("utcPointInTime: " + utcPointInTime + ", offset: " + utcPointInTime.getOffset());

        Person jack = new Person();
        jack.setFirstName("Jack");
        jack.setLastName("Bauer");
        jack.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).withZoneSameLocal(ZoneOffset.ofHours(-7)));
        jack.setAddress(address);
        personRepository.save(jack);

        Person kim = new Person();
        kim.setFirstName("Kim");
        kim.setLastName("Bauer");
        kim.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).plusYears(25).withZoneSameLocal(ZoneOffset.ofHours(-7)));
        kim.setAddress(address);
        personRepository.save(kim);

    }

    @After
    public void tearDown() {
        personRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testZonedDateTimeAndAuditing() {

        Person jack = personRepository.findFirstByFirstNameAndLastName("Jack", "Bauer");

        // a ZonedDateTime field is always represented by a ZonedDateTime instance in the system default time zone, when fetched from DB
        ZonedDateTime jacksBirthDateTime = jack.getBirthDateTime().withZoneSameLocal(ZoneOffset.ofHours(-7));

        assertEquals(ZonedDateTime.now().getOffset(), jack.getCreatedDateTime().getOffset());
        assertEquals(ZoneOffset.UTC, jack.getBirthDateTime().getOffset());
        assertEquals(ZoneOffset.ofHours(-7), jacksBirthDateTime.getOffset());
        assertEquals(21, jacksBirthDateTime.getHour());
        assertEquals(jack.getCreatedDateTime(), jack.getLastModifiedDateTime());
        assertNull(jack.getCreatedBy());
        assertNull(jack.getLastModifiedBy());
        assertEquals(0, jack.getVersion().intValue());

        jack.setFirstName("Jacko");

        personRepository.save(jack);

        jack = personRepository.findById(jack.getId()).get();
//        jack = personRepository.findFirstByFirstNameAndLastName("Jacko", "Bauer");

        assertTrue(jack.getCreatedDateTime().isBefore(jack.getLastModifiedDateTime()));
        assertEquals(1, jack.getVersion().intValue());
    }

    @Test
    public void testFindAllByLastName() {

        List<Person> theBauers = personRepository.findAllByLastName("Bauer");

        assertNotNull(theBauers);
        assertEquals(2, theBauers.size());
    }

    @Test(expected = LazyInitializationException.class)
    public void testLazyLoadingExceptionFindAllByLastName() {

        List<Person> persons = personRepository.findAllByLastName("Bauer");

        // now transaction is terminated, but you can still access the proxy object
        assertNotNull(persons.get(0).getAddress());

        // and you can access the id of proxy object
        assertNotNull(persons.get(0).getAddress().getId());

        // but you cannot access any other field
        persons.get(0).getAddress().getStreet();

    }

    @Test(expected = LazyInitializationException.class)
    public void testLazyLoadingExceptionFindFirstByFirstNameAndLastName() {

        Person kimBauer = personRepository.findFirstByFirstNameAndLastName("Kim", "Bauer");

        // now transaction is terminated, but you can still access the proxy object
        assertNotNull(kimBauer.getAddress());

        // and you can access the id of proxy object
        assertNotNull(kimBauer.getAddress().getId());

        // but you cannot access any other field
        kimBauer.getAddress().getStreet();

    }

    // See https://vladmihalcea.com/how-does-a-jpa-proxy-work-and-how-to-unproxy-it-with-hibernate/
    // See https://vladmihalcea.com/entitymanager-find-getreference-jpa/
    @Test(expected = LazyInitializationException.class)
    public void testLazyLoadingExceptionGetOne() {

        Person jack = personRepository.findFirstByFirstNameAndLastName("Jack", "Bauer");

        // getOne returns a proxy object for the person instance, so the address can't be accessed
        personRepository.getOne(jack.getId()).getAddress();

    }

    @Test
    public void testFetchOfLazyLoadedRelation() {
        List<Person> persons = personRepository.findAllByLastName("Bauer");
        Person personWithAddress = personRepository.getPersonWithAddressById(persons.get(0).getId());
        assertNotNull(personWithAddress.getAddress().getStreet());
    }

}