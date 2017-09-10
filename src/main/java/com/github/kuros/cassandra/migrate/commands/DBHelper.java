package com.github.kuros.cassandra.migrate.commands;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.github.kuros.cassandra.migrate.Environment;
import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.model.Change;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBHelper {

    private static final String CREATE_TABLE = "CREATE TABLE {0} (bucket int, id text, description text, primary key((bucket), id))";

    private Environment environment;
    private Cluster cluster;
    private Session session;

    public DBHelper(final Environment environment) {
        this.environment = environment;
    }

    public void open() {
        try {
            final QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMaxPendingRefreshSchemaRequests(1);
            queryOptions.setConsistencyLevel(ConsistencyLevel.ALL);

            cluster = Cluster.builder()
                    .addContactPoint(environment.getContactPoint())
                    .withPort(Integer.valueOf(environment.getPort()))
                    .withCredentials(environment.getUsername(), environment.getPassword())
                    .withMaxSchemaAgreementWaitSeconds(30)
                    .withQueryOptions(queryOptions)
                    .build();

            getSession();
        } catch (final Throwable throwable) {
            throw new MigrationException("Unable to connect to Cassandra Cluster!", throwable);
        }
    }

    private Session getSession() {
        if (session == null) {
            session = getCluster()
                    .connect(environment.getKeyspace());

            if (isTableNotExistsInDB()) {
                createTable();
            }
        }

        return session;
    }

    public void insert(final Change change) {
        final Insert insert = QueryBuilder.insertInto(environment.getChangeLog())
                .value("bucket", 1)
                .value("id", change.getId())
                .value("description", change.getDescription());
        executeCQL(insert.toString());
    }

    public void delete(final Change change) {

        Delete.Where delete = QueryBuilder
                .delete()
                .all()
                .from(environment.getChangeLog())
                .where(QueryBuilder.eq("bucket", 1)).and(QueryBuilder.eq("id", change.getId()));

        executeCQL(delete.toString());
    }

    public List<Change> findAll() {
        Select select = QueryBuilder.select()
                .all()
                .from(environment.getChangeLog());

        final List<Change> result = new ArrayList<Change>();
        final ResultSet resultSet = executeCQL(select.toString());

        for (Row row : resultSet) {
            final Change change = new Change();
            change.setId(row.get("id", String.class));
            change.setDescription(row.get("description", String.class));

            result.add(change);
        }

        return result;
    }

    public ResultSet executeCQL(final String cql) {
        return getSession().execute(cql);
    }

    private void createTable() {
        final String query = MessageFormat.format(CREATE_TABLE, environment.getChangeLog());
        getSession().execute(query);
    }

    private boolean isTableNotExistsInDB() {
        Collection<TableMetadata> tables = getCluster()
                .getMetadata()
                .getKeyspace(environment.getKeyspace())
                .getTables();

        for (TableMetadata table : tables) {
            if (table.getName().equalsIgnoreCase(environment.getChangeLog())) {
                return false;
            }
        }

        return true;
    }

    private Cluster getCluster() {
        if (cluster == null) {
            open();
        }

        return cluster;
    }

    public void close() {
        try {
            if (session != null) {
                session.close();
            }

            if (cluster != null) {
                cluster.close();
            }
        } catch (final Throwable throwable) {
            System.out.println("Failed to close DB Session!");
        }
    }
}
