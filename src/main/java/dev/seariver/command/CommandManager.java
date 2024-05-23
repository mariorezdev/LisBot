package dev.seariver.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommandManager {

    private static final CommandManager instance = new CommandManager();
    private final Set<Command> commands;

    private CommandManager() {
        this.commands = new HashSet<>();
    }

    public static CommandManager instance() {
        return instance;
    }

    public void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public Optional<Command> findCommand(String filter) {
        return commands.stream()
            .filter(command -> command.alias().equalsIgnoreCase(filter.trim()))
            .findAny();
    }
}