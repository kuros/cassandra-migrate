package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.utils.Files;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.util.Properties;

public class NewCommand extends BaseCommand {

    NewCommand(final SelectedOptions selectedOptions) {
        super(selectedOptions);
    }

    public void execute(final String... params) {
        if (Util.isEmpty(params)) {
            throw new MigrationException("No description specified for new migration.");
        }
        String description = params[0];
        Properties variables = new Properties();
        variables.setProperty("description", description);
        existingEnvironmentFile();
        String filename = getNextID() + "_" + description.replace(' ', '_') + ".cql";

        copyDefaultTemplate(variables, filename);

        printStream.println(filename);
    }

    private void copyDefaultTemplate(Properties variables, String filename) {
        Files.INSTANCE.copyResourceTo("templates/migration.cql", Util.file(paths.getScriptPath(), filename),
                variables);
    }
}
