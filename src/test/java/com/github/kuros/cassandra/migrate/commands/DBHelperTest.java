package com.github.kuros.cassandra.migrate.commands;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.UUIDs;
import com.github.kuros.cassandra.migrate.Environment;
import com.github.kuros.cassandra.migrate.model.Change;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DBHelperTest {

    public static void main(String[] args) {
        Environment environment = new Environment(new File("/Users/rohit/Documents/github/cassandra-migrate/testdir/environments/development.properties"));

        DBHelper helper = new DBHelper(environment);

        helper.open();

//        Change change = new Change();
//
//        change.setId("20170909223353");
//        change.setDescription("desc-3");
//
//        helper.insert(change);
        List<Change> all = helper.findAll();
        for (Change change : all) {
            System.out.println(change.getId());
            System.out.println(change.getDescription());
        }

//        helper.executeCQL("insert into changelog(bucket, id, description) values (1, 'date', 'my-desc2');");

        ResultSet resultSet = helper.executeCQL("select * from changelog");

        for (Row row : resultSet) {
            System.out.println(row);
        }


        helper.close();
    }
}