# cassandra-migrate
Tool to manage cassandra scripts

## Introduction

The entire Migrations system can be accessed through this one simple command. You can access the built-in help by typing: **cassandra-migrate --help**.

Calling the migrate command with no options or invalid options also produces the help message. Here's the output of the help command:

```
Usage: cassandra-migrate commands [parameter] [--path=<directory>] [--env=<environment>] 

--path=<directory>   Path to repository.  Default current working directory.
--env=<environment>  Environment to configure. Default environment is 'development'.
--force              Forces script to continue even if SQL errors are encountered.
--help               Displays this usage message.

Commands:
  info               Display build version informations.
  init               Creates (if necessary) and initializes a migration path.
  new <description>  Creates a new migration with the provided description.
  up [n]             Run unapplied migrations, ALL by default, or 'n' specified.
  down [n]           Undoes migrations applied to the database. ONE by default or 'n' specified.
  status             Prints the changelog from the database if the changelog table exists.


```
