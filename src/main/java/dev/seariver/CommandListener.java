package dev.seariver;

import dev.seariver.command.CommandManager;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.standard.TextMessage;

import static java.lang.System.out;

public record CommandListener(CommandManager commandManager) implements Listener {

    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo messageInfo) {

        out.printf("New message: %s%n", messageInfo.toJson());

        if (!(messageInfo.message().content() instanceof TextMessage textMessage)) {
            return;
        }

        commandManager
            .findCommand(textMessage.text())
            .ifPresent(command -> command.execute(whatsapp, messageInfo));
    }
}
