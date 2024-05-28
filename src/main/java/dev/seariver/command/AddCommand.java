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

        repository.addPersonOnNextEvent(newMessage);
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/a");
    }
}
