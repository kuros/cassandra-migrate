package com.github.kuros.cassandra.migrate.option;

import com.github.kuros.cassandra.migrate.exception.OptionNotFoundException;

import java.io.File;

import static com.github.kuros.cassandra.migrate.utils.Util.isOption;

public final class OptionsParser {

    public static SelectedOptions parse(String[] args) {
        final SelectedOptions selectedOptions = new SelectedOptions();

        for (String arg : args) {
            final boolean isOption = isOption(arg);
            if (isOption) {
                parseOptions(arg, selectedOptions);
            } else {
                setCommandOrAppendParams(arg, selectedOptions);
            }
        }

        return selectedOptions;
    }

    private static void setCommandOrAppendParams(String arg, SelectedOptions options) {
        if (options.getCommand() == null) {
            options.setCommand(arg);
        } else {
            final String myParams = options.getParams() == null ? arg : options.getParams() + " " + arg;
            options.setParams(myParams);
        }
    }

    private static boolean parseOptions(String arg, SelectedOptions options) {
        final boolean isOption = isOption(arg);

        if (isOption) {
            final String[] argParts = arg.substring(2).split("=");
            try {
                final Options option = Options.valueOf(argParts[0].toUpperCase());

                switch (option) {
                    case PATH:
                        options.getPaths().setBasePath(new File(argParts[1]));
                        break;
                    case SCRIPTPATH:
                        options.getPaths().setScriptPath(new File(argParts[1]));
                        break;
                    case ENV:
                        options.setEnvironment(argParts[1]);
                        break;
                    case FORCE:
                        options.setForce(true);
                        break;
                    case TRACE:
                        options.setTrace(true);
                        break;
                    case HELP:
                        options.setHelp(true);
                        break;
                }
            } catch (final Exception e) {
                throw new OptionNotFoundException(argParts[0]);
            }
        }

        return isOption;
    }
}
