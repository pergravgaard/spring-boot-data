package com.company;

import liquibase.exception.CommandLineParsingException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.Main;

import java.io.IOException;

public class LiquibaseRunner extends Main {


//    public static void main(String[] args) {
//        String[] customArgs = new String[] {
//            "update"
//        };
//
//        int errorLevel = 0;
//        try {
//            errorLevel = run(customArgs);
//        } catch (LiquibaseException e) {
//            System.exit(-1);
//        }
//        System.exit(errorLevel);
//    }

    public static void main(String[] args) {
        String[] customArgs = new String[] {
                "--driver", "org.mariadb.jdbc.Driver",
                //"--classpath", "\path\to\classes:jdbcdriver.jar",
                "--changeLogFile", "db/changelog/db.changelog-master.yaml",
                "--url", "jdbc:mariadb://localhost:3306/example_dev_db?autoReconnect=true&useLegacyDatetimeCode=false&serverTimezone=GMT",
                "--username", "example",
                "--password", "example",
                "update"
        };

        try {
            run(customArgs);
        }
        catch (LiquibaseException e) {
        }
        System.exit(-1);
    }
}
