package com.company.config;

import com.company.rest.resource.PeopleResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/resources")
public class JerseyRestConfig extends ResourceConfig {

    public JerseyRestConfig() {
        // do NOT use the packages method when using a Spring Boot executable jar/war - Jersey scanning won't be able to find your Resource classes
        //packages("com.company.rest.resource");
        register(PeopleResource.class);
    }

}
