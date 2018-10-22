Branch: spring-boot-data-jpa-only:

- Spring: Log4J2
- Spring: Transactional JPA & Bean Validation
- Spring: Remove "magical" auto configuration
- Spring: Liquibase & Hibernate Schema Generator


#Introduction

This project shows how to use Spring Data as the JPA layer and how to configure Spring manually, i.e. getting rid of the auto configuration.

Furthermore we'll learn how to use the new java.time classes in JPA and in JPA auditing.

Some people dislikes Spring due to the "magic" of how easy it is to get started on a project.
Of course there is no magic to it, but just a lot of sensible default configuration for each layer/component in your application.

So if you do not have any custom needs with Spring Boot, you're done.

But at the end of the day you always have custom needs that requires changes in the default configuration of every layer.

So we'll disable the default configuration of the JPA layer and configure it manually.
Not by XML though, but pure Java classes only. Except from a few property files.


-----


#Running the application

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

#Other Resources

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