package com.github.kuros.cassandra.migrate.option;

public class SelectedOptions {
    private SelectedPaths paths = new SelectedPaths();
    private String environment = "development";
    private boolean force;
    private String command;
    private String params;
    private boolean help;
    private boolean trace;

    public SelectedPaths getPaths() {
        return paths;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String aEnvironment) {
        environment = aEnvironment;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean aForce) {
        force = aForce;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String aCommand) {
        command = aCommand;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String aParams) {
        params = aParams;
    }

    public boolean needsHelp() {
        return help;
    }

    public void setHelp(boolean aHelp) {
        help = aHelp;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(final boolean trace) {
        this.trace = trace;
    }
}
