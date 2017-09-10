package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.model.Change;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;
import com.github.kuros.cassandra.migrate.utils.ScriptLoader;
import com.github.kuros.cassandra.migrate.utils.ScriptReader;
import com.github.kuros.cassandra.migrate.utils.Util;

import java.io.IOException;
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
            List<Change> applied = getDbHelper().findAll();

            final Map<String, Change> changeMap = getChangeMap();

            Collections.reverse(applied);


            int downCount = Integer.parseInt(this.options.getParams());

            downCount = downCount > applied.size() ? applied.size() : downCount;

            for (int j = 0; j < downCount; j++) {
                applyChange(changeMap.get(applied.get(j).getId()));
            }

        } catch (final Throwable throwable) {
            throw new MigrationException(throwable);
        } finally {
            getDbHelper().close();
        }
    }

    private Map<String, Change> getChangeMap() {
        final Map<String, Change> idMap = new HashMap<String, Change>();
        final List<Change> changes = ScriptLoader.loadScripts(this.paths.getScriptPath());
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
