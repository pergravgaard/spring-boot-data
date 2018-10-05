
#TODO

- disable Open In Session View
- Check ZonedDateTime in DB
- REST: Convert ZonedDateTime

#Introduction

This project shows how to use Spring Data as the JPA layer and how to configure Spring manually, i.e. getting rid of the auto configuration.

Furthermore we'll learn how to use the new java.time classes in JPA and in JPA auditing.

Many people dislikes Spring due to the "magic" of how easy it is to get started on a project.
Of course there is no magic to it, but just a lot of sensible default configuration for each layer/component in your application.

So if you do not have any custom needs, you're done.

But at the end of the day you always have custom needs that requires changes in the default configuration of every layer provided by Spring.

So we'll disable the default configuration of the JPA layer and configure it manually. Not by XML, but pure Java classes only, except from a few property files.


-----

Noticed that you never installed or referred an existing HTTP server?

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

# Open Session in View Is An Anti-Pattern

Read this article on the impact on starting JPA session in HTTP layer:
https://vladmihalcea.com/the-open-session-in-view-anti-pattern/

This pattern should be avoided at all costs.

#Other Resources

- Spring (https://spring.io)
- Spring Projects (https://spring.io/projects)
- Spring Documentation (https://spring.io/docs)
- Spring Guides (https://spring.io/guides)
- Baeldung (https://www.baeldung.com)
- Spring Data JPA Tutorial (https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/)