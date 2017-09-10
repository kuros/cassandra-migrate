package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;

import static com.github.kuros.cassandra.migrate.option.OptionsParser.parse;

public class CommandLineExecutor {
    private PrintStream console = System.out;
    private String[] args;

    public CommandLineExecutor(final String[] args) {
        this.args = args;
    }

    public void execute() {
        SelectedOptions selectedOptions = null;
        try {
            selectedOptions = parse(args);
        } catch (Exception e) {
            printUsage();
            return;
        }

        try {
            if (!validOptions(selectedOptions) || selectedOptions.needsHelp()) {
                printUsage();
            } else {
                runCommand(selectedOptions);
            }
        } catch (Exception e) {
            console.printf("\nERROR: %s", e.getMessage());
            if (selectedOptions.isTrace()) {
                e.printStackTrace();
            }
        }
    }

    private void runCommand(final SelectedOptions selectedOptions) {
        final String commandString = selectedOptions.getCommand();

        console.printf("------------------------------------------------------------------------%n");
        console.printf("-- Cassandra Migrations - %s%n", commandString);
        console.printf("------------------------------------------------------------------------%n");

        long start = System.currentTimeMillis();
        boolean exceptionCaught = false;

        try {
            final Command command = Commands.resolveCommand(commandString.toUpperCase(), selectedOptions);
            command.execute(selectedOptions.getParams());
        } catch (Throwable t) {
            exceptionCaught = true;
            if (t instanceof MigrationException) {
                throw (MigrationException) t;
            } else {
                throw new MigrationException(t);
            }
        } finally {
            console.printf("------------------------------------------------------------------------%n");
            console.printf("-- Cassandra Migrations %s%n", (exceptionCaught) ? "FAILURE" : "SUCCESS");
            console.printf("-- Total time: %ss%n", ((System.currentTimeMillis() - start) / 1000));
            console.printf("-- Finished at: %s%n", new Date());
            console.printf("------------------------------------------------------------------------%n");
        }

    }


    private boolean validOptions(SelectedOptions selectedOptions) {
        if (!selectedOptions.needsHelp() && selectedOptions.getCommand() == null) {
            console.printf("No commands specified.%n");
            return false;
        }

        return validBasePath(selectedOptions.getPaths().getBasePath());
    }

    private boolean validBasePath(File basePath) {
        final boolean validDirectory = basePath.exists() && basePath.isDirectory();

        if (!validDirectory) {
            console.printf("Migrations path must be a directory: %s%n", basePath.getAbsolutePath());
        }

        return validDirectory;
    }

    private void printUsage() {
        console.printf(
                "%nUsage: cassandra-migrate commands [parameter] [--path=<directory>] [--env=<environment>] %n%n");
        console.printf("--path=<directory>   Path to repository.  Default current working directory.%n");
        console.printf("--env=<environment>  Environment to configure. Default environment is 'development'.%n");
        console.printf("--force              Forces script to continue even if SQL errors are encountered.%n");
        console.printf("--help               Displays this usage message.%n");
        console.printf("%n");
        console.printf("Commands:%n");
        console.printf("  info               Display build version informations.%n");
        console.printf("  init               Creates (if necessary) and initializes a migration path.%n");
        console.printf("  new <description>  Creates a new migration with the provided description.%n");
        console.printf("  up [n]             Run unapplied migrations, ALL by default, or 'n' specified.%n");
        console.printf("  down [n]           Undoes migrations applied to the database. ONE by default or 'n' specified.%n");
        console.printf("  status             Prints the changelog from the database if the changelog table exists.%n");
        console.printf("%n");
    }
}
