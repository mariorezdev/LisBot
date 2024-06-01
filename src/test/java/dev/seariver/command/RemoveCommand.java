package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

public class RemoveCommand implements Command {

    private final Repository repository;

    public RemoveCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage event) {

    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/r");
    }
}
