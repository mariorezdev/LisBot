package dev.seariver.command;

import dev.seariver.NewMessage;

public interface Command {

    void execute(NewMessage event);

    String alias();
}
