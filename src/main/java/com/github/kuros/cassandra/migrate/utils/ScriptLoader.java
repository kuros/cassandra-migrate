package com.github.kuros.cassandra.migrate.utils;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.model.Change;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScriptLoader {

    public static List<Change> loadScripts(final File scriptDir) {
        List<Change> migrations = new ArrayList<Change>();
        if (scriptDir.isDirectory()) {
            String[] filenames = scriptDir.list();
            if (filenames == null) {
                throw new MigrationException(scriptDir + " does not exist.\n");
            }
            for (String filename : filenames) {
                if (filename.endsWith(".cql")) {
                    Change change = parseChangeFromFilename(filename);
                    migrations.add(change);
                }
            }
        }

        Collections.sort(migrations);
        return migrations;
    }

    private static Change parseChangeFromFilename(final String filename) {
        try {
            Change change = new Change();
            int lastIndexOfDot = filename.lastIndexOf(".");
            String[] parts = filename.substring(0, lastIndexOfDot).split("_");
            change.setId(parts[0]);
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                if (i > 1) {
                    builder.append(" ");
                }
                builder.append(parts[i]);
            }
            change.setDescription(builder.toString());
            change.setFileName(filename);
            return change;
        } catch (Exception e) {
            throw new MigrationException("Error parsing change from file.  Cause: " + e + "\n", e);
        }
    }

}
