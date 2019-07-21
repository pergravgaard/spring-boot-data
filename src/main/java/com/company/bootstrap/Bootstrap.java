package com.company.bootstrap;

import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import com.company.repository.jpa.AddressRepository;
import com.company.repository.jpa.PersonRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Bootstrap {

    private Bootstrap() {

    }

    public static Address createArbitraryAddress(AddressRepository addressRepository) {
        Address address = new Address();
        address.setStreet("Hollywood Avenue");
        address.setNo("21C");
        address.setZipCode("90210");
        address.setCity("Los Angeles");
        address.setState("California");
        address.setCountry("USA");
        return addressRepository.save(address);
    }

    public static void createTheBauers(PersonRepository personRepository, Address address, ZonedDateTime utcPointInTime) {
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
    public static void createPersons(PersonRepository personRepository, Address address) {
        LocalDateTime localDateTime = LocalDateTime.of(1971, 12, 10, 14, 15);
        ZonedDateTime utcPointInTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);

        List<String> lastNames = Arrays.asList("Andersen", "Andersen", "Christensen", "Damgaard", "Espersen", "Frederiksen", "Gaardbo", "Hansen", "Ibsen", "Jakobsen", "Karlsen", "Ladefoged", "Mortensen", "Nielsen", "Olsen", "Petersen", "Quist", "Rasmussen", "Sørensen", "Thomsen", "Udsen", "Villadsen", "Westergaard");
        List<String> firstNames = Arrays.asList("Axel", "Børge", "Chris", "Åge", "Øjvind");

        AtomicInteger counter = new AtomicInteger(0);
        final int size = firstNames.size();
        List<Person> persons = lastNames.stream().map(s -> {
            int i = counter.getAndIncrement() % size;
            Person p = new Person();
            p.setFirstName(firstNames.get(i));
            p.setLastName(s);
            p.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).plusYears(25).withZoneSameLocal(ZoneOffset.ofHours(-7)));
            p.setAddress(address);
            return p;
        }).collect(Collectors.toList());
        personRepository.saveAll(persons);
    }

}
