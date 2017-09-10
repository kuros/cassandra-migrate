-- // Create ${changelog}

-- Default DDL for ${changelog} table that will keep
-- a record of the migrations that have been run.

-- You can modify this to suit your database before
-- running your first migration.


CREATE TABLE ${changelog} (
    BUCKET int,
    ID int,
    APPLIED text,
    DESCRIPTION text,
    primary key((BUCKET), ID)
);

-- //@UNDO

DROP TABLE ${changelog};