
# TODO

- Spring Data Solr
- Test Rest Controller
- Swagger detect auto created REST controllers
- REST fetch lazy loaded relations
- REST Error Handling
- REST prevent exposure of method DONE
- REST expose a method DONE
- How many conversion services in Spring?
- REST: Convert ZonedDateTime. WORKAROUND - CREATE BUG REPORT
- getOne (getReference and lazy initialization exception) DONE
- disable Open In Session View. DONE
- Check ZonedDateTime in DB. DONE

# Introduction

This project shows how to use Spring Data as the JPA layer and how to configure Spring manually, i.e. getting rid of the auto configuration.

Furthermore we'll learn how to use the new Java 8 time classes in JPA and in JPA auditing.

Some people dislikes Spring due to the "magic" of how easy it is to get a project up and running.
They feel like they loose control.
Of course there is no magic to it, but just a lot of sensible default configuration for each layer/component in your application.

But you do NOT loose control! Everything can be configured.
Admitted, to customize the configuration can sometimes be time consuming.
But the people behind Spring has done a great job (and continues to do so) of making this is as easy as possible.
Furthermore the different Spring projects are Open Source and nowadays there is a lot of documentation and guides out there.

So if you do not have any custom needs, you're up and running in no time with Spring Boot.

But at the end of the day you always have custom needs that requires changes in the default configuration of every layer.

So we'll disable the default configuration of the JPA layer and configure it manually.
Not by XML though, but pure Java classes only. Except from a few property files.

This project is versioned by Git and the different branches are the different "steps" in this guide

## Branch: spring-boot-data-jpa-only:

- Gradle: Split test folder into unit and integration
    - Just apply java-groovy-test-structure.gradle in build.gradle
- Spring Bootify your project
    - Add Gradle plugins and dependencies
    - Create Java application class
    - Import Java configuration classes
    - Add gradle.properties and specify which class Spring Boot should use as the main class (only relevant if more than one class is runnable, i.e. contains a static main method)
    - Exclude auto configuration for JPA and data sources
    - Manually configure JPA and data sources    
- Spring: Log4J2
    - Add Gradle dependencies
    - Apply versions.gradle in build.gradle
- Spring: JPA
    - Java 8 Time classes and auditing
    - Transactional behavior
    - Autowiring (Dependency Injection) and singleton components (repositories and services)
    - Lazy loaded relations
    - Bean Validation & Conversion Service
- Spring: Remove "magical" auto configuration
- Spring: Liquibase & Hibernate Schema Generator

-----


# Running the application

We'll show how to run your application.
For more information visit https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html

## From your favorite IDE

Just run/debug the class ExampleApplication


## From Gradle Plugin

Just execute `gradle bootRun`

## As packaged application

Just execute `gradle clean build`

and then `java -jar build/libs/spring-boot-data-0.0.1-SNAPSHOT.jar`

Noticed that you never installed or referred an existing HTTP server?

# Open Session in View Is An Anti-Pattern

Read this article on the impact on starting JPA session in HTTP layer:
https://vladmihalcea.com/the-open-session-in-view-anti-pattern/

If using DTO projections anyway as recommended in the article, there's is absolutely no need to keep the session open in the view layer.

This pattern should be avoided at all costs.

# Hibernate Schema Generator

Use Hibernates reflection classes to create sql files based on your entities

# Other Resources

- Spring (https://spring.io)
- Spring Projects (https://spring.io/projects)
- Spring Documentation (https://spring.io/docs)
- Spring Guides (https://spring.io/guides)
- Spring Reference (https://spring.io/docs/reference)
- Baeldung (https://www.baeldung.com)
- JPA getReference vs find (https://vladmihalcea.com/entitymanager-find-getreference-jpa/)
- Spring Data JPA Tutorial (https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/)
- JPA and Java 8 Date Time (https://vladmihalcea.com/whats-new-in-jpa-2-2-java-8-date-and-time-types/)
- Don't use java.util.Date (https://programminghints.com/2017/05/still-using-java-util-date-dont/)
- Resource Naming (https://restfulapi.net/resource-naming/)
- JAX-RS vs Spring MVC (https://dzone.com/articles/7-reasons-i-do-not-use-jax-rs-in-spring-boot-web-a)
- Spring REST HAL (https://www.baeldung.com/spring-rest-hal)
- Spring Boot & Liquibase (https://objectpartners.com/2018/05/09/liquibase-and-spring-boot/)