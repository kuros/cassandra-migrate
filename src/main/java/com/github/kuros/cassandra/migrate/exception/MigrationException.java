package com.github.kuros.cassandra.migrate.exception;

public class MigrationException extends RuntimeException {

    public MigrationException(final String message) {
        super(message);
    }

    public MigrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MigrationException(final Throwable cause) {
        super(cause);
    }
}
