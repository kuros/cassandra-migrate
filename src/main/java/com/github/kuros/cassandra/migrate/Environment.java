package com.github.kuros.cassandra.migrate;

import com.github.kuros.cassandra.migrate.exception.MigrationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

public class Environment {

    public static final String CHANGELOG = "CHANGELOG";

    private final String delimeter;
    private final String username;
    private final String password;
    private final String contactPoint;
    private final String keyspace;
    private final String port;
    private final String changeLog;
    private final String charset;


    public Environment(final File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(inputStream);

            this.delimeter = ";";
            this.contactPoint = prop.getProperty(Keys.contact_point.name());
            this.keyspace = prop.getProperty(Keys.keyspace.name());
            this.port = prop.getProperty(Keys.port.name(), "9042");
            this.username = prop.getProperty(Keys.username.name());
            this.password = prop.getProperty(Keys.password.name());
            this.changeLog = prop.getProperty(Keys.changelog.name(), CHANGELOG.toUpperCase());
            this.charset = prop.getProperty(Keys.charset.name(), Charset.defaultCharset().name());
        } catch (Exception e) {
            throw new MigrationException("Environment file not found", e);
        }
    }

    public String getDelimeter() {
        return delimeter;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getContactPoint() {
        return contactPoint;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public String getPort() {
        return port;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public String getCharset() {
        return charset;
    }

    private enum Keys {
        contact_point, keyspace, port, username, password, delimeter, changelog, charset
    }
}
