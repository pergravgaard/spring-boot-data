package com.company.config;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/resources")
public class JerseyRestConfig extends ResourceConfig {

    public JerseyRestConfig() {
        packages("com.company.rest.resource");
    }

//    public Set<Class<?>> getClasses() {
//        return new HashSet<>(
//                Arrays.asList(
//                        Person.class,
//                        NotFoundExceptionHandler.class,
//                        AlreadyExistsExceptionHandler.class
//                )
//        );
//    }

}
