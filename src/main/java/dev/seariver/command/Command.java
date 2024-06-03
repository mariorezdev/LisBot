package dev.seariver.command;

import dev.seariver.NewMessage;

public interface Command {

    default void run(NewMessage newMessage) {

        var breakLine = newMessage.text().split("\\R").length > 1;

        if (breakLine) {
            newMessage.response("Assim nao, ne \uD83E\uDD28");
            return;
        }

        execute(newMessage);
    }

    void execute(NewMessage newMessage);

    boolean itsMine(String text);
}
