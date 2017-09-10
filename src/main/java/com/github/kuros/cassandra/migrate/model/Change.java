package com.github.kuros.cassandra.migrate.model;

import com.github.kuros.cassandra.migrate.exception.MigrationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Change implements Comparable {

    private static final String FORMAT = "yyyyMMddHHmmss";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT);

    private String id;
    private String description;
    private String fileName;
    private Date date;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
        setDate(id);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    private void setDate(final String date) {
        try {
            this.date = DATE_FORMAT.parse(date);
        } catch (final ParseException e) {
            throw new MigrationException("Not able to parse id");
        }
    }

    public Date getDate() {
        return date;
    }

    public int compareTo(final Change o) {
        return this.date.compareTo(o.getDate());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int compareTo(final Object o) {
        if (o instanceof Change) {
            final Change other = (Change) o;
            return this.date.compareTo(other.getDate());
        }
        return 0;
    }
}
