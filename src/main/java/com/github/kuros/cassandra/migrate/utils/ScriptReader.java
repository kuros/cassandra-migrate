package com.github.kuros.cassandra.migrate.utils;

import com.github.kuros.cassandra.migrate.exception.MigrationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ScriptReader {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    private String target;

    public ScriptReader(File file, String charset, boolean undo) throws IOException {
        this(new FileInputStream(file), charset, undo);
    }

    private ScriptReader(InputStream inputStream, String charset, boolean undo)
            throws IOException {
        final Reader source = scriptFileReader(inputStream, charset);
        try {
            BufferedReader reader = new BufferedReader(source);
            StringBuilder doBuilder = new StringBuilder();
            StringBuilder undoBuilder = new StringBuilder();
            StringBuilder currentBuilder = doBuilder;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().matches("^--\\s*//.*$")) {
                    if (line.contains("@UNDO")) {
                        currentBuilder = undoBuilder;
                    }
                    line = line.replaceFirst("--\\s*//", "-- ");
                }

                if (line.trim().startsWith("--") || line.trim().length() == 0) {
                    continue;
                }
                currentBuilder.append(line);
                currentBuilder.append(LINE_SEPARATOR);
            }
            if (undo) {
                target = undoBuilder.toString();
            } else {
                target = doBuilder.toString();
            }
        } finally {
            source.close();
        }
    }

    public List<String> getQueries(String delimeter) {

        List<String> strings = new ArrayList<String>();

        String str = target;

        while (true) {
            int index = str.indexOf(delimeter);
            if (index < 0) {
                break;
            }
            strings.add(str.substring(0, index));
            str = str.substring(index + 1);
        }

        if (strings.isEmpty() || str.length() > 4) {
            throw new MigrationException("Delimeter missing in statement\n");
        }

        return strings;
    }

    private Reader scriptFileReader(InputStream inputStream, String charset)
            throws FileNotFoundException, UnsupportedEncodingException {
        if (charset == null || charset.length() == 0) {
            return new InputStreamReader(inputStream);
        } else {
            return new InputStreamReader(inputStream, charset);
        }
    }
}
