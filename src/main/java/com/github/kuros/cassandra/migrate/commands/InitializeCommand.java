package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.utils.Files;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.io.File;
import java.util.Properties;


public class InitializeCommand extends BaseCommand {

    InitializeCommand(final SelectedOptions selectedOptions) {
        super(selectedOptions);
    }

    public void execute(final String... params) {
        final File basePath = paths.getBasePath();
        final File scriptPath = paths.getScriptPath();

        printStream.println("Initializing: " + basePath);

        createDirectoryIfNecessary(basePath);
        ensureDirectoryIsEmpty(basePath);

        createDirectoryIfNecessary(paths.getEnvPath());
        createDirectoryIfNecessary(scriptPath);

        Files.INSTANCE.copyResourceTo("templates/README", Util.file(basePath, "README"));
        Files.INSTANCE.copyResourceTo("templates/environment.properties", environmentFile());
        Files.INSTANCE.copyResourceTo("templates/migration.cql",
                Util.file(scriptPath, getNextID() + "_first_migration.cql"), new Properties() {
                    {
                        setProperty("description", "First migration.");
                    }
                });
        printStream.println("Done!");
        printStream.println();
    }

    private void ensureDirectoryIsEmpty(File path) {
        String[] list = path.list();
        if (list.length != 0) {
            for (String entry : list) {
                if (!entry.startsWith(".")) {
                    throw new MigrationException("Directory must be empty (.git etc allowed): " + path.getAbsolutePath());
                }
            }
        }
    }

    private void createDirectoryIfNecessary(File path) {
        if (!path.exists()) {
            printStream.println("Creating: " + path.getName());
            if (!path.mkdirs()) {
                throw new MigrationException(
                        "Could not create directory path for an unknown reason. Make sure you have access to the directory.");
            }
        }
    }


}
