package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

public class RemoveCommand implements Command {

    private final Repository repository;

    public RemoveCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage newMessage) {

        var success = false;

        var commandParams = newMessage
            .text()
            .substring(2)
            .trim();

        if (commandParams.isEmpty()) {
            success = repository.removeSender(newMessage);
        } else {
            success = repository.removeByName(newMessage, commandParams);
        }

        if (success) repository.listNextEvent(newMessage);
    }

    @Override
    public boolean itsMine(String text) {
        return text
            .substring(0, 2)
            .equalsIgnoreCase("/r");
    }
}
