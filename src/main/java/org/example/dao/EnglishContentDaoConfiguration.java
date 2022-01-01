package org.example.dao;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Configuration class for passing arguments to dao implementation from Quarkus context
 */
@Singleton
public class EnglishContentDaoConfiguration {
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String url;
    @ConfigProperty(name = "quarkus.datasource.username")
    String userName;
    @ConfigProperty(name = "quarkus.datasource.password")
    String password;

    @Singleton
    @Produces
    public EnglishContentDao englishContentDao() {
        return new EnglishContentDaoImpl(userName, password, url);
    }
}
