package com.github.kuros.cassandra.migrate;

import com.github.kuros.cassandra.migrate.commands.CommandLineExecutor;

public class CassandraMigrate {

    public static void main(String[] args) {
        String[] arguments = {"--path=./testdir", "down", "5", "--trace"};
        CommandLineExecutor executor = new CommandLineExecutor(arguments);
        executor.execute();
    }
}
