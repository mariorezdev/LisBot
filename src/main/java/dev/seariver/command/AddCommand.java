package dev.seariver.command;

import dev.seariver.NewMessage;

public class AddCommand implements Command {

    @Override
    public void execute(NewMessage event) {

        var response = "Received command: %s".formatted(event.text());

        event.reply(response);
    }

    @Override
    public String alias() {
        return "/a";
    }
}
