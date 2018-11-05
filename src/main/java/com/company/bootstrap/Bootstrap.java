package com.company.bootstrap;

import com.company.model.jpa.Address;
import com.company.model.jpa.Person;
import com.company.repository.jpa.PersonRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bootstrap {

    private Bootstrap() {

    }

    public static void createPersons(PersonRepository personRepository, Address address) {
        LocalDateTime localDateTime = LocalDateTime.of(1971, 12, 10, 14, 15);
        ZonedDateTime utcPointInTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);

        List<String> lastNames = Arrays.asList("Andersen", "Christensen", "Damgaard", "Espersen", "Frederiksen", "Gaardbo", "Hansen", "Ibsen", "Jakobsen", "Karlsen", "Ladefoged", "Mortensen", "Nielsen", "Olsen", "Petersen", "Quist", "Rasmussen", "Sørensen", "Thomsen", "Udsen", "Villadsen", "Westergaard");
        List<Person> persons = lastNames.stream().map(s -> {
            Person p = new Person();
            p.setFirstName("Firstname");
            p.setLastName(s);
            p.setBirthDateTime(utcPointInTime.withNano(0).withSecond(0).plusYears(25).withZoneSameLocal(ZoneOffset.ofHours(-7)));
            p.setAddress(address);
            return p;
        }).collect(Collectors.toList());
        personRepository.saveAll(persons);
    }
}
