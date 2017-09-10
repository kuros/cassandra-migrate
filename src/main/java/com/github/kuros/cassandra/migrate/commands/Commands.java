/**
 * Copyright 2010-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kuros.cassandra.migrate.commands;

import com.github.kuros.cassandra.migrate.exception.MigrationException;
import com.github.kuros.cassandra.migrate.option.SelectedOptions;

public enum Commands {
    INIT, NEW, UP, DOWN, STATUS;

    public static Command resolveCommand(String commandString, SelectedOptions selectedOptions) {
        for (Commands command : values()) {
            if (command.name().startsWith(commandString)) {
                return createCommand(command, selectedOptions);
            }
        }

        throw new MigrationException("Attempt to execute unknown command: " + commandString);
    }

    private static Command createCommand(Commands aResolvedCommand, SelectedOptions selectedOptions) {
        Command command;
        switch (aResolvedCommand) {
            case INIT:
                command=new InitializeCommand(selectedOptions);
                break;
            case NEW:
                command = new NewCommand(selectedOptions);
                break;
            case UP:
                command = new UpCommand(selectedOptions);
                break;
            case DOWN:
                command = new DownCommand(selectedOptions);
                break;
            default:
                return new Command() {
                    public void execute(String... params) {
                        System.out.println("unknown command");
                    }
                };
        }

        return command;
    }
}
