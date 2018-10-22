package com.company.generator;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.io.File;
import java.util.EnumSet;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class HibernateSchemaGenerator {

    public static void main(String[] args) {
        String file = "src/main/resources/db/changelog/changes/v0001.sql";
        String resource = "mariadb-development.properties";
        String entityPackage = "com.company.model";
        //EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT, TargetType.DATABASE);
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
        generate(file, resource, entityPackage, targetTypes, SchemaExport.Action.CREATE);
    }

    public static void generate(String file, String resource, String entityPackage, EnumSet<TargetType> targetTypes, SchemaExport.Action exportAction) {

        try {

            MetadataSources metadata = new MetadataSources(
                    new StandardServiceRegistryBuilder()
                        .loadProperties(new File(getClassLoader().getResource(resource).getFile()))
                        .build()
            );

            new Reflections(entityPackage).getTypesAnnotatedWith(Entity.class).forEach(metadata::addAnnotatedClass);

            new File(file).delete();

            SchemaExport export = new SchemaExport();
            export.setDelimiter(";");
            export.setFormat(true);
            export.setOutputFile(file);
            export.execute(targetTypes, exportAction, metadata.buildMetadata());
            System.exit(0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
