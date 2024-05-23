package dev.seariver.command;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.standard.TextMessage;

import static java.lang.System.out;

public class ListCommand implements Command {
    @Override
    public void onCommand(Whatsapp whatsapp, MessageInfo messageInfo) {

        var textMessage = (TextMessage) messageInfo.message().content();

        out.printf("Received command: %s, from: %s%n", textMessage.text(), messageInfo.senderJid());
    }

    @Override
    public String command() {
        return "/l";
    }
}
