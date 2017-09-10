package com.github.kuros.cassandra.migrate;

import com.github.kuros.cassandra.migrate.commands.CommandLineExecutor;

public class CassandraMigrate {

    public static void main(String[] args) {
        CommandLineExecutor executor = new CommandLineExecutor(args);
        executor.execute();
    }
}
