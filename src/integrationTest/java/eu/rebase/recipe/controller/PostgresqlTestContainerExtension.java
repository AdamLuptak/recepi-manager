package eu.rebase.recipe.controller;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

public class PostgresqlTestContainerExtension implements BeforeAllCallback {

    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");


    @Override
    public void beforeAll(ExtensionContext context) {
        Startables.deepStart(Stream.of(postgreSQLContainer)).join();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }
}
