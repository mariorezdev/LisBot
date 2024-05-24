package dev.seariver;

import dev.seariver.command.CommandBus;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.model.info.MessageInfo;

import static java.lang.System.out;

public record CommandListener(CommandBus commandBus) implements Listener {

    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo messageInfo) {

        out.printf("New message: %s%n", messageInfo.toJson());

        var event = new Event(whatsapp, messageInfo);

        if (event.text().isEmpty()) {
            return;
        }

        commandBus.execute(event);
    }
}
