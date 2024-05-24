package dev.seariver.command;

import dev.seariver.Event;

public interface Command {

    void execute(Event event);

    String alias();
}
