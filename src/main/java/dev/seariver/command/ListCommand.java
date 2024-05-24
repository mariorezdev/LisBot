package dev.seariver.command;

import dev.seariver.Event;

import static java.lang.System.out;

public class ListCommand implements Command {

    @Override
    public void execute(Event event) {

        out.printf("Received command: %s, sender: %s, chat: %s%n",
            event.text(),
            event.senderJid(),
            event.chatJid());
    }

    @Override
    public String alias() {
        return "/l";
    }
}
