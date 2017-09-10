package com.github.kuros.cassandra.migrate.utils;

import com.github.kuros.cassandra.migrate.exception.MigrationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Properties;

public class Files {

    private Files() {
    }

    public static final Files INSTANCE = new Files();

    public void copyResourceTo(String resource, File toFile) {
        copyResourceTo(resource, toFile, null);
    }

    public void copyResourceTo(String resource, File toFile, Properties variables) {
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resource));
            copyTemplate(inputStreamReader, toFile, variables);
        } catch (IOException e) {
            throw new MigrationException("Error copying " + resource + " to " + toFile.getAbsolutePath() + ".  Cause: " + e,
                    e);
        }
    }

    public static void copyTemplate(File templateFile, File toFile, Properties variables) throws IOException {
        copyTemplate(new FileReader(templateFile), toFile, variables);
    }

    public static void copyTemplate(Reader templateReader, File toFile, Properties variables) throws IOException {
        LineNumberReader reader = new LineNumberReader(templateReader);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(toFile));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = ExpressionResolver.resolveExpression(line, variables);
                    writer.println(line);
                }
            } finally {
                writer.close();
            }
        } finally {
            reader.close();
        }
    }
}
