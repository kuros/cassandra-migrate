package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.model.Change;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.utils.ScriptLoader;
import com.github.kuros.cassandra.migrate.utils.ScriptReader;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusCommand extends BaseCommand {

    StatusCommand(final SelectedOptions selectedOptions) {
        super(selectedOptions);
    }

    public void execute(final String... params) {

        try {
            getDbHelper().open();


            final Map<String, Change> changeMap = getChangeMap();

            final List<Change> changes = ScriptLoader.loadScripts(this.paths.getScriptPath());

            println(printStream, "ID\t\t\t\tstatus\t\tDescription");
            println(printStream, Util.horizontalLine("", 80));

            for (Change change : changes) {
                if (changeMap.containsKey(change.getId())) {
                    change.setStatus("Done");
                } else {
                    change.setStatus("Pending");
                }

                println(printStream, change.getId() + "\t" + change.getStatus() + "\t\t" + change.getDescription());
            }

        } catch (final Throwable throwable) {
            throw new MigrationException(throwable);
        } finally {
            getDbHelper().close();
        }
    }

    private void println(final PrintStream printStream, final String s) {
        printStream.println(s);
    }

    private Map<String, Change> getChangeMap() {
        final Map<String, Change> idMap = new HashMap<String, Change>();
        List<Change> changes = getDbHelper().findAll();
        for (Change change : changes) {
            idMap.put(change.getId(), change);
        }

        return idMap;
    }

    private void applyChange(final Change change) {


        try {
            final ScriptReader scriptReader = new ScriptReader(Util.file(paths.getScriptPath(), change.getFileName()), environment().getCharset(), true);
            List<String> queries = scriptReader.getQueries(environment().getDelimeter());
            System.out.println(Util.horizontalLine("Applying: " + change.getFileName(), 10));
            for (String query : queries) {
                if (query.trim().length() != 0) {
                    System.out.println(query);
                    getDbHelper().executeCQL(query);
                }
            }
            getDbHelper().delete(change);
        } catch (IOException e) {
            throw new MigrationException(e);
        }
    }
}
