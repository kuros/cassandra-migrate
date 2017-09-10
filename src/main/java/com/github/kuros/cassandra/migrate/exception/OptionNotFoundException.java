package com.github.kuros.cassandra.migrate.exception;

import com.github.kuros.cassandra.migrate.option.Options;

public class OptionNotFoundException extends RuntimeException {

    public OptionNotFoundException(final String options) {
        super("Command not found: " + options);
    }
}
