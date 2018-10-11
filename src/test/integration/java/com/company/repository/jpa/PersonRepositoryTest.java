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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PersonRepositoryTest {

    private LocalDateTime localDateTime;
    private ZonedDateTime utcPointInTime;
    private List<String> lastNames;

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

        List<Person> persons = new ArrayList<>();
        lastNames = Arrays.asList("Andersen", "Christensen", "Damgaard", "Espersen", "Frederiksen");
        for (int i = 0; i < lastNames.size(); i++) {
            Person p = new Person();
            p.setFirstName("First" + i);
            p.setLastName(lastNames.get(i));
            p.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).plusYears(25).withZoneSameLocal(ZoneOffset.ofHours(-7)));
            p.setAddress(address);
            persons.add(p);
        }
        personRepository.saveAll(persons);
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

    @Test
    public void testFindAllWithAddress() {
        Page<Person> persons = personRepository.findAllWithAddress(PageRequest.of(1, 3));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(Sort.unsorted(), persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(3, persons.getNumberOfElements());
        assertEquals(1, persons.getNumber());

        persons = personRepository.findAllWithAddress(PageRequest.of(0, 3));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(Sort.unsorted(), persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(3, persons.getNumberOfElements());
        assertEquals(0, persons.getNumber());

        persons = personRepository.findAllWithAddress(PageRequest.of(2, 3));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(Sort.unsorted(), persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(1, persons.getNumberOfElements());
        assertEquals(2, persons.getNumber());

        persons.forEach(person -> {
            assertNotNull(person.getAddress().getStreet());
            System.out.println(person.getLastName() + ", " + person.getFirstName());
        });

    }

    @Test
    public void testFindAllWithAddressSorted() {
        Sort sort = Sort.by(
                new Sort.Order(Sort.Direction.ASC, "lastName"),
                new Sort.Order(Sort.Direction.DESC, "firstName")
        );
        Page<Person> persons = personRepository.findAllWithAddress(PageRequest.of(0, 3, sort));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(sort, persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(3, persons.getNumberOfElements());
        assertEquals(0, persons.getNumber());
        assertEquals(lastNames.get(0), persons.getContent().get(0).getLastName());
        assertEquals("Kim", persons.getContent().get(1).getFirstName());
        persons.forEach(person -> {
            assertNotNull(person.getAddress().getStreet());
//            System.out.println(person.getLastName() + ", " + person.getFirstName());
        });

        persons = personRepository.findAllWithAddress(PageRequest.of(1, 3, sort));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(sort, persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(3, persons.getNumberOfElements());
        assertEquals(1, persons.getNumber());
        assertEquals(lastNames.get(1), persons.getContent().get(0).getLastName());
        persons.forEach(person -> {
            assertNotNull(person.getAddress().getStreet());
//            System.out.println(person.getLastName() + ", " + person.getFirstName());
        });


        persons = personRepository.findAllWithAddress(PageRequest.of(2, 3, sort));
        assertEquals(7, persons.getTotalElements());
        assertEquals(3, persons.getTotalPages());
        assertEquals(sort, persons.getSort());
        assertEquals(3, persons.getSize());
        assertEquals(1, persons.getNumberOfElements());
        assertEquals(2, persons.getNumber());

        persons.forEach(person -> {
            assertNotNull(person.getAddress().getStreet());
//            System.out.println(person.getLastName() + ", " + person.getFirstName());
        });

    }

}