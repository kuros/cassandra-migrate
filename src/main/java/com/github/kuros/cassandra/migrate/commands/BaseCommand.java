package com.github.kuros.cassandra.migrate.commands;

import com.datastax.driver.core.utils.UUIDs;
import com.github.kuros.cassandra.migrate.Environment;
import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.option.SelectedPaths;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public abstract class BaseCommand implements Command {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    protected final SelectedOptions options;
    protected final SelectedPaths paths;
    private Environment environment;
    protected PrintStream printStream;
    protected DBHelper dbHelper;


    protected BaseCommand(SelectedOptions selectedOptions) {
        this.options = selectedOptions;
        this.paths = selectedOptions.getPaths();
        this.printStream = System.out;
    }

    protected String getNextID() {
        try {
            // Ensure that two subsequent calls are less likely to return the same value.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final Date now = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));

        return dateFormat.format(now);
    }

    protected String changelogTable() {
        return environment().getChangeLog();
    }

    protected File environmentFile() {
        return Util.file(paths.getEnvPath(), options.getEnvironment() + ".properties");
    }

    protected File existingEnvironmentFile() {
        File envFile = environmentFile();
        if (!envFile.exists()) {
            throw new MigrationException("Environment file missing: " + envFile.getAbsolutePath());
        }
        return envFile;
    }

    protected Environment environment() {
        if (environment != null) {
            return environment;
        }
        environment = new Environment(existingEnvironmentFile());
        return environment;
    }

    protected DBHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(environment());
        }
        return dbHelper;
    }
}
