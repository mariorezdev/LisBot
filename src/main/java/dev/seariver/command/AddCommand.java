package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

public class AddCommand implements Command {

    private final Repository repository;

    public AddCommand(Repository repository) {
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
            success = repository.addPersonOnNextEvent(newMessage, newMessage.senderName());
            if (!success) return;
            repository.listNextEvent(newMessage);
            return;
        }

        var nameList = commandParams.split(",");

        for (String name : nameList) {
            newMessage.names(name.trim());
            success = repository.addPersonOnNextEvent(newMessage, name.trim());
            if (!success) break;
        }

        if (!success) return;

        repository.listNextEvent(newMessage);
    }

    @Override
    public boolean itsMine(String text) {
        return text
            .substring(0, 2)
            .equalsIgnoreCase("/a");
    }
}
