package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.model.Change;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.utils.ScriptLoader;
import com.github.kuros.cassandra.migrate.utils.ScriptReader;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpCommand extends BaseCommand {

    private static final String FIND_ALL = "";

    UpCommand(final SelectedOptions selectedOptions) {
        super(selectedOptions);
    }

    public void execute(final String... params) {

        try {
            getDbHelper().open();
            final List<Change> changes = ScriptLoader.loadScripts(this.paths.getScriptPath());
            List<Change> applied = getDbHelper().findAll();
            Change lastApplied = null;
            if (!applied.isEmpty()) {
                Collections.reverse(applied);
                 lastApplied = applied.get(0);
            }

            for (Change change : changes) {
                if (lastApplied != null
                        && (change.getDate().before(lastApplied.getDate())
                        || change.getDate().equals(lastApplied.getDate()))) {
                    continue;
                }

                applyChange(change);
            }

        } catch (final Throwable throwable) {
            throw new MigrationException(throwable);
        } finally {
            getDbHelper().close();
        }
    }

    private void applyChange(final Change change) {
        try {
            final ScriptReader scriptReader = new ScriptReader(Util.file(paths.getScriptPath(), change.getFileName()), environment().getCharset(), false);
            List<String> queries = scriptReader.getQueries(environment().getDelimeter());
            System.out.println(Util.horizontalLine("Applying: " + change.getFileName(), 10));
            for (String query : queries) {
                if (query.trim().length() != 0){
                    System.out.println(query);
                    getDbHelper().executeCQL(query);
                }
            }
            getDbHelper().insert(change);
        } catch (IOException e) {
            throw new MigrationException(e);
        }
    }
}
