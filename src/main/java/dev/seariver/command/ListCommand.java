package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

public class ListCommand implements Command {

    private final Repository repository;

    public ListCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage newMessage) {
        repository.listNextEvent(newMessage);
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/l");
    }
}
