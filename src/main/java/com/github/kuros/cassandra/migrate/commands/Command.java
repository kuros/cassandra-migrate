package com.github.kuros.cassandra.migrate.commands;

public interface Command {
    void execute(String... params);
}
