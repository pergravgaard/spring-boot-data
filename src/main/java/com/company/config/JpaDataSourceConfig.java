package com.company.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;


public class JpaDataSourceConfig {

    private static final Logger Log = LogManager.getLogger(JpaDataSourceConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource(DatabasePopulator populator) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        Log.info("Using JDBC url: " + env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        DatabasePopulatorUtils.execute(populator, dataSource);
        return dataSource;
    }

    @Bean
    public DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(false);
        //populator.addScript(new ClassPathResource("bootstrap.sql")) // uncomment if you wanna use SQL for populating the database
        return populator;
    }

}
