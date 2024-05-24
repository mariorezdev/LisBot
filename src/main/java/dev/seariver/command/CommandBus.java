package dev.seariver.command;

import dev.seariver.Event;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommandBus {

    private static final CommandBus instance = new CommandBus();
    private final Set<Command> commands;

    private CommandBus() {
        this.commands = new HashSet<>();
    }

    public void execute(Event event) {
        findCommand(event.text())
            .ifPresent(command -> command.execute(event));
    }

    public static CommandBus instance() {
        return instance;
    }

    public void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    private Optional<Command> findCommand(String text) {
        return commands.stream()
            .filter(command -> command.alias().equalsIgnoreCase(text))
            .findAny();
    }
}
